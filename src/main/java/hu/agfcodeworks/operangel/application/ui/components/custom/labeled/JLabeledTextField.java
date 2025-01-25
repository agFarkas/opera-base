package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;

import javax.swing.JTextField;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class JLabeledTextField extends JAbstractedLabeledTextField<JTextField> {

    public JLabeledTextField(@NonNull String labelText, int columns) {
        super(labelText, new JTextField(EMPTY_TEXT, columns));
    }
}
