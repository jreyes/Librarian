package librarian.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import librarian.alert.AlertMaker;
import librarian.util.LibraryAssistantUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class AboutController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    private static final String FACEBOOK = "https://www.facebook.com/vaporwarecorp";
    private static final String YOUTUBE = "https://www.youtube.com/user/vaporwarecorp";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AlertMaker.showTrayMessage(
                String.format("Hello %s!", System.getProperty("user.name")),
                "Thanks for trying out Librarian"
        );
    }

    private void handleWebPageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.setTitle("Librarian");
        stage.show();
        LibraryAssistantUtil.setStageIcon(stage);
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadFacebook(ActionEvent event) {
        loadWebPage(FACEBOOK);
    }

    private void loadWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebPageLoadException(url);
        }
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadYoutubeChannel(ActionEvent event) {
        loadWebPage(YOUTUBE);
    }
}
