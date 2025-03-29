package hu.agfcodeworks.operangel.application.ui.util;

import hu.agfcodeworks.operangel.application.exception.ValidationException;
import lombok.experimental.UtilityClass;

import javax.swing.JOptionPane;
import java.awt.Component;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.INVALID_VALUES;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.UNEXPECTED_ERROR_ERROR_MESSAGE_PATTERN;

@UtilityClass
public class DialogUtil {

    public void showInfoMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE,
                null
        );
    }

    public void showWarningMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.WARNING_MESSAGE,
                null
        );
    }

    public void showWarningMessageByValidation(Component parent, ValidationException ex) {
        showWarningMessage(
                parent,
                INVALID_VALUES,
                ex.getMessage()
        );
    }

    public void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.ERROR_MESSAGE,
                null
        );
    }

    public void showErrorMessageByException(Component parent, String title, Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                parent,
                UNEXPECTED_ERROR_ERROR_MESSAGE_PATTERN.formatted(ex),
                title,
                JOptionPane.ERROR_MESSAGE,
                null
        );
    }

    public int showCustomQuestionDialog(Component parent, String title, String message, Object[] options, int defaultOptionIndex) {
        return JOptionPane.showOptionDialog(parent, message, title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[defaultOptionIndex]);
    }
}
