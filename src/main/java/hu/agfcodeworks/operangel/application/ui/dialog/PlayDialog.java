package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.dto.ErrorDto;
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

import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;

public class PlayDialog extends JAbstractDialog<PlayListDto> {

    private static final String TITLE_CREATE_COMPOSER_DIALOG = "Új szerző";

    private JLabeledComboBox<ComposerDto> cbComposer;

    private JLabeledTextField tfTitle;

    public PlayDialog(Frame owner, String title, PlayListDto initialValue) {
        super(owner, title, initialValue);
        setVisible(true);
    }

    public PlayDialog(Frame owner, String title) {
        super(owner, title, null);

        cbComposer.setSelectedIndex(-1);

        setVisible(true);
    }

    @Override
    protected List<ErrorDto> validateCustomFields() {
        var errorDtos = new LinkedList<ErrorDto>();

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
        var dialog = new ComposerDialog((Frame) getOwner(), TITLE_CREATE_COMPOSER_DIALOG);
        if (dialog.getDialogStatus() ==  OK) {

            var savedComposerDto = ContextUtil.getBean(ComposerCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedComposerDto);
        }

        return Optional.empty();
    }

    @Override
    protected void initiateValue(PlayListDto initialValue) {
        if (Objects.nonNull(initialValue)) {
            cbComposer.setSelectedItem(initialValue.getComposer());
            tfTitle.setText(initialValue.getTitle());
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
