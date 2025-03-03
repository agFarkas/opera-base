package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ErrorDto;
import hu.agfcodeworks.operangel.application.exception.ValidationException;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JAbstractedLabeledTextField;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComboBox;
import hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus;
import org.springframework.util.CollectionUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.CANCEL;
import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;
import static hu.agfcodeworks.operangel.application.ui.uidto.ValidationStatus.INVALID_FOR_CONTENT_RULE;
import static hu.agfcodeworks.operangel.application.ui.uidto.ValidationStatus.INVALID_FOR_MANDATORY;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.INVALID_VALUES;

public abstract class JAbstractDialog<V> extends JDialog {

    protected static final String NO_WHITESPACE_REGEX = "[^-\\s]{1,}";

    protected static final String VALIDATION_MESSAGE_NO_WHITESPACE = "Nem tartalmazhat szóközt.";

    protected static final String VALIDATION_MESSAGE_NUMBERS_ONLY = "Csak számot tartalmazhat.";

    protected static final String VALIDATION_MESSAGE_MANDATORY = "Kötelező";

    private static final int HORIZONTAL_DISTANCE_FROM_OWNER = 30;

    private static final int VERTICAL_DISTANCE_FROM_OWNER = 15;

    private Container contentPane;

    private JButton buttonOK = new JButton("OK");

    private JButton buttonCancel = new JButton("Mégsem");

    private JPanel formPane = new JPanel();

    private JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    protected V value;

    private DialogStatus dialogStatus = CANCEL;

    public JAbstractDialog(Frame owner, String title, V initialValue) {
        super(owner);
        setLocation(new Point(owner.getX() + HORIZONTAL_DISTANCE_FROM_OWNER, owner.getY() + VERTICAL_DISTANCE_FROM_OWNER));

        this.value = initialValue;

        setTitle(title);
        this.contentPane = getContentPane();

        formPane.setLayout(new BoxLayout(formPane, BoxLayout.PAGE_AXIS));

        buttonPane.add(buttonOK);
        buttonPane.add(buttonCancel);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(formPane);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        buildFormPane(formPane);
        initiateValue(initialValue);

        setModal(true);
        getRootPane()
                .setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        setResizable(false);
        pack();
    }

    protected static List<ErrorDto> getErrorDtos(JLabeledComboBox<?> comboBox) {
        var validationStatuses = comboBox.getValidationStatus();

        if (!CollectionUtils.isEmpty(validationStatuses)) {
            return List.of(new ErrorDto(comboBox.getLabelText(), VALIDATION_MESSAGE_MANDATORY));
        }

        return Collections.emptyList();
    }

    protected static List<ErrorDto> getErrorDtos(JAbstractedLabeledTextField<?> textField) {
        var validationStatuses = textField.getValidationStatus();
        var errorDtos = new LinkedList<ErrorDto>();

        if (validationStatuses.contains(INVALID_FOR_MANDATORY)) {
            errorDtos.add(new ErrorDto(textField.getLabelText(), VALIDATION_MESSAGE_MANDATORY));
        }

        if (validationStatuses.contains(INVALID_FOR_CONTENT_RULE)) {
            errorDtos.add(new ErrorDto(textField.getLabelText(), textField.getValidationMessage()));
        }

        return errorDtos;
    }

    private void onOK() {
        try {
            validateContent();

            this.value = composeValue();
            this.dialogStatus = OK;
            dispose();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), INVALID_VALUES, JOptionPane.WARNING_MESSAGE);
        }
    }

    protected void validateContent() {
        var errorDtos = validateCustomFields();

        if (!CollectionUtils.isEmpty(errorDtos)) {
            throw new ValidationException(errorDtos);
        }
    }

    protected abstract List<ErrorDto> validateCustomFields();

    private void onCancel() {
        dispose();
    }

    protected abstract void buildFormPane(JPanel formPane);

    protected abstract void initiateValue(V initialValue);

    protected abstract V composeValue();

    public V getValue() {
        return value;
    }

    public DialogStatus getDialogStatus() {
        return dialogStatus;
    }
}
