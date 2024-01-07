package hu.agfcodeworks.operangel.application.ui;

import hu.agfcodeworks.operangel.application.ui.components.tabpanes.CalendarTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.ConductorsTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.OperasTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.PerformersTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.SeasonsTabPane;
import hu.agfcodeworks.operangel.application.ui.util.ContextUtil;
import hu.agfcodeworks.operangel.application.ui.util.UiUtil;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

    private static final String TITLE_PATTERN = "%s (%s)";

    private final String applicationName = "Operangel";

    private final String applicationVersion = "1.0.0 - BETA";

    public MainWindow() {
        setSize(1600, 900);
        setTitle(TITLE_PATTERN.formatted(applicationName, applicationVersion));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(makeMainTabbedPane());
        setJMenuBar(makeMenuBar());

        setVisible(true);
    }

    private JMenuBar makeMenuBar() {
        var menuBar = new JMenuBar();

        var mFile = new JMenu("Fájl");
        var mSettings = new JMenu("Beállítások");
        var mHelp = new JMenu("Súgó");

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

        tabbedPane.addTab("Naptár", new CalendarTabPane());
        tabbedPane.addTab("Karmesterek", new ConductorsTabPane());
        tabbedPane.addTab("Előadók", new PerformersTabPane());
        tabbedPane.addTab("Operák", new OperasTabPane());
        tabbedPane.addTab("Évadok", new SeasonsTabPane());

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

    }

    private void showAbout() {

    }
}
