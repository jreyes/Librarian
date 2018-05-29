package librarian.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Stage;
import librarian.service.BookService;
import librarian.service.dto.BookDTO;
import librarian.util.LibraryAssistantUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static librarian.util.LibraryAssistantUtil.*;

@FXMLController
public class IssuedListController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    TableColumn<Item, String> bookIDCol;
    @FXML
    TableColumn<Item, String> bookNameCol;
    @FXML
    TableColumn<Item, Integer> daysCol;
    @FXML
    TableColumn<Item, Float> fineCol;
    @FXML
    TableColumn<Item, String> holderNameCol;
    @FXML
    TableColumn<Item, String> issueCol;
    @FXML
    StackPane rootPane;
    @FXML
    TableView<Item> tableView;

    private final BookService bookService;
    private ObservableList<Item> list = FXCollections.observableArrayList();
    private final MainController mainController;

// --------------------------- CONSTRUCTORS ---------------------------

    public IssuedListController(BookService bookService, MainController mainController) {
        this.bookService = bookService;
        this.mainController = mainController;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    @SuppressWarnings("unused")
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    @SuppressWarnings("unused")
    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"BOOK ID", "      BOOK NAME      ", "    HOLDER NAME    ", "ISSUE DATE", "DAYS ELAPSED", "FINE"};
        printData.add(Arrays.asList(headers));
        for (Item info : list) {
            List<String> row = new ArrayList<>();
            row.add(info.getBookID());
            row.add(info.getBookName());
            row.add(info.getHolderName());
            row.add(info.getDateOfIssue());
            row.add(String.valueOf(info.getDays()));
            row.add(String.valueOf(info.getFine()));
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExprot(rootPane, getStage(), printData);
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleReturn(ActionEvent event) {
        Item issueInfo = tableView.getSelectionModel().getSelectedItem();
        if (issueInfo != null) {
            mainController.loadBookReturn(issueInfo.getBookID());
            closeStage(event);
        }
    }

    private void initCol() {
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        holderNameCol.setCellValueFactory(new PropertyValueFactory<>("holderName"));
        issueCol.setCellValueFactory(new PropertyValueFactory<>("dateOfIssue"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        fineCol.setCellValueFactory(new PropertyValueFactory<>("fine"));
        tableView.setItems(list);
    }

    private void loadData() {
        list.clear();
        bookService.getBookDetails().forEach(bookDTO -> list.add(new Item(bookDTO)));
        tableView.setItems(list);
    }

// -------------------------- INNER CLASSES --------------------------

    @SuppressWarnings("WeakerAccess")
    public static class Item {
        private final SimpleStringProperty bookID;
        private final SimpleStringProperty bookName;
        private final SimpleStringProperty holderName;
        private final SimpleStringProperty dateOfIssue;
        private final SimpleIntegerProperty nDays;
        private final SimpleFloatProperty fine;

        public Item(BookDTO bookDTO) {
            this.bookID = new SimpleStringProperty(bookDTO.getBook().getBookId());
            this.bookName = new SimpleStringProperty(bookDTO.getBook().getTitle());
            this.holderName = new SimpleStringProperty(bookDTO.getMember().getName());
            this.dateOfIssue = new SimpleStringProperty(formatDate(bookDTO.getBook().getIssueTime()));
            this.nDays = new SimpleIntegerProperty(getIssuedDays(bookDTO.getBook().getIssueTime()));
            this.fine = new SimpleFloatProperty(getFineAmount(bookDTO.getSetting(), bookDTO.getBook().getIssueTime()));
        }

        public String getBookID() {
            return bookID.get();
        }

        public String getBookName() {
            return bookName.get();
        }

        public String getHolderName() {
            return holderName.get();
        }

        public String getDateOfIssue() {
            return dateOfIssue.get();
        }

        public Integer getDays() {
            return nDays.get();
        }

        public Float getFine() {
            return fine.get();
        }
    }
}
