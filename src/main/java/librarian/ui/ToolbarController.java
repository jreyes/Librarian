package librarian.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@FXMLController
public class ToolbarController {
// ------------------------------ FIELDS ------------------------------

    private final MainController mainController;

// --------------------------- CONSTRUCTORS ---------------------------

    public ToolbarController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        mainController.handleAddBook(event);
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        mainController.handleAddMember(event);
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        mainController.handleBookList(event);
    }

    @FXML
    private void loadIssuedBookList(ActionEvent event) {
        mainController.handleIssueList(event);
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        mainController.handleMemberList(event);
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        mainController.handleSettings(event);
    }
}
