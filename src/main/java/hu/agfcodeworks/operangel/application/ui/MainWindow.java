package hu.agfcodeworks.operangel.application.ui;

import hu.agfcodeworks.operangel.application.event.ContextEvent;
import hu.agfcodeworks.operangel.application.event.listener.ContextEventListener;
import hu.agfcodeworks.operangel.application.service.DbSettingsService;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import hu.agfcodeworks.operangel.application.ui.components.custom.StatusBar;
import hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes.CalendarTabPane;
import hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes.ConductorsTabPane;
import hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes.OperasTabPane;
import hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes.PerformersTabPane;
import hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes.SeasonsTabPane;
import hu.agfcodeworks.operangel.application.ui.design.TabbedPaneUi;
import hu.agfcodeworks.operangel.application.ui.dialog.DbSettingsDialog;
import hu.agfcodeworks.operangel.application.ui.util.UiUtil;
import hu.agfcodeworks.operangel.application.util.ContextUtil;
import hu.agfcodeworks.operangel.application.util.FileUtil;
import hu.agfcodeworks.operangel.application.util.OutContextUtil;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static hu.agfcodeworks.operangel.application.constants.FilePaths.FILENAME_DB_SETTINGS;
import static hu.agfcodeworks.operangel.application.ui.uidto.DbConnectionStatus.AWAITING;
import static hu.agfcodeworks.operangel.application.ui.uidto.DbConnectionStatus.CLOSED;
import static hu.agfcodeworks.operangel.application.ui.uidto.DbConnectionStatus.ESTABLISHED;
import static hu.agfcodeworks.operangel.application.ui.uidto.DbConnectionStatus.REFUSED;
import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;

public class MainWindow extends JFrame {

    private static final String TITLE_PATTERN = "%s (%s)";

    private final DbSettingsService dbSettingsService = OutContextUtil.getComponent(DbSettingsService.class);

    private final String applicationName = "Operangel";

    private final String applicationVersion = "1.0.0 - BETA";

    private final StatusBar statusBar = new StatusBar();

    private final ContextEventListener contextEventListener = this::statusChanged;

    public MainWindow() {
        setSize(1600, 900);
        setTitle(TITLE_PATTERN.formatted(applicationName, applicationVersion));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var contentPane = getContentPane();

        contentPane.add(makeMainTabbedPane());
        setJMenuBar(makeMenuBar());

        contentPane.add(statusBar, BorderLayout.PAGE_END);

        addWindowListener(makeWindowListener());

        setVisible(true);
    }

    private JMenuBar makeMenuBar() {
        var menuBar = new JMenuBar();

        var mFile = UiUtil.makeMenu("Fájl");
        var mSettings = UiUtil.makeMenu("Beállítások");
        var mHelp = UiUtil.makeMenu("Súgó");

        menuBar.add(mFile);
        menuBar.add(mSettings);
        menuBar.add(mHelp);

        mFile.add(UiUtil.makeMenuItem("Importálás", event -> this.importFile(), true));
        mFile.add(UiUtil.makeMenuItem("Exportálás", event -> this.exportFile(), true));
        mFile.add(UiUtil.makeMenuItem("Kilépés", event -> this.closeWindow(), false));

        mSettings.add(UiUtil.makeMenuItem("Adatbázis", event -> this.changeDatabaseConnection(), true));

        mHelp.add(UiUtil.makeMenuItem("Névjegy", event -> showAbout(), true));

        return menuBar;
    }

    private JTabbedPane makeMainTabbedPane() {
        var tabbedPane = new JTabbedPane();

        tabbedPane.setUI(new TabbedPaneUi());

        tabbedPane.addTab("Naptár", new CalendarTabPane());
        tabbedPane.addTab("Karmesterek", new ConductorsTabPane());
        tabbedPane.addTab("Előadók", new PerformersTabPane());
        tabbedPane.addTab("Operák", new OperasTabPane());
        tabbedPane.addTab("Évadok", new SeasonsTabPane());

        tabbedPane.setEnabledAt(1, false);

        return tabbedPane;
    }

    private void importFile() {

    }

    private void exportFile() {

    }

    private void closeWindow() {
        ContextUtil.stopContext();
        this.dispose();
    }

    private void changeDatabaseConnection() {
        var dbSettings = dbSettingsService.obtainDbSettings();
        var dialog = new DbSettingsDialog(this, dbSettings);

        if (dialog.getDialogStatus() == OK) {
            dbSettingsService.saveDbSettings(dialog.getValue());
            startContext(dbSettings);
        }
    }

    private void showAbout() {

    }

    private WindowListener makeWindowListener() {
        return new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent event) {
                statusBar.setDbConnectionStatus(CLOSED);

                if (FileUtil.fileExists(FILENAME_DB_SETTINGS)) {
                    var dbSettings = dbSettingsService.obtainDbSettings();
                    startContext(dbSettings);
                } else {
                    changeDatabaseConnection();
                }
            }

            @Override
            public void windowClosing(WindowEvent event) {
                super.windowClosing(event);
            }
        };
    }

    private void startContext(DbSettings dbSettings) {
        statusBar.setDbSettings(dbSettings);
        ContextUtil.startContext(contextEventListener);
    }

    private void statusChanged(ContextEvent event) {
        switch (event.getStatus()) {
            case CLOSED -> statusBar.setDbConnectionStatus(CLOSED);
            case AWAITING -> statusBar.setDbConnectionStatus(AWAITING);
            case ESTABLISHED -> statusBar.setDbConnectionStatus(ESTABLISHED);
            case REFUSED -> statusBar.setDbConnectionStatus(REFUSED);
        }
    }
}
