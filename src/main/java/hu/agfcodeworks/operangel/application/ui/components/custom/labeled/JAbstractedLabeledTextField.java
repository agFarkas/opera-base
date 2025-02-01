package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import hu.agfcodeworks.operangel.application.ui.components.custom.Validated;
import hu.agfcodeworks.operangel.application.ui.uidto.ValidationStatus;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public abstract class JAbstractedLabeledTextField<C extends JTextField> extends JLabeledComponent<C> implements Validated {

    private static final Color validBackground = Color.WHITE;

    private static final Color invalidBackground = Color.RED;

    private static final Color validForeground = Color.BLACK;

    private static final Color invalidForeground = Color.WHITE;

    private final DocumentListener validationDocumentListener;


    private String regex = EMPTY_TEXT;

    private boolean continuouslyValidated;

    private String validationMessage = EMPTY_TEXT;

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

    public Set<ValidationStatus> getValidationStatus() {
        var validationStatuses = new HashSet<ValidationStatus>();

        if (!isValidForMandatory()) {
            validationStatuses.add(ValidationStatus.INVALID_FOR_MANDATORY);
        }

        if (!isValidByPattern()) {
            validationStatuses.add(ValidationStatus.INVALID_FOR_CONTENT_RULE);
        }

        return validationStatuses;
    }

    private boolean isValidForMandatory() {
        return !isMandatory() || StringUtils.hasText(component.getText());
    }

    private boolean isValidByPattern() {
        var text = component.getText();
        return !StringUtils.hasText(regex) || (StringUtils.hasText(text) && text.matches(regex));
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
        if (isValidForMandatory() && isValidByPattern()) {
            component.setBackground(validBackground);
            component.setForeground(validForeground);
        } else {
            component.setBackground(invalidBackground);
            component.setForeground(invalidForeground);
        }
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
