package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public abstract class JLabeledComponent<C extends JComponent> extends JPanel {

    private final JLabel label;

    protected final C component;

    public JLabeledComponent(@NonNull String labelText, @NonNull C component) {
        this.label = new JLabel(labelText);
        this.component = component;

        buildPanel();
    }

    private void buildPanel() {
        setLayout(new FlowLayout());

        add(label);
        add(component);

        label.setLabelFor(component);

        setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    public String getLabelText() {
        return label.getText();
    }

    public void setLabelText(String labelText) {
        label.setText(labelText);
    }
}
