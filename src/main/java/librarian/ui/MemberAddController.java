package librarian.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import librarian.alert.AlertMaker;
import librarian.service.MemberService;

@FXMLController
public class MemberAddController {
// ------------------------------ FIELDS ------------------------------

    @FXML
    JFXTextField email;
    @FXML
    JFXTextField id;
    @FXML
    JFXTextField mobile;
    @FXML
    JFXTextField name;
    @FXML
    StackPane rootPane;

    private final MemberService memberService;

// --------------------------- CONSTRUCTORS ---------------------------

    public MemberAddController(MemberService memberService) {
        this.memberService = memberService;
    }

    @SuppressWarnings("unused")
    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();

        if (mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty()) {
            error("Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (memberService.exists(mID)) {
            error("Duplicate member id", "Member with same id exists.\nPlease use new ID");
            return;
        }

        memberService.addMember(mName, mID, mMobile, mEmail);
        success("New member added", mName + " has been added");
        clearEntries();
    }

    @SuppressWarnings("unused")
    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) name.getScene().getWindow()).close();
    }

    private void clearEntries() {
        name.clear();
        id.clear();
        mobile.clear();
        email.clear();
    }

    @SuppressWarnings("SameParameterValue")
    private void error(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showErrorMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("SameParameterValue")
    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }
}
