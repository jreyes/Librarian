package librarian.ui;

import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import librarian.LibrarianApp;
import librarian.alert.AlertMaker;
import librarian.domain.Member;
import librarian.service.MemberService;
import librarian.util.LibraryAssistantUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MemberListController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    TableColumn<Item, String> emailCol;
    @FXML
    TableColumn<Item, String> idCol;
    @FXML
    TableColumn<Item, String> mobileCol;
    @FXML
    TableColumn<Item, String> nameCol;
    @FXML
    StackPane rootPane;
    @FXML
    TableView<Item> tableView;

    private ObservableList<Item> list;
    private final MemberEditController memberEditController;
    private final MemberService memberService;

// --------------------------- CONSTRUCTORS ---------------------------

    public MemberListController(MemberEditController memberEditController,
                                MemberService memberService) {
        this.memberEditController = memberEditController;
        this.list = FXCollections.observableArrayList();
        this.memberService = memberService;
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

    @SuppressWarnings("SameParameterValue")
    private void error(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showErrorMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("unused")
    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"   Name    ", "ID", "Mobile", "    Email   "};
        printData.add(Arrays.asList(headers));
        for (Item item : list) {
            List<String> row = new ArrayList<>();
            row.add(item.getName());
            row.add(item.getId());
            row.add(item.getMobile());
            row.add(item.getEmail());
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExprot(rootPane, getStage(), printData);
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleMemberDelete(ActionEvent event) {
        //Fetch the selected row
        Item selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            error("No member selected", "Please select a member for deletion.");
            return;
        }
        if (memberService.hasAnyBooks(selectedForDeletion.getId())) {
            error("Cant be deleted", "This member has some books.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting member");
        alert.setContentText("Are you sure want to delete " + selectedForDeletion.getName() + " ?");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                memberService.deleteMember(selectedForDeletion.getId());
                list.remove(selectedForDeletion);
                success("Member deleted", selectedForDeletion.getName() + " was deleted successfully.");
            } else {
                error("Deletion cancelled", "Deletion process cancelled");
            }
        });
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleMemberEdit(ActionEvent event) {
        //Fetch the selected row
        Item selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            error("No member selected", "Please select a member for edit.");
            return;
        }

        memberEditController.loadMember(selectedForEdit.getId());
        LibrarianApp.showView(MemberEditView.class, Modality.WINDOW_MODAL);
        handleRefresh(new ActionEvent());
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadData() {
        list.clear();
        memberService.getMembers().forEach(member -> list.add(new Item(member)));
        tableView.setItems(list);
    }

    @SuppressWarnings("SameParameterValue")
    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }

// -------------------------- INNER CLASSES --------------------------

    @SuppressWarnings("WeakerAccess")
    public static class Item {
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Item(Member member) {
            this.name = new SimpleStringProperty(member.getName());
            this.id = new SimpleStringProperty(member.getMemberId());
            this.mobile = new SimpleStringProperty(member.getMobile());
            this.email = new SimpleStringProperty(member.getEmail());
        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }
    }
}
