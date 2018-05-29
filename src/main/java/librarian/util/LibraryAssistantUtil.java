package librarian.util;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import librarian.alert.AlertMaker;
import librarian.domain.Setting;
import librarian.pdf.ListToPDF;
import librarian.ui.MainController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryAssistantUtil {
// ------------------------------ FIELDS ------------------------------

    public static final String ICON_IMAGE_LOC = "/images/icon.png";
    private static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

// -------------------------- STATIC METHODS --------------------------

    public static String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return LOCAL_DATE_FORMAT.format(localDateTime);
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return LOCAL_DATE_TIME_FORMAT.format(localDateTime);
    }

    public static float getFineAmount(Setting setting, LocalDateTime issuedTime) {
        int fineDays = getIssuedDays(issuedTime) - setting.getDaysWithoutFine();
        float fine = 0f;
        if (fineDays > 0) {
            fine = fineDays * setting.getFinePerDay();
        }
        return fine;
    }

    public static int getIssuedDays(LocalDateTime issuedTime) {
        if (issuedTime == null) {
            return 0;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(issuedTime, LocalDateTime.now())) + 1;
    }

    public static void initPDFExprot(StackPane rootPane, Stage stage, List<List> data) {
        ExtensionFilter extFilter = new ExtensionFilter("PDF files (*.pdf)", "*.pdf");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PDF");
        fileChooser.getExtensionFilters().add(extFilter);

        File saveLoc = fileChooser.showSaveDialog(stage);

        JFXButton okayBtn = new JFXButton("Okay");
        JFXButton openBtn = new JFXButton("View File");
        openBtn.setOnAction(ev -> {
            try {
                Desktop.getDesktop().open(saveLoc);
            } catch (Exception exp) {
                AlertMaker.showErrorMessage(rootPane, "Could not load file", "Cant load file");
            }
        });

        boolean flag = new ListToPDF().doPrintToPdf(rootPane, data, saveLoc, ListToPDF.Orientation.LANDSCAPE);
        if (flag) {
            AlertMaker.showSuccessMessage(rootPane, "Completed", "Member data has been exported.", okayBtn, openBtn);
        }
    }

    public static Object loadWindow(URL loc, String title, Stage parentStage) {
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            Stage stage = null;
            if (parentStage != null) {
                stage = parentStage;
            } else {
                stage = new Stage(StageStyle.DECORATED);
            }
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return controller;
    }

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }
}
