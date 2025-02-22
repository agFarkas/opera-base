package hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.LayoutManager;

public abstract class AbstractCustomTabPane extends JPanel {

    private boolean enabled = true;

    protected final Frame owner;

    public AbstractCustomTabPane(Frame owner, LayoutManager layout) {
        super(layout);
        this.owner = owner;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        setComponentsEnabled(enabled);

        this.enabled = enabled;
    }

    public abstract void refreshContent();

    public abstract void clearContent();

    protected abstract void setComponentsEnabled(boolean enabled);
}
