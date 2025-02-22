package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

public abstract class JLabeledComponent<C extends JComponent> extends JPanel {

    protected final C component;
    protected final JLabel label;
    private boolean mandatory;

    public JLabeledComponent(@NonNull String labelText, @NonNull C component) {
        this.label = new JLabel(labelText);
        this.component = component;

        setMandatory(false);

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

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        label.setFont(label.getFont().deriveFont(makeStyle(mandatory)));

        this.mandatory = mandatory;
    }

    private int makeStyle(boolean mandatory) {
        return mandatory ? Font.BOLD : Font.PLAIN;
    }

    @Override
    public boolean isEnabled() {
        return component.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }
}
