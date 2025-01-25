package hu.agfcodeworks.operangel.application.ui.components.custom;

import hu.agfcodeworks.operangel.application.settings.DbSettings;
import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.DbConnectionStatus;
import hu.agfcodeworks.operangel.application.ui.util.UiUtil;
import hu.agfcodeworks.operangel.application.util.DbUtil;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

public class StatusBar extends JPanel {

    private static final Map<DbConnectionStatus, Icon> statusIcons = new HashMap<>();

    static {
        for (var status : DbConnectionStatus.values()) {
            var icon = UiUtil.loadDbConnectionIconFromPresource(status);
            statusIcons.put(status, icon);
        }
    }

    private DbSettings dbSettings;

    private DbConnectionStatus dbConnectionStatus;

    private final JLabel lbDbUrl = new JLabel(" ");


    public StatusBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(lbDbUrl);
    }

    public void setDbSettings(DbSettings dbSettings) {
        this.dbSettings = dbSettings;

        var labelText = DbUtil.composeDbUrl(this.dbSettings);
        lbDbUrl.setText(labelText);
    }

    public void setDbConnectionStatus(DbConnectionStatus dbConnectionStatus) {
        this.dbConnectionStatus = dbConnectionStatus;

        lbDbUrl.setIcon(statusIcons.get(dbConnectionStatus));
        lbDbUrl.setToolTipText(dbConnectionStatus.getText());
    }
}
