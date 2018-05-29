package librarian.ui;

import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import librarian.LibrarianApp;
import librarian.alert.AlertMaker;
import librarian.domain.Book;
import librarian.service.BookService;
import librarian.util.LibraryAssistantUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

@FXMLController
public class BookListController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    TableColumn<Item, String> authorCol;
    @FXML
    TableColumn<Item, Boolean> availabilityCol;
    @FXML
    TableColumn<Item, String> idCol;
    @FXML
    TableColumn<Item, String> publisherCol;
    @FXML
    StackPane rootPane;
    @FXML
    TableView<Item> tableView;
    @FXML
    TableColumn<Item, String> titleCol;

    private final BookEditController bookEditController;
    private final BookService bookService;
    private ObservableList<Item> list;

// --------------------------- CONSTRUCTORS ---------------------------

    public BookListController(BookEditController bookEditController,
                              BookService bookService) {
        this.bookEditController = bookEditController;
        this.bookService = bookService;
        this.list = FXCollections.observableArrayList();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        refreshData();
    }

    @SuppressWarnings("unused")
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    @SuppressWarnings("SameParameterValue")
    private void error(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showErrorMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("unused")
    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"   Title   ", "ID", "  Author  ", "  Publisher  ", "  Avail  "};
        printData.add(Arrays.asList(headers));
        for (Item item : list) {
            List<String> row = new ArrayList<>();
            row.add(item.getTitle());
            row.add(item.getId());
            row.add(item.getAuthor());
            row.add(item.getPublisher());
            row.add(item.getAvailabilty());
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExprot(rootPane, getStage(), printData);
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleBookDeleteOption(ActionEvent event) {
        //Fetch the selected row
        Item selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            error("No book selected", "Please select a book for deletion.");
            return;
        }
        if (bookService.isAlreadyIssued(selectedForDeletion.getId())) {
            error("Cant be deleted", "This book is already issued and cant be deleted.");
            return;
        }

        JFXButton okButton = new JFXButton("Delete book");
        okButton.addEventHandler(MOUSE_CLICKED, ev -> {
            bookService.deleteBook(selectedForDeletion.getId());
            list.remove(selectedForDeletion);
            success("Book deleted", selectedForDeletion.getTitle() + " was deleted successfully.");
        });
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.addEventHandler(MOUSE_CLICKED, ev -> error("Deletion cancelled", "Deletion process cancelled"));
        success("Deleting book", "Are you sure want to delete the book " + selectedForDeletion.getTitle() + " ?", okButton, cancelButton);
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleBookEditOption(ActionEvent event) {
        //Fetch the selected row
        Item selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            error("No book selected", "Please select a book for edit.");
            return;
        }

        bookEditController.loadBook(selectedForEdit.getId());
        LibrarianApp.showView(BookEditView.class, Modality.WINDOW_MODAL);
        handleRefresh(new ActionEvent());
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleRefresh(ActionEvent event) {
        refreshData();
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availabilty"));
    }

    private void refreshData() {
        list.clear();
        bookService.getBooks().forEach(book -> list.add(new Item(book)));
        tableView.setItems(list);
    }

    @SuppressWarnings("SameParameterValue")
    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }

// -------------------------- INNER CLASSES --------------------------

    @SuppressWarnings("WeakerAccess")
    public static class Item {
        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty availabilty;

        public Item(Book book) {
            this.title = new SimpleStringProperty(book.getTitle());
            this.id = new SimpleStringProperty(book.getBookId());
            this.author = new SimpleStringProperty(book.getAuthor());
            this.publisher = new SimpleStringProperty(book.getPublisher());
            if (book.isAvailable()) {
                this.availabilty = new SimpleStringProperty("Available");
            } else {
                this.availabilty = new SimpleStringProperty("Issued");
            }
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getAvailabilty() {
            return availabilty.get();
        }
    }
}
