package librarian.alert;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import librarian.util.LibraryAssistantUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class AlertMaker {
// ------------------------------ FIELDS ------------------------------

    private static final BoxBlur BLUR_EFFECT = new BoxBlur(3, 3, 3);
    private static final String THEME_LOC = AlertMaker.class.getResource("/styles/dark-theme.css").toExternalForm();

// -------------------------- STATIC METHODS --------------------------

    public static void addBlurEffect(Node nodeToBeBlurred) {
        nodeToBeBlurred.setEffect(BLUR_EFFECT);
    }

    public static void removeBlurEffect(Node blurredNode) {
        blurredNode.setEffect(null);
    }

    @SuppressWarnings("unused")
    public static void showErrorMessage(Node nodeToBeBlurred, String header, String body, JFXButton... controls) {
        showMaterialDialog(nodeToBeBlurred, "dialog-error", header, body, controls);
    }

    @SuppressWarnings("unused")
    public static void showInfoMessage(Node nodeToBeBlurred, String header, String body, JFXButton... controls) {
        showMaterialDialog(nodeToBeBlurred, "dialog-info", header, body, controls);
    }

    private static void showMaterialDialog(Node nodeToBeBlurred,
                                           String styleClass,
                                           String header,
                                           String body,
                                           JFXButton... controls) {
        List<JFXButton> buttons;
        if (controls.length == 0) {
            buttons = Collections.singletonList(new JFXButton("OK"));
        } else {
            buttons = Arrays.asList(controls);
        }

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.getStyleClass().add("jfx-dialog-layout");
        dialogLayout.getStyleClass().add(styleClass);

        JFXAlert<String> alert = new JFXAlert<>();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.getDialogPane().getStylesheets().add(THEME_LOC);
        alert.getDialogPane().setPadding(new Insets(20, 20, 20, 20));

        buttons.forEach(button -> button.addEventHandler(MOUSE_CLICKED, ev -> {
            alert.hideWithAnimation();
            removeBlurEffect(nodeToBeBlurred);
        }));

        dialogLayout.setHeading(new Label(header));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(buttons);

        addBlurEffect(nodeToBeBlurred);

        alert.setContent(dialogLayout);
        alert.showAndWait();
    }

    @SuppressWarnings("unused")
    public static void showSuccessMessage(Node nodeToBeBlurred, String header, String body, JFXButton... controls) {
        showMaterialDialog(nodeToBeBlurred, "dialog-success", header, body, controls);
    }

    public static void showTrayMessage(String title, String message) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            BufferedImage image = ImageIO.read(AlertMaker.class.getResource(LibraryAssistantUtil.ICON_IMAGE_LOC));
            TrayIcon trayIcon = new TrayIcon(image, "Library Assistant");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Library Assistant");
            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, MessageType.INFO);
            tray.remove(trayIcon);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
