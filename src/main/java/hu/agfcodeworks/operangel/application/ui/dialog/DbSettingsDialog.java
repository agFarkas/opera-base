package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ErrorDto;
import hu.agfcodeworks.operangel.application.settings.DbEngine;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComboBox;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComponent;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledNumberField;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledPasswordField;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DbSettingsDialog extends JAbstractDialog<DbSettings> {

    private static final int TEXT_FIELD_COLUMNS = 15;

    private JLabeledComboBox<DbEngine> cbDbEngine;

    private JLabeledTextField tfHost;

    private JLabeledNumberField tfPort;

    private JLabeledTextField tfName;

    private JLabeledTextField tfUsername;

    private JLabeledPasswordField pfPassword;

    public DbSettingsDialog(Frame owner, DbSettings value) {
        super(owner, "Adatbázis-beállítások", value);

        cbDbEngine.setTextProvider(DbEngine::getName);
        setSize(400, 300);
        setVisible(true);
    }

    private Collection<DbEngine> collectValues() {
        return Stream.of(DbEngine.values())
                .collect(Collectors.toList());
    }

    @Override
    protected List<ErrorDto> validateCustomFields() {
        var errorDtos = new LinkedList<ErrorDto>();

        errorDtos.addAll(getErrorDtos(cbDbEngine));
        errorDtos.addAll(getErrorDtos(tfHost));
        errorDtos.addAll(getErrorDtos(tfPort));
        errorDtos.addAll(getErrorDtos(tfName));
        errorDtos.addAll(getErrorDtos(tfUsername));
        errorDtos.addAll(getErrorDtos(pfPassword));

        return errorDtos;
    }

    @Override
    protected void buildFormPane(JPanel formPane) {
        cbDbEngine = new JLabeledComboBox<>("Adatbázis-szerver");
        tfHost = new JLabeledTextField("Cím", TEXT_FIELD_COLUMNS);
        tfPort = new JLabeledNumberField("Port", TEXT_FIELD_COLUMNS);
        tfName = new JLabeledTextField("Adatbázis neve", TEXT_FIELD_COLUMNS);
        tfUsername = new JLabeledTextField("Felhasználónév", TEXT_FIELD_COLUMNS);
        pfPassword = new JLabeledPasswordField("Jelszó", TEXT_FIELD_COLUMNS);

        setAllMandatory(
                cbDbEngine,
                tfHost, tfPort,
                tfName,
                tfUsername, pfPassword
        );

        tfHost.setRegex(NO_WHITESPACE_REGEX);
        tfHost.setValidationMessage(VALIDATION_MESSAGE_NO_WHITESPACE);
        tfPort.setValidationMessage(VALIDATION_MESSAGE_NUMBERS_ONLY);
        tfName.setRegex(NO_WHITESPACE_REGEX);
        tfName.setValidationMessage(VALIDATION_MESSAGE_NO_WHITESPACE);
        tfUsername.setRegex(NO_WHITESPACE_REGEX);
        tfUsername.setValidationMessage(VALIDATION_MESSAGE_NO_WHITESPACE);

        formPane.add(cbDbEngine);
        formPane.add(tfHost);
        formPane.add(tfPort);
        formPane.add(tfName);
        formPane.add(tfUsername);
        formPane.add(pfPassword);
    }

    private void setAllMandatory(JLabeledComponent<?>... jLabeledComponents) {
        for (var component : jLabeledComponents) {
            component.setMandatory(true);
        }
    }

    @Override
    protected void initiateValue(DbSettings initialValue) {
        cbDbEngine.addItems(collectValues());

        cbDbEngine.setSelectedItem(initialValue.getDbEngine());
        tfHost.setText(initialValue.getHost());
        tfPort.setText(Integer.toString(initialValue.getPort()));
        tfName.setText(initialValue.getName());
        tfUsername.setText(initialValue.getUsername());
        pfPassword.setText(initialValue.getPassword());
    }

    @Override
    protected DbSettings composeValue() {
        return DbSettings.builder()
                .withDbEngine(cbDbEngine.getSelectedItem())
                .withHost(tfHost.getText())
                .withPort(tfPort.getNumber())
                .withName(tfName.getText())
                .withUsername(tfUsername.getText())
                .withPassword(pfPassword.getText())
                .build();
    }
}
