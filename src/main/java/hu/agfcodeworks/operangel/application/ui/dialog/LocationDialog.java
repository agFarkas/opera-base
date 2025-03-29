package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;
import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.locationTextProvider;

public class LocationDialog extends JAbstractDialog<LocationDto> {

    private static final String TITLE_CREATE_LOCATION_DIALOG = "Új helyszín";

    private JLabeledTextField name;

    public LocationDialog(Frame owner, LocationDto initialValue) {
        super(owner, locationTextProvider, initialValue);
        setVisible(true);
    }

    public LocationDialog(Frame owner) {
        super(owner);
        setVisible(true);
    }

    @Override
    protected List<DialogValidationErrorDto> validateCustomFields() {
        return new LinkedList<>(getErrorDtos(name));
    }

    @Override
    protected void buildFormPane(JPanel formPane) {
        formPane.setLayout(new FlowLayout());

        this.name = new JLabeledTextField("Név", 40);
        name.setMandatory(true);

        formPane.add(name);
    }

    @Override
    protected void initiateValue() {
        if (Objects.nonNull(value)) {
            name.setText(value.getName());
        }
    }

    @Override
    protected LocationDto composeValue() {
        if (Objects.nonNull(value)) {
            return LocationDto.builder()
                    .withNaturalId(value.getNaturalId())
                    .withName(name.getText())
                    .build();
        }

        return LocationDto.builder()
                .withName(name.getText())
                .build();
    }

    @Override
    protected String obtainTitle() {
        return TITLE_CREATE_LOCATION_DIALOG;
    }
}
