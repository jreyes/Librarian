package librarian.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import librarian.domain.Book;
import librarian.domain.Issue;
import librarian.domain.Setting;
import librarian.repository.BookRepository;
import librarian.repository.IssueRepository;
import librarian.repository.MemberRepository;
import librarian.repository.SettingRepository;
import librarian.service.dto.BookDTO;
import librarian.service.dto.ResultDTO;
import librarian.service.dto.VolumeInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;

@Service
@Transactional
public class BookService {
// ------------------------------ FIELDS ------------------------------

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q={q}&maxResults=1&startIndex=0&key={key}";

    private final BookRepository bookRepository;
    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final SettingRepository settingRepository;

// --------------------------- CONSTRUCTORS ---------------------------

    public BookService(BookRepository bookRepository,
                       IssueRepository issueRepository,
                       MemberRepository memberRepository,
                       RestTemplateBuilder restTemplateBuilder,
                       SettingRepository settingRepository) {
        this.bookRepository = bookRepository;
        this.issueRepository = issueRepository;
        this.memberRepository = memberRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.settingRepository = settingRepository;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addBook(String bookId, String title, String author, String publisher) {
        Book book = new Book();
        book.setAvailable(true);
        book.setAuthor(author);
        book.setBookId(bookId);
        book.setPublisher(publisher);
        book.setTitle(title);
        book.setRenewCount(0);
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public ObservableList<PieChart.Data> bookGraphStatistics() {
        long bookCount = bookRepository.count();
        long issueCount = issueRepository.count();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Total Books (" + bookCount + ")", bookCount));
        data.add(new PieChart.Data("Issued Books (" + issueCount + ")", issueCount));
        return data;
    }

    public void deleteBook(String bookId) {
        bookRepository.deleteById(bookId);
    }

    @Transactional(readOnly = true)
    public boolean exists(String bookId) {
        return bookRepository.existsById(bookId);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findByISBN(String isbn) {
        if (StringUtils.isNotEmpty(isbn)) {
            final Setting setting = getSetting();
            if (setting != null) {
                try {
                    ResultDTO result = restTemplate.getForObject(
                            API_URL,
                            ResultDTO.class,
                            of("q", isbn, "key", setting.getApiKey())
                    );
                    return Optional.ofNullable(resultDtoToBook(result));
                } catch (HttpClientErrorException ignored) {
                }
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBook(String bookId) {
        return bookRepository.findById(bookId);
    }

    @Transactional(readOnly = true)
    public BookDTO getBookDetail(String bookId) {
        return issueRepository.findById(bookId).map(this::issueToBookDTO).orElseGet(BookDTO::new);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBookDetails() {
        return issueRepository.findAll().stream().map(this::issueToBookDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean isAlreadyIssued(String bookId) {
        return issueRepository.existsById(bookId);
    }

    public void issueBook(String bookId, String memberId) {
        memberRepository.findById(memberId).ifPresent(member ->
                bookRepository.findById(bookId).ifPresent(book -> {
                    Issue issue = new Issue();
                    issue.setBookId(bookId);
                    issue.setMemberId(memberId);
                    issueRepository.save(issue);

                    book.setAvailable(false);
                    book.setIssueTime(LocalDateTime.now());
                    book.setRenewCount(0);
                    bookRepository.save(book);
                })
        );
    }

    public void renewIssue(String bookId) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setIssueTime(LocalDateTime.now());
            book.setRenewCount(book.getRenewCount() + 1);
            bookRepository.save(book);
        });
    }

    public void returnBook(String bookId) {
        issueRepository.findById(bookId).ifPresent(issue ->
                bookRepository.findById(bookId).ifPresent(book -> {
                    issueRepository.delete(issue);

                    book.setAvailable(true);
                    bookRepository.save(book);
                })
        );
    }

    public void updateBook(String bookId, String title, String author, String publisher) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setTitle(title);
            bookRepository.save(book);
        });
    }

    private Setting getSetting() {
        return settingRepository.findAll().stream().findFirst().orElse(null);
    }

    private BookDTO issueToBookDTO(Issue issue) {
        BookDTO bookDTO = new BookDTO();
        bookRepository.findById(issue.getBookId()).ifPresent(bookDTO::setBook);
        memberRepository.findById(issue.getMemberId()).ifPresent(bookDTO::setMember);
        settingRepository.findAll().stream().findFirst().ifPresent(bookDTO::setSetting);
        return bookDTO;
    }

    private Book resultDtoToBook(ResultDTO result) {
        if (result == null || result.getItems() == null || result.getItems().isEmpty()) {
            return null;
        }

        VolumeInfoDTO volumeInfo = result.getItems().get(0).getVolumeInfo();
        Book book = new Book();
        book.setTitle(volumeInfo.getTitle());
        book.setAuthor(StringUtils.join(volumeInfo.getAuthors(), ","));
        book.setPublisher(volumeInfo.getPublisher());
        return book;
    }
}
