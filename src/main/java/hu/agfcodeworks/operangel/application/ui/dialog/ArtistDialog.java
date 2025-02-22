package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ErrorDto;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ArtistDialog extends JAbstractDialog<ArtistListDto> {

    private JLabeledTextField tfGivenName;
    private JLabeledTextField tfFamilyName;

    public ArtistDialog(Frame owner, String title, ArtistListDto initialValue) {
        super(owner, title, initialValue);
        setVisible(true);
    }

    public ArtistDialog(Frame owner, String title) {
        super(owner, title, null);
        setVisible(true);
    }

    @Override
    protected List<ErrorDto> validateCustomFields() {
        var errorDtos = new LinkedList<ErrorDto>();

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
    protected void initiateValue(ArtistListDto initialValue) {
        if (Objects.nonNull(initialValue)) {
            tfGivenName.setText(initialValue.getGivenName());
            tfFamilyName.setText(initialValue.getFamilyName());
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
