package djf.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 * @author McKillaGorilla
 * @author Ritwik Banerjee
 */
public class AppDialogs {
    // DIALOG METHODS
    public static void showMessageDialog(Stage parent, Object titleProperty, Object contentTextProperty) {
        PropertiesManager props         = PropertiesManager.getPropertiesManager();
        String            title         = props.getProperty(titleProperty);
        String            contentText   = props.getProperty(contentTextProperty);
        Alert             messageDialog = new Alert(Alert.AlertType.INFORMATION);
        messageDialog.initOwner(parent);
        messageDialog.initModality(Modality.APPLICATION_MODAL);
        messageDialog.setTitle(title);
        messageDialog.setHeaderText("");
        Label label = new Label(contentText);
        label.setWrapText(true);
        messageDialog.getDialogPane().setContent(label);
        messageDialog.showAndWait();
    }

    public static ButtonType showYesNoCancelDialog(Stage parent, Object titleProperty, Object contentTextProperty) {
        PropertiesManager props              = PropertiesManager.getPropertiesManager();
        String            title              = props.getProperty(titleProperty);
        String            contentText        = props.getProperty(contentTextProperty);
        Alert             confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.initOwner(parent);
        confirmationDialog.initModality(Modality.APPLICATION_MODAL);
        confirmationDialog.getButtonTypes().clear();
        confirmationDialog.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirmationDialog.setTitle(title);
        confirmationDialog.setContentText(contentText);
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.orElse(null);
    }

    public static void showStackTraceDialog(Stage parent, Exception exception,
                                            Object appErrorTitleProperty,
                                            Object appErrorContentProperty) {
        // FIRST MAKE THE DIALOG
        Alert stackTraceDialog = new Alert(Alert.AlertType.ERROR);
        stackTraceDialog.initOwner(parent);
        stackTraceDialog.initModality(Modality.APPLICATION_MODAL);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        stackTraceDialog.setTitle(props.getProperty(appErrorTitleProperty));
        stackTraceDialog.setContentText(props.getProperty(appErrorContentProperty));

        // GET THE TEXT FOR THE STACK TRACE
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        // AND PUT THE STACK TRACE IN A TEXT ARA
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        stackTraceDialog.getDialogPane().setExpandableContent(textArea);

        // OPEN THE DIALOG
        stackTraceDialog.showAndWait();
    }

    public static String showTextInputDialog(Stage parent, Object titleProperty, Object contentProperty) {
        PropertiesManager props       = PropertiesManager.getPropertiesManager();
        String            title       = props.getProperty(titleProperty);
        String            contentText = props.getProperty(contentProperty);
        TextInputDialog   textDialog  = new TextInputDialog();
        textDialog.initOwner(parent);
        textDialog.initModality(Modality.APPLICATION_MODAL);
        textDialog.setTitle(title);
        textDialog.setContentText(contentText);
        Optional<String> result = textDialog.showAndWait();
        return result.orElse("");
    }
}
