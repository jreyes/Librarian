package librarian.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import librarian.alert.AlertMaker;
import librarian.domain.Book;
import librarian.service.BookService;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

@FXMLController
public class BookEditController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    JFXTextField author;
    @FXML
    JFXTextField publisher;
    @FXML
    StackPane rootPane;
    @FXML
    JFXTextField title;

    private Book book;
    private final BookService bookService;

// --------------------------- CONSTRUCTORS ---------------------------

    public BookEditController(BookService bookService) {
        this.bookService = bookService;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
    }

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings("WeakerAccess")
    public void loadBook(String bookId) {
        bookService.getBook(bookId).ifPresent(book -> this.book = book);
    }

    @SuppressWarnings("unused")
    @FXML
    private void cancel(ActionEvent event) {
        title.clear();
        author.clear();
        publisher.clear();
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @SuppressWarnings("SameParameterValue")
    private void error(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showErrorMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("SameParameterValue")
    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("unused")
    @FXML
    private void updateBook(ActionEvent event) {
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();

        if (bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            error("Insufficient Data", "Please enter data in all fields.");
            return;
        }

        bookService.updateBook(book.getBookId(), bookName, bookAuthor, bookPublisher);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.addEventHandler(MOUSE_CLICKED, ev -> cancel(new ActionEvent()));
        success("New book updated", bookName + " has been updated", closeButton);
    }
}
