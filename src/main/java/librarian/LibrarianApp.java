package librarian;

import de.felixroske.jfxsupport.AbstractJavaFxApplication;
import librarian.ui.LoginView;
import librarian.ui.SplashView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static librarian.util.LibraryAssistantUtil.formatDateTime;

@SpringBootApplication
public class LibrarianApp extends AbstractJavaFxApplication {
// ------------------------------ FIELDS ------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(LibrarianApp.class);

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        final LocalDateTime startTime = LocalDateTime.now();
        LOGGER.info("Library Assistant launched on {}", formatDateTime(startTime));
        launch(LibrarianApp.class, LoginView.class, new SplashView(), args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final LocalDateTime endTime = LocalDateTime.now();
            LOGGER.info(
                    "Library Assistant is closing on {}. Used for {} ms",
                    formatDateTime(endTime),
                    ChronoUnit.MICROS.between(startTime, endTime)
            );
        }));
    }
}
