package librarian.ui;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import librarian.LibrarianApp;
import librarian.alert.AlertMaker;
import librarian.service.BookService;
import librarian.service.MemberService;
import librarian.service.dto.BookDTO;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import static librarian.util.LibraryAssistantUtil.*;

@FXMLController
public class MainController implements Initializable {
// ------------------------------ FIELDS ------------------------------

    private static final String BOOK_AVAILABLE = "Available";
    private static final String BOOK_NOT_AVAILABLE = "Not Available";
    private static final String NO_SUCH_BOOK_AVAILABLE = "No Such Book Available";
    private static final String NO_SUCH_MEMBER_AVAILABLE = "No Such Member Available";

    @FXML
    Text bookAuthor;
    @FXML
    Text bookAuthorHolder;
    @FXML
    JFXTextField bookID;
    @FXML
    TextField bookIDInput;
    @FXML
    StackPane bookInfoContainer;
    @FXML
    Tab bookIssueTab;
    @FXML
    Text bookName;
    @FXML
    Text bookNameHolder;
    @FXML
    Text bookPublisherHolder;
    @FXML
    Text bookStatus;
    @FXML
    HBox book_info;
    @FXML
    JFXDrawer drawer;
    @FXML
    Text fineInfoHolder;
    @FXML
    JFXHamburger hamburger;
    @FXML
    Text issueDateHolder;
    @FXML
    JFXTabPane mainTabPane;
    @FXML
    Text memberContactHolder;
    @FXML
    Text memberEmailHolder;
    @FXML
    TextField memberIDInput;
    @FXML
    StackPane memberInfoContainer;
    @FXML
    Text memberMobile;
    @FXML
    Text memberName;
    @FXML
    Text memberNameHolder;
    @FXML
    HBox member_info;
    @FXML
    Text numberDaysHolder;
    @FXML
    JFXButton renewButton;
    @FXML
    Tab renewTab;
    @FXML
    StackPane rootPane;
    @FXML
    JFXButton submissionButton;
    @FXML
    HBox submissionDataContainer;

    private PieChart bookChart;
    private final BookService bookService;
    private Boolean isReadyForSubmission;
    private PieChart memberChart;
    private final MemberService memberService;
    private final ToolbarView toolbarView;

// --------------------------- CONSTRUCTORS ---------------------------

    public MainController(BookService bookService,
                          MemberService memberService,
                          ToolbarView toolbarView) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.toolbarView = toolbarView;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Initializable ---------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);

        initDrawer();
        initGraphs();
    }

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings("unused")
    @FXML
    public void handleAbout(ActionEvent event) {
        showView(AboutView.class);
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleAddBook(ActionEvent event) {
        showView(BookAddView.class);
        refreshGraphs();
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleAddMember(ActionEvent event) {
        showView(MemberAddView.class);
        refreshGraphs();
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleBookList(ActionEvent event) {
        showView(BookListView.class);
        closeDrawer();
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleClose(ActionEvent event) {
        getStage().close();
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleIssueList(ActionEvent event) {
        showView(IssuedListView.class);
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleMemberList(ActionEvent event) {
        showView(MemberListView.class);
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleSettings(ActionEvent event) {
        showView(SettingView.class);
    }

    @SuppressWarnings("WeakerAccess")
    public void loadBookReturn(String bookId) {
        this.bookID.setText(bookId);
        mainTabPane.getSelectionModel().select(renewTab);
        loadBookInfo2(null);
        getStage().toFront();
        closeDrawer();
    }

    private boolean checkForIssueValidity() {
        bookIDInput.fireEvent(new ActionEvent());
        memberIDInput.fireEvent(new ActionEvent());
        return bookIDInput.getText().isEmpty()
                || memberIDInput.getText().isEmpty()
                || memberName.getText().isEmpty()
                || bookName.getText().isEmpty()
                || bookName.getText().equals(NO_SUCH_BOOK_AVAILABLE)
                || memberName.getText().equals(NO_SUCH_MEMBER_AVAILABLE);
    }

    private void clearBookCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    private void clearEntries() {
        memberNameHolder.setText("");
        memberEmailHolder.setText("");
        memberContactHolder.setText("");

        bookNameHolder.setText("");
        bookAuthorHolder.setText("");
        bookPublisherHolder.setText("");

        issueDateHolder.setText("");
        numberDaysHolder.setText("");
        fineInfoHolder.setText("");

        disableEnableControls(false);
        submissionDataContainer.setOpacity(0);
    }

    private void clearIssueEntries() {
        bookIDInput.clear();
        memberIDInput.clear();
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
        memberMobile.setText("");
        memberName.setText("");
        enableDisableGraph(true);
    }

    private void clearMemberCache() {
        memberName.setText("");
        memberMobile.setText("");
    }

    private void closeDrawer() {
        if (drawer.isOpened()) {
            drawer.close();
        }
    }

    private void disableEnableControls(Boolean enableFlag) {
        if (enableFlag) {
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        } else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }

    private void enableDisableGraph(Boolean status) {
        if (status) {
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);
        } else {
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    private void initDrawer() {
        drawer.setSidePane(toolbarView.getView());

        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MOUSE_CLICKED, ev -> drawer.toggle());
        drawer.setOnDrawerOpening(ev -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed(ev -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }

    private void initGraphs() {
        bookChart = new PieChart(bookService.bookGraphStatistics());
        memberChart = new PieChart(memberService.memberGraphStatistics());
        bookInfoContainer.getChildren().add(bookChart);
        memberInfoContainer.getChildren().add(memberChart);

        bookIssueTab.setOnSelectionChanged((Event event) -> {
            clearIssueEntries();
            if (bookIssueTab.isSelected()) {
                refreshGraphs();
            }
        });
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();
        enableDisableGraph(false);

        // default
        bookName.setText(NO_SUCH_BOOK_AVAILABLE);

        bookService.getBook(bookIDInput.getText()).ifPresent(book -> {
            bookName.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            if (!book.isAvailable()) {
                bookStatus.setText(String.format("Issued on %s", formatDate(book.getIssueTime())));
                bookStatus.getStyleClass().add("not-available");
            } else {
                bookStatus.setText(BOOK_AVAILABLE);
                bookStatus.getStyleClass().remove("not-available");
            }
        });
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadBookInfo2(ActionEvent event) {
        clearEntries();
        isReadyForSubmission = false;

        BookDTO bookDTO = bookService.getBookDetail(bookID.getText());
        if (bookDTO.getBook() != null) {
            bookNameHolder.setText(bookDTO.getBook().getTitle());
            bookAuthorHolder.setText(bookDTO.getBook().getAuthor());
            bookPublisherHolder.setText(bookDTO.getBook().getPublisher());

            if (bookDTO.getMember() != null) {
                memberNameHolder.setText(bookDTO.getMember().getName());
                memberContactHolder.setText(bookDTO.getMember().getMobile());
                memberEmailHolder.setText(bookDTO.getMember().getEmail());
                issueDateHolder.setText(formatDateTime(bookDTO.getBook().getIssueTime()));
                numberDaysHolder.setText(String.format("Used %d days", getIssuedDays(bookDTO.getBook().getIssueTime())));

                float fine = getFineAmount(bookDTO.getSetting(), bookDTO.getBook().getIssueTime());
                if (fine > 0) {
                    fineInfoHolder.setText(String.format("Fine : %.2f", fine));
                } else {
                    fineInfoHolder.setText("");
                }
            }

            isReadyForSubmission = true;
            disableEnableControls(true);
            submissionDataContainer.setOpacity(1);
        } else {
            success("No such Book Exists in Issue Database", "Okay.I'll Check");
        }
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadIssueOperation(ActionEvent event) {
        if (checkForIssueValidity()) {
            success("Invalid Input", "Okay!");
            return;
        }
        if (bookStatus.getText().equals(BOOK_NOT_AVAILABLE)) {
            JFXButton btn = new JFXButton("Okay!");
            JFXButton details = new JFXButton("View Details");
            details.addEventHandler(MOUSE_CLICKED, ev -> {
                String bookToBeLoaded = bookIDInput.getText();
                bookID.setText(bookToBeLoaded);
                bookID.fireEvent(new ActionEvent());
                mainTabPane.getSelectionModel().select(renewTab);
            });
            success("Already issued book", "This book is already issued. Cant process issue request", btn, details);
            return;
        }

        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MOUSE_CLICKED, ev -> {
            bookService.issueBook(bookIDInput.getText(), memberIDInput.getText());
            success("Book Issue Complete", "Done!");
            refreshGraphs();
            clearIssueEntries();
        });
        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MOUSE_CLICKED, ev -> {
            success("Issue Cancelled", "That's Okay");
            clearIssueEntries();
        });
        success(
                "Confirm Issue",
                "Are you sure want to issue the book " + bookName.getText() + " to " + memberName.getText() + "?",
                yesButton,
                noButton
        );
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberCache();
        enableDisableGraph(false);

        // default
        memberName.setText(NO_SUCH_MEMBER_AVAILABLE);

        memberService.findById(memberIDInput.getText()).ifPresent(member -> {
            memberName.setText(member.getName());
            memberMobile.setText(member.getMobile());
        });
    }

    @SuppressWarnings("unused")
    @FXML
    private void loadRenewOp(ActionEvent event) {
        if (!isReadyForSubmission) {
            success("Please select a book to renew", "Okay!");
            return;
        }

        JFXButton yesButton = new JFXButton("YES, Please");
        yesButton.addEventHandler(MOUSE_CLICKED, ev -> {
            bookService.renewIssue(bookID.getText());
            success("Book Has Been Renewed", "Alright!");
            disableEnableControls(false);
            submissionDataContainer.setOpacity(0);
        });
        JFXButton noButton = new JFXButton("No, Don't!");
        noButton.addEventHandler(MOUSE_CLICKED, ev -> success("Renew Operation cancelled", "Okay!"));

        success("Confirm Renew Operation", "Are you sure want to renew the book ?", yesButton, noButton);
    }

    @FXML
    private void loadSubmissionOp(@SuppressWarnings("unused") ActionEvent event) {
        if (!isReadyForSubmission) {
            JFXButton btn = new JFXButton("Okay!");
            success("Please select a book to submit", "Cant simply submit a null book :-)", btn);
            return;
        }

        JFXButton yesButton = new JFXButton("YES, Please");
        yesButton.addEventHandler(MOUSE_CLICKED, ev -> {
            bookService.returnBook(bookID.getText());
            success("Book has been submitted", "Done!");
            disableEnableControls(false);
            submissionDataContainer.setOpacity(0);
        });
        JFXButton noButton = new JFXButton("No, Cancel");
        noButton.addEventHandler(MOUSE_CLICKED, ev -> success("Submission Operation cancelled", "Okay!"));

        success("Confirm Submission Operation", "Are you sure want to return the book ?", yesButton, noButton);
    }

    private void refreshGraphs() {
        bookChart.setData(bookService.bookGraphStatistics());
        memberChart.setData(memberService.memberGraphStatistics());
    }

    private void showView(Class<? extends AbstractFxmlView> viewClass) {
        AlertMaker.addBlurEffect(rootPane);
        LibrarianApp.showView(viewClass, Modality.WINDOW_MODAL);
        AlertMaker.removeBlurEffect(rootPane);
        closeDrawer();
    }

    private void success(String headerText, String buttonText) {
        success(headerText, null, new JFXButton(buttonText));
    }

    private void success(String headerText, String bodyText, JFXButton... buttons) {
        AlertMaker.showSuccessMessage(rootPane, headerText, bodyText, buttons);
    }
}
