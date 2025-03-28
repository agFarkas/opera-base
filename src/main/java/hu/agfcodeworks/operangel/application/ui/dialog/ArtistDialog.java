package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;
import hu.agfcodeworks.operangel.application.ui.dialog.enums.ArtistPosition;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.artistTextProvider;

public class ArtistDialog extends JAbstractDialog<ArtistListDto> {

    private static final String TITLE_CREATE_SINGER_DIALOG = "Új énekes";

    private static final String TITLE_CREATE_CONDUCTOR_DIALOG = "Új karmester";

    private ArtistPosition artistPosition;

    private JLabeledTextField tfGivenName;
    private JLabeledTextField tfFamilyName;

    public ArtistDialog(Frame owner, ArtistListDto initialValue) {
        super(owner, artistTextProvider, initialValue);
        setVisible(true);
    }

    public ArtistDialog(Frame owner, ArtistPosition artistPosition) {
        super(owner);
        this.artistPosition = artistPosition;

        setVisible(true);
    }

    @Override
    protected String obtainTitle() {
        return switch (artistPosition) {
            case CONDUCTOR -> TITLE_CREATE_CONDUCTOR_DIALOG;
            case SINGER -> TITLE_CREATE_SINGER_DIALOG;
        };
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
    protected ArtistListDto composeValue() {
        if (Objects.nonNull(value)) {
            return ArtistListDto.builder()
                    .withNaturalId(value.getNaturalId())
                    .withGivenName(tfGivenName.getText())
                    .withFamilyName(tfFamilyName.getText())
                    .build();
        }

        return ArtistListDto.builder()
                .withGivenName(tfGivenName.getText())
                .withFamilyName(tfFamilyName.getText())
                .build();
    }
}
