package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.DialogStatus;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static hu.agfcodeworks.operangel.application.ui.components.custom.uidto.DialogStatus.CANCEL;
import static hu.agfcodeworks.operangel.application.ui.components.custom.uidto.DialogStatus.OK;

public abstract class JAbstractDialog<V> extends JDialog {

    private static final int HORIZONTAL_DISTANCE_FROM_OWNER = 30;

    private static final int VERTICAL_DISTANCE_FROM_OWNER = 15;

    private JPanel contentPane;

    private JButton buttonOK;

    private JButton buttonCancel;

    private JPanel formPane;
    private JPanel buttonPane;

    private V value;

    private DialogStatus dialogStatus = CANCEL;

    public JAbstractDialog(Frame owner, String title, V initialValue) {
        super(owner);
        setLocation(new Point(owner.getX() + HORIZONTAL_DISTANCE_FROM_OWNER, owner.getY() + VERTICAL_DISTANCE_FROM_OWNER));

        this.value = initialValue;

        setTitle(title);
        setContentPane(contentPane);

        formPane.setLayout(new BoxLayout(formPane, BoxLayout.PAGE_AXIS));

        buildFormPane(formPane);
        initiateValue(initialValue);

        setModal(true);
        getRootPane()
                .setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        setResizable(false);
        pack();
    }

    private void onOK() {
        this.value = composeValue();
        this.dialogStatus = OK;

        dispose();
    }

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
