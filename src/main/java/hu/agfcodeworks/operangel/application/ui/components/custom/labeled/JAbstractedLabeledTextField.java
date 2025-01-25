package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public abstract class JAbstractedLabeledTextField<C extends JTextField> extends JLabeledComponent<C> {

    private static final Color validBackground = Color.WHITE;

    private static final Color invalidBackground = Color.RED;

    private static final Color validForeground = Color.BLACK;

    private static final Color invalidForeground = Color.WHITE;

    private final DocumentListener validationDocumentListener;

    private boolean mandatory = false;

    private boolean continuouslyValidated;

    public JAbstractedLabeledTextField(@NonNull String labelText, @NonNull C component) {
        super(labelText, component);

        validationDocumentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                validateContent();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                validateContent();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                validateContent();
            }
        };

        setContinuouslyValidated(true);
    }

    public void setEditable(boolean editable) {
        component.setEditable(editable);
    }

    public String getText() {
        return component.getText();
    }

    public void setText(String text) {
        component.setText(StringUtils.hasText(text) ? text : EMPTY_TEXT);
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isContentValid() {
        var text = component.getText();

        return (!mandatory || StringUtils.hasText(text));
    }

    public boolean isContinuouslyValidated() {
        return continuouslyValidated;
    }

    private void setContinuouslyValidated(boolean continuouslyValidated) {
        this.continuouslyValidated = continuouslyValidated;

        if (continuouslyValidated) {
            component.getDocument()
                    .addDocumentListener(validationDocumentListener);
        } else {
            component.getDocument()
                    .removeDocumentListener(validationDocumentListener);
        }
    }

    private void validateContent() {
        if (isContentValid()) {
            component.setBackground(validBackground);
            component.setForeground(validForeground);
        } else {
            component.setBackground(invalidBackground);
            component.setForeground(invalidForeground);
        }
    }
}
