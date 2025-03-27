package hu.agfcodeworks.operangel.application.ui.util;

import lombok.experimental.UtilityClass;

import javax.swing.JOptionPane;
import java.awt.Component;

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

    public void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
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
