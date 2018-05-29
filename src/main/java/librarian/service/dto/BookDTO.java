package librarian.service.dto;

import librarian.domain.Book;
import librarian.domain.Member;
import librarian.domain.Setting;
import lombok.Data;

@Data
public class BookDTO {
// ------------------------------ FIELDS ------------------------------

    private Book book;
    private Member member;
    private Setting setting;
}
