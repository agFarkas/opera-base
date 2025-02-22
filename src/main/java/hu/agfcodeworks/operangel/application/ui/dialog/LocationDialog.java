package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ErrorDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LocationDialog extends JAbstractDialog<LocationDto> {

    private JLabeledTextField name;

    public LocationDialog(Frame owner, String title, LocationDto initialValue) {
        super(owner, title, initialValue);
        setVisible(true);
    }

    public LocationDialog(Frame owner, String title) {
        super(owner, title, null);
        setVisible(true);
    }

    @Override
    protected List<ErrorDto> validateCustomFields() {
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
    protected void initiateValue(LocationDto initialValue) {
        if (Objects.nonNull(initialValue)) {
            name.setText(initialValue.getName());
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
}
