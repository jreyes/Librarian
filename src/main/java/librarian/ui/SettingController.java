package librarian.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import librarian.service.AdminService;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class SettingController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    @FXML
    JFXPasswordField apiKey;
    @FXML
    JFXTextField finePerDay;
    @FXML
    JFXTextField nDaysWithoutFine;
    @FXML
    JFXPasswordField password;
    @FXML
    JFXTextField username;

    private final AdminService adminService;

// --------------------------- CONSTRUCTORS ---------------------------

    public SettingController(AdminService adminService) {
        this.adminService = adminService;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }

    private void close() {
        ((Stage) nDaysWithoutFine.getScene().getWindow()).close();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        close();
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        int ndays = Integer.parseInt(nDaysWithoutFine.getText());
        float fine = Float.parseFloat(finePerDay.getText());
        String uname = username.getText();
        String pass = password.getText();
        String nApiKey = apiKey.getText();

        adminService.updateSettings(uname, pass, fine, ndays, nApiKey);
        close();
    }

    private void initDefaultValues() {
        adminService.getSettings().ifPresent(setting -> {
            nDaysWithoutFine.setText(String.valueOf(setting.getDaysWithoutFine()));
            finePerDay.setText(String.valueOf(setting.getFinePerDay()));
            username.setText(setting.getUsername());
            password.setText(setting.getPassword());
            apiKey.setText(setting.getApiKey());
        });
    }
}
