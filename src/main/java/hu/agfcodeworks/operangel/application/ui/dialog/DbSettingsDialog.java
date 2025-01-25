package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.settings.DbEngine;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComboBox;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledPasswordField;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DbSettingsDialog extends JAbstractDialog<DbSettings> {

    private static final int TEXT_FIELD_COLUMNS = 15;

    private JLabeledComboBox<DbEngine> cbDbEngine;

    private JLabeledTextField tfHost;

    private JLabeledTextField tfPort;

    private JLabeledTextField tfName;

    private JLabeledTextField tfUsername;

    private JLabeledPasswordField pfPassword;

    public DbSettingsDialog(Frame owner, DbSettings value) {
        super(owner,"Adatbázis-beállítások", value);

        cbDbEngine.setTextProvider(DbEngine::getName);
        setSize(400, 300);
        setVisible(true);
    }

    private Collection<DbEngine> collectValues() {
        return Stream.of(DbEngine.values())
                .collect(Collectors.toList());
    }

    @Override
    protected void buildFormPane(JPanel formPane) {
        cbDbEngine = new JLabeledComboBox<>("Adatbázis-szerver");
        tfHost = new JLabeledTextField("Hoszt", TEXT_FIELD_COLUMNS);
        tfPort = new JLabeledTextField("Port", TEXT_FIELD_COLUMNS);
        tfName = new JLabeledTextField("Adatbázisnév", TEXT_FIELD_COLUMNS);
        tfUsername = new JLabeledTextField("Felhasználónév", TEXT_FIELD_COLUMNS);
        pfPassword = new JLabeledPasswordField("Jelszó", TEXT_FIELD_COLUMNS);

        formPane.add(cbDbEngine);
        formPane.add(tfHost);
        formPane.add(tfPort);
        formPane.add(tfUsername);
        formPane.add(pfPassword);
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
                .withPort(Integer.parseInt(tfPort.getText()))
                .withName(tfName.getText())
                .withUsername(tfUsername.getText())
                .withPassword(pfPassword.getText())
                .build();
    }
}
