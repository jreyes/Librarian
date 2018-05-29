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
import librarian.domain.Member;
import librarian.service.MemberService;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

@FXMLController
public class MemberEditController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    JFXTextField email;
    @FXML
    JFXTextField mobile;
    @FXML
    JFXTextField name;
    @FXML
    StackPane rootPane;

    private Member member;
    private final MemberService memberService;

// --------------------------- CONSTRUCTORS ---------------------------

    public MemberEditController(MemberService memberService) {
        this.memberService = memberService;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(member.getName());
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());
    }

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings("WeakerAccess")
    public void loadMember(String memberId) {
        memberService.getMember(memberId).ifPresent(member -> this.member = member);
    }

    @SuppressWarnings("unused")
    @FXML
    private void cancel(ActionEvent event) {
        name.clear();
        mobile.clear();
        email.clear();
        ((Stage) name.getScene().getWindow()).close();
    }

    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }

    @SuppressWarnings("unused")
    @FXML
    private void updateMember(ActionEvent event) {
        String mName = name.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();

        if (mName.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty()) {
            success("Insufficient Data", "Please enter data in all fields.");
            return;
        }

        memberService.updateMember(member.getMemberId(), mName, mMobile, mEmail);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.addEventHandler(MOUSE_CLICKED, ev -> cancel(new ActionEvent()));
        success("Member updated", mName + " has been updated", closeButton);
    }
}
