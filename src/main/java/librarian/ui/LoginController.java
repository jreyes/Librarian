package librarian.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import librarian.LibrarianApp;
import librarian.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FXMLController
public class LoginController {
// ------------------------------ FIELDS ------------------------------

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class.getName());

    @FXML
    JFXPasswordField password;
    @FXML
    JFXTextField username;

    private final AdminService adminService;

// --------------------------- CONSTRUCTORS ---------------------------

    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = username.getText();
        String pword = password.getText();

        if (adminService.authenticate(uname, pword)) {
            closeStage();
            LibrarianApp.showView(MainView.class);
            LOGGER.info("User successfully logged in {}", uname);
        } else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }
}
