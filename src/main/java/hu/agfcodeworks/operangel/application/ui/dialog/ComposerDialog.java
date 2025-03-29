package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;
import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.composerTextProvider;

public class ComposerDialog extends JAbstractDialog<ComposerDto> {

    private static final String TITLE_CREATE_COMPOSER_DIALOG = "Új szerző";

    private JLabeledTextField tfGivenName;
    private JLabeledTextField tfFamilyName;

    public ComposerDialog(Frame owner, ComposerDto initialValue) {
        super(owner, composerTextProvider, initialValue);
        setVisible(true);
    }

    public ComposerDialog(Frame owner) {
        super(owner);
        setVisible(true);
    }

    @Override
    protected List<DialogValidationErrorDto> validateCustomFields() {
        var errorDtos = new LinkedList<DialogValidationErrorDto>();

        errorDtos.addAll(getErrorDtos(tfGivenName));
        errorDtos.addAll(getErrorDtos(tfFamilyName));

        return errorDtos;
    }

    @Override
    protected void buildFormPane(JPanel formPane) {
        this.tfGivenName = new JLabeledTextField("Utónév", 40);
        this.tfFamilyName = new JLabeledTextField("Családnév", 40);

        tfGivenName.setMandatory(true);
        tfFamilyName.setMandatory(true);

        formPane.add(tfGivenName);
        formPane.add(tfFamilyName);
    }

    @Override
    protected void initiateValue() {
        if (Objects.nonNull(value)) {
            tfGivenName.setText(value.getGivenName());
            tfFamilyName.setText(value.getFamilyName());
        }
    }

    @Override
    protected ComposerDto composeValue() {
        if (Objects.nonNull(value)) {
            return ComposerDto.builder()
                    .withNaturalId(value.getNaturalId())
                    .withGivenName(tfGivenName.getText())
                    .withFamilyName(tfFamilyName.getText())
                    .build();
        }

        return ComposerDto.builder()
                .withGivenName(tfGivenName.getText())
                .withFamilyName(tfFamilyName.getText())
                .build();
    }

    @Override
    protected String obtainTitle() {
        return TITLE_CREATE_COMPOSER_DIALOG;
    }
}
