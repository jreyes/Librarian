package librarian.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import librarian.alert.AlertMaker;
import librarian.service.BookService;

@FXMLController
public class BookAddController {
// ------------------------------ FIELDS ------------------------------

    @FXML
    JFXTextField author;
    @FXML
    JFXTextField id;
    @FXML
    JFXTextField isbn;
    @FXML
    JFXTextField publisher;
    @FXML
    StackPane rootPane;
    @FXML
    JFXTextField title;

    private final BookService bookService;

// --------------------------- CONSTRUCTORS ---------------------------

    public BookAddController(BookService bookService) {
        this.bookService = bookService;
    }

    @SuppressWarnings("unused")
    @FXML
    private void addBook(ActionEvent event) {
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();

        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            error("Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (bookService.exists(bookID)) {
            error("Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
            return;
        }

        bookService.addBook(bookID, bookName, bookAuthor, bookPublisher);

        success("New book added", bookName + " has been added");
        clearEntries();
    }

    @SuppressWarnings("unused")
    @FXML
    private void cancel(ActionEvent event) {
        clearEntries();
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    private void clearEntries() {
        isbn.clear();
        title.clear();
        id.clear();
        author.clear();
        publisher.clear();
    }

    @SuppressWarnings("SameParameterValue")
    private void error(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showErrorMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleBookSearch(ActionEvent event) {
        final String isbnValue = isbn.getText();
        clearEntries();
        bookService.findByISBN(isbnValue).ifPresent(book -> {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            publisher.setText(book.getPublisher());
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }
}
