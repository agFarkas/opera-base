package hu.agfcodeworks.operangel.application.ui;

import hu.agfcodeworks.operangel.application.ui.components.tabpanes.CalendarTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.ConductorsTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.OperasTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.PerformersTabPane;
import hu.agfcodeworks.operangel.application.ui.components.tabpanes.SeasonsTabPane;
import hu.agfcodeworks.operangel.application.ui.util.UiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

@Component
public class MainWindow extends JFrame {

    private static final String TITLE_PATTERN = "%s (%s)";

    @Autowired
    private CalendarTabPane pnCalendar;

    @Autowired
    private ConductorsTabPane pnConductors;

    @Autowired
    private PerformersTabPane pnPerformers;

    @Autowired
    private OperasTabPane pnOperas;

    @Autowired
    private SeasonsTabPane pnSeasons;

    @Value("${application.title}")
    private String applicationName;

    @Value("${application.version}")
    private String applicationVersion;

    @PostConstruct
    public void postConstruct() {
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
        menuBar.add(mFile);

        mFile.add(UiUtil.makeMenuItem("Importálás", event -> this.importFile(), true));
        mFile.add(UiUtil.makeMenuItem("Exportálás", event -> this.exportFile(), true));
        mFile.add(UiUtil.makeMenuItem("Kilépés", event -> this.closeWindow(), false));

        return menuBar;
    }

    private JTabbedPane makeMainTabbedPane() {
        var tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Naptár", pnCalendar);
        tabbedPane.addTab("Karmesterek", pnConductors);
        tabbedPane.addTab("Előadók", pnPerformers);
        tabbedPane.addTab("Operák", pnOperas);
        tabbedPane.addTab("Évadok", pnSeasons);

        return tabbedPane;
    }

    private void importFile() {

    }

    private void exportFile() {

    }

    private void closeWindow() {
        this.dispose();
    }
}
