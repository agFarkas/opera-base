package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.service.cache.global.ComposerCache;
import hu.agfcodeworks.operangel.application.service.commmand.service.ComposerCommandService;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComboBox;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledTextField;
import hu.agfcodeworks.operangel.application.ui.text.Comparators;
import hu.agfcodeworks.operangel.application.ui.text.TextProviders;
import hu.agfcodeworks.operangel.application.util.ContextUtil;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.composerPlayTextProvider;
import static hu.agfcodeworks.operangel.application.ui.dto.DialogStatus.OK;

public class PlayDialog extends JAbstractDialog<PlayListDto> {

    private static final String TITLE_CREATE_OPERA_DIALOG = "Új opera";

    private JLabeledComboBox<ComposerDto> cbComposer;

    private JLabeledTextField tfTitle;

    public PlayDialog(Frame owner, PlayListDto initialValue) {
        super(owner, composerPlayTextProvider, initialValue);
        setVisible(true);
    }

    public PlayDialog(Frame owner) {
        super(owner);

        cbComposer.setSelectedIndex(-1);

        setVisible(true);
    }

    @Override
    protected String obtainTitle() {
        return TITLE_CREATE_OPERA_DIALOG;
    }

    @Override
    protected List<DialogValidationErrorDto> validateCustomFields() {
        var errorDtos = new LinkedList<DialogValidationErrorDto>();

        errorDtos.addAll(getErrorDtos(cbComposer));
        errorDtos.addAll(getErrorDtos(tfTitle));

        return errorDtos;
    }

    @Override
    protected void buildFormPane(JPanel formPane) {
        this.cbComposer = new JLabeledComboBox<>("Szerző");
        this.tfTitle = new JLabeledTextField("Cím", 40);

        prepareComponents();

        formPane.add(cbComposer);
        formPane.add(tfTitle);
    }

    private void prepareComponents() {
        cbComposer.addListItems(ContextUtil.getBean(ComposerCache.class).getAll());

        cbComposer.setMandatory(true);
        tfTitle.setMandatory(true);

        cbComposer.setTextProvider(TextProviders.composerTextProvider);
        cbComposer.setItemComparator(Comparators.composerComparator);
        cbComposer.setProvidingNewAddition(true);
        cbComposer.setItemSupplier(this::createComposer);

    }

    private Optional<ComposerDto> createComposer() {
        var dialog = new ComposerDialog((Frame) getOwner());
        if (dialog.getDialogStatus() ==  OK) {

            var savedComposerDto = ContextUtil.getBean(ComposerCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedComposerDto);
        }

        return Optional.empty();
    }

    @Override
    protected void initiateValue() {
        if (Objects.nonNull(value)) {
            cbComposer.setSelectedItem(value.getComposer());
            tfTitle.setText(value.getTitle());
        }
    }

    @Override
    protected PlayListDto composeValue() {
        if (Objects.nonNull(value)) {
            return PlayListDto.builder()
                    .withNaturalId(value.getNaturalId())
                    .withComposer(cbComposer.getSelectedItem())
                    .withTitle(tfTitle.getText())
                    .build();
        }

        return PlayListDto.builder()
                .withComposer(cbComposer.getSelectedItem())
                .withTitle(tfTitle.getText())
                .build();
    }
}
