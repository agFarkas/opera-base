package hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.RoleJoinDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.service.cache.RoleCache;
import hu.agfcodeworks.operangel.application.service.commmandservice.ArtistCommandService;
import hu.agfcodeworks.operangel.application.service.commmandservice.LocationCommandService;
import hu.agfcodeworks.operangel.application.service.commmandservice.PlayCommandService;
import hu.agfcodeworks.operangel.application.service.commmandservice.RoleCommandService;
import hu.agfcodeworks.operangel.application.service.queryservice.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.service.queryservice.PlayQueryService;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledList;
import hu.agfcodeworks.operangel.application.ui.dialog.ArtistDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.LocationDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.PlayDialog;
import hu.agfcodeworks.operangel.application.ui.editor.ArtistEditor;
import hu.agfcodeworks.operangel.application.ui.editor.DateEditor;
import hu.agfcodeworks.operangel.application.ui.editor.LocationEditor;
import hu.agfcodeworks.operangel.application.ui.editor.OperationOnChangingValue;
import hu.agfcodeworks.operangel.application.ui.editor.RoleEditor;
import hu.agfcodeworks.operangel.application.ui.renderer.OperaTableCellRenderer;
import hu.agfcodeworks.operangel.application.util.ContextUtil;
import lombok.NonNull;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.COLUMN_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_ARTIST;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.roleChangeOperationOptions;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.yesNoOptions;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.playDtoByTitleComparator;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.composerPlayTextProvider;
import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;

public class OperasTabPane extends AbstractCustomTabPane {

    private static final String TITLE_CREATE_LOCATION_DIALOG = "Új helyszín";

    private static final String TITLE_CREATE_ARTIST_DIALOG = "Új énekes";

    private static final String CAPTION_CONDUCTOR = "Karmester";

    private static final String ROLE_CHANGE_QUESTION = "A '%s' szerep megnevezését átírtad a következőre: '%s'.\r\nMódosítod a leírást a meglévő szerepnél, vagy új szerepet hozol létre az új leírással?";

    public static final String OPERA_DELETE_QUESTION = "Biztosan törlöd ezt az operát: '%s'?";

    public static final String CHOOSING_OPERATION_TITLE = "Műveletválasztás";

    public static final String TITLE_CREATE_OPERA_DIALOG = "Új opera";

    public static final String TITLE_UPDATE_OPERA_DIALOG = "Opera módosítása";


    private final JLabeledList<PlayListDto> lsOpera = new JLabeledList<>("Operák");

    private final JButton btCreateOpera = new JButton("Új opera...");

    private final JButton btUpdateOpera = new JButton("Módosítás...");

    private final JButton btDeleteOpera = new JButton("Törlés");

    private final JButton btCreatePerformance = new JButton("Új előadás");

    private final JButton btDeletePerformance = new JButton("Előadás törlése");

    private final JButton btSaveAllPerformance = new JButton("Összes módosítás");

    private PlayListDto selectedPlay;

    private List<PerformanceDto> performances;

    private int lastConductorRow = ROW_CONDUCTOR;

    private OperaTableCellRenderer operaTableCellRenderer = new OperaTableCellRenderer(ROW_CONDUCTOR);

    private final JTable tblPerformances = new JTable() {

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == COLUMN_ROLE && row <= lastConductorRow) {
                return false;
            }

            if (column > COLUMN_ROLE && isRoleRow(row)) {
                return Objects.nonNull(getValueAt(row, COLUMN_ROLE));
            }

            return super.isCellEditable(row, column);
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == COLUMN_ROLE) {
                if (isRoleRow(row)) {
                    return new RoleEditor(
                            row,
                            r -> createRole(r),
                            r -> updateRole(r),
                            this::addRowIfLast,
                            (originalRole, newRoleDescription) -> chooseOperation(originalRole, newRoleDescription),
                            ContextUtil.getBean(RoleCache.class).get(selectedPlay.getNaturalId())
                    );
                }
            }

            return switch (row) {
                case ROW_DATE -> new DateEditor(row, column, (r, c) -> editingFinished(r, c));
                case ROW_LOCATION -> new LocationEditor(() -> createLocation());
                default -> {
                    if (isConductorRow(row)) {
                        yield new ArtistEditor(() -> createArtist(), FONT_STYLE_CONDUCTOR);
                    }

                    yield isRoleRow(row) ? new ArtistEditor(() -> createArtist(), FONT_STYLE_ARTIST) : super.getCellEditor(row, column);
                }
            };
        }

        private void addRowIfLast(int row) {
            if (isLastRow(row)) {
                addEmptyRow();
            }
        }

        private boolean isLastRow(int row) {
            return row == getRowCount() - 1;
        }

    };

    private void addEmptyRow() {
        retrieveModel()
                .addRow(new Vector<>());
    }

    private Optional<OperationOnChangingValue> chooseOperation(RoleDto originalRole, String newRoleDescription) {
        if (occursOnlyOnce(originalRole)) {
            return Optional.of(OperationOnChangingValue.UPDATE_EXISTING);
        }

        var index = getRoleChangeOperationIndex(originalRole, newRoleDescription);

        return switch (index) {
            case 0 -> Optional.of(OperationOnChangingValue.CREATE_NEW);
            case 1 -> Optional.of(OperationOnChangingValue.UPDATE_EXISTING);
            default -> Optional.empty();
        };
    }

    private boolean occursOnlyOnce(RoleDto roleDto) {
        var count = 0;

        for (var r = getFirstRoleRow(); r < tblPerformances.getRowCount(); r++) {
            if (foundRoleInRow(roleDto, r)) {
                if (count == 0) {
                    count++;
                } else {
                    return false;
                }
            }
        }

        return count == 1;
    }

    private boolean foundRoleInRow(RoleDto roleDto, int row) {
        var value = tblPerformances.getValueAt(row, COLUMN_ROLE);

        if (value instanceof RoleDto r) {
            return Objects.equals(r.getNaturalId(), roleDto.getNaturalId());
        }

        return false;
    }

    private int getRoleChangeOperationIndex(RoleDto originalRole, String newRoleDescription) {
        return JOptionPane.showOptionDialog(owner,
                ROLE_CHANGE_QUESTION.formatted(originalRole.getDescription(), newRoleDescription), CHOOSING_OPERATION_TITLE,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, roleChangeOperationOptions, roleChangeOperationOptions[0]);
    }

    private boolean isConductorRow(int row) {
        return row >= ROW_CONDUCTOR && row <= lastConductorRow;
    }

    private boolean isRoleRow(int row) {
        return row > lastConductorRow;
    }

    private void editingFinished(int row, int col) {

    }

    public OperasTabPane(Frame owner) {
        super(owner, new BorderLayout());

        initTableDataModel();

        add(makeCrudPanel(), BorderLayout.PAGE_START);
        add(makeSplitPane());
        setOperaDatasEnabled(false);
    }

    private JPanel makeCrudPanel() {
        var crudPanel = new JPanel(new BorderLayout());

        crudPanel.add(makeOperaCrudPanel(), BorderLayout.PAGE_START);
        crudPanel.add(makePerformanceCrudPanel(), BorderLayout.PAGE_END);

        return crudPanel;
    }

    private void initTableDataModel() {
        Optional<PerformanceSummaryDto> emptyPerformanceSummaryDtoOpt = Optional.empty();
        var maxNumOfRoles = calculateMaxNumOfRoles(emptyPerformanceSummaryDtoOpt, Collections.emptyList());

        initTableDataModel(emptyPerformanceSummaryDtoOpt, maxNumOfRoles);
        fixColumnWidths();
    }

    private void initTableDataModel(PlayDetailedDto playDetailedDto, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var roles = playDetailedDto.getRoles();
        var maxNumOfRoles = calculateMaxNumOfRoles(performanceSummaryDtoOpt, roles);

        initTableDataModel(performanceSummaryDtoOpt, maxNumOfRoles);

        if (performanceSummaryDtoOpt.isPresent()) {
            fillRoles(roles, performanceSummaryDtoOpt);
        } else {
            fillRoles(roles);
        }

        fillPerformances(performanceSummaryDtoOpt.map(PerformanceSummaryDto::getPerformances).orElseGet(Collections::emptyList));

        fixColumnWidths();
    }

    private void fillRoles(List<RoleDto> roles, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var firstRoleRow = getFirstRoleRow();
        var rolesToDisplay = collectRoles(roles, performanceSummaryDtoOpt);

        var r = 0;
        for (var roleDto : rolesToDisplay) {
            tblPerformances.setValueAt(roleDto, firstRoleRow + (r++), COLUMN_ROLE);
        }
    }

    private List<RoleDto> collectRoles(List<RoleDto> roles, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var list = new LinkedList<RoleDto>();

        for (var roleDto : roles) {
            var maxRoleCountInPrefromaces = performanceSummaryDtoOpt.map(perfSumDto -> getMaxRoleCountOf(roleDto, perfSumDto))
                    .orElse(1);

            for (var i = 0; i < maxRoleCountInPrefromaces; i++) {
                list.add(roleDto);
            }
        }

        return list;
    }

    private Integer getMaxRoleCountOf(RoleDto roleDto, PerformanceSummaryDto perfSumDto) {
        return perfSumDto.getMaxRoleCounts().getOrDefault(roleDto.getNaturalId(), 1);
    }

    private void fillRoles(List<RoleDto> roles) {
        var firstRoleRow = getFirstRoleRow();

        for (int i = 0; i < roles.size(); i++) {
            var roleDto = roles.get(i);
            tblPerformances.setValueAt(roleDto, firstRoleRow + i, COLUMN_ROLE);
        }
    }

    private void fillPerformances(List<PerformanceDto> performances) {
        for (var i = 0; i < performances.size(); i++) {
            var performanceColumnIndex = COLUMN_ROLE + 1 + i;
            var performance = performances.get(i);

            fillPerformanceHeadDatas(performance, performanceColumnIndex);
            fillPerformanceRoleDatas(performance, performanceColumnIndex);
        }
    }

    private void fillPerformanceHeadDatas(PerformanceDto performance, int performanceColumnIndex) {
        tblPerformances.setValueAt(performance.getDate(), ROW_DATE, performanceColumnIndex);
        tblPerformances.setValueAt(performance.getLocation(), ROW_LOCATION, performanceColumnIndex);

        for (var i = 0; i < performance.getConductors().size(); i++) {
            tblPerformances.setValueAt(performance.getConductors().get(i), ROW_CONDUCTOR + i, performanceColumnIndex);
        }

    }

    private void fillPerformanceRoleDatas(PerformanceDto performanceDto, int performanceColumnIndex) {
        for (var join : performanceDto.getRoleArtists()) {
            var rowNumber = seekFirstEmptyPerformanceRowFor(performanceColumnIndex, join.getRoleJoinDto());

            tblPerformances.setValueAt(join.getArtistListDto(), rowNumber, performanceColumnIndex);
        }
    }

    private int seekFirstEmptyPerformanceRowFor(int performanceColumnIndex, RoleJoinDto roleJoinDto) {
        for (var r = getFirstRoleRow(); r < tblPerformances.getRowCount(); r++) {
            var roleValue = tblPerformances.getValueAt(r, COLUMN_ROLE);

            if (roleValue instanceof RoleDto roleDto
                    && isRoleEqual(roleDto, roleJoinDto)
                    && isRowEmptyForPerformance(performanceColumnIndex, r)
            ) {
                return r;
            }
        }

        return -1;
    }

    private boolean isRoleEqual(RoleDto roleDto, RoleJoinDto roleJoinDto) {
        return Objects.equals(roleDto.getNaturalId(), roleJoinDto.getNaturalId());
    }

    private boolean isRowEmptyForPerformance(int performanceColumnIndex, int rowIndex) {
        return tblPerformances.getValueAt(rowIndex, performanceColumnIndex) == null;
    }

    private void initTableDataModel(Optional<PerformanceSummaryDto> performanceSummaryOpt, int maxNumOfRoles) {

        var maxNumOfConductors = performanceSummaryOpt.map(PerformanceSummaryDto::getMaxConductorCount).orElse(1);

        var numOfPerformances = performanceSummaryOpt.map(ps -> ps.getPerformances().size()).orElse(0);

        var rowCount = 2 + maxNumOfConductors + maxNumOfRoles + 1;
        var columnCount = 1 + numOfPerformances;

        lastConductorRow = ROW_CONDUCTOR - 1 + maxNumOfConductors;
        operaTableCellRenderer.setLastConductorRow(lastConductorRow);

        var model = new DefaultTableModel(rowCount, columnCount);

        model.setValueAt("Dátum", ROW_DATE, COLUMN_ROLE);
        model.setValueAt("Helyszín", ROW_LOCATION, COLUMN_ROLE);

        for (var r = ROW_CONDUCTOR; r <= lastConductorRow; r++) {
            model.setValueAt(CAPTION_CONDUCTOR, r, COLUMN_ROLE);
        }

        tblPerformances.setModel(model);
        tblPerformances.setTableHeader(null);
    }

    private int calculateMaxNumOfRoles(@NonNull Optional<PerformanceSummaryDto> performanceSummaryOpt, @NonNull List<RoleDto> roles) {
        if (performanceSummaryOpt.isEmpty()) {
            return roles.size();
        }

        var roleCountsFromPerformanceSummeries = performanceSummaryOpt.map(PerformanceSummaryDto::getMaxRoleCounts)
                .orElseGet(Collections::emptyMap);

        var roleCountOutOfPerformances = countRolesOutOfAllPerformances(performanceSummaryOpt, roles);
        int roleCountsSumInPerformances = roleCountsFromPerformanceSummeries.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        return roleCountsSumInPerformances + roleCountOutOfPerformances;
    }

    private int countRolesOutOfAllPerformances(@NonNull Optional<PerformanceSummaryDto> performanceSummaryOpt, @NonNull List<RoleDto> roles) {
        var roleNaturalIdsInPerformances = performanceSummaryOpt.map(perfSumDto -> perfSumDto.getMaxRoleCounts().keySet())
                .orElseGet(Collections::emptySet);

        return (int) roles.stream()
                .filter(role -> !roleNaturalIdsInPerformances.contains(role.getNaturalId()))
                .count();
    }

    private JPanel makeOperaCrudPanel() {
        var operaCrudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btCreateOpera.addActionListener(e -> createOpera());
        btUpdateOpera.addActionListener(e -> updateOpera());
        btDeleteOpera.addActionListener(e -> deleteOpera());

        operaCrudPanel.add(btCreateOpera);
        operaCrudPanel.add(btUpdateOpera);
        operaCrudPanel.add(btDeleteOpera);

        return operaCrudPanel;
    }

    private JPanel makePerformanceCrudPanel() {
        var performanceCrudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btCreatePerformance.addActionListener(e -> createPerformance());
        btDeletePerformance.addActionListener(e -> deletePerformance());
        performanceCrudPanel.add(btCreatePerformance);
        performanceCrudPanel.add(btDeletePerformance);

        return performanceCrudPanel;
    }

    private void createOpera() {
        var dialog = new PlayDialog(owner, TITLE_CREATE_OPERA_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var playListDto = ContextUtil.getBean(PlayCommandService.class)
                    .saveOpera(makePlayCommand(dialog.getValue()));

            putPlayIntoList(playListDto);
        }
    }

    private void updateOpera() {
        var dialog = new PlayDialog(owner, TITLE_UPDATE_OPERA_DIALOG, selectedPlay);

        if (dialog.getDialogStatus() == OK) {
            var playListDto = ContextUtil.getBean(PlayCommandService.class)
                    .saveOpera(makePlayCommand(dialog.getValue()));

            putPlayIntoList(playListDto);
        }
    }

    private void deleteOpera() {
        if (showConfirmationForDeleteOpera() != 0) {
            return;
        }

        ContextUtil.getBean(PlayCommandService.class)
                .deletePlay(selectedPlay.getNaturalId());

        var selectedIndex = lsOpera.getSelectedIndex();
        if (selectedIndex == lsOpera.getCountOfElements() - 1) {
            lsOpera.setSelectedIndex(selectedIndex - 1);
        }

        lsOpera.removeItemAt(selectedIndex);
    }

    private int showConfirmationForDeleteOpera() {
        return JOptionPane.showOptionDialog(owner,
                OPERA_DELETE_QUESTION.formatted(composerPlayTextProvider.apply(selectedPlay)),
                CHOOSING_OPERATION_TITLE,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, yesNoOptions, yesNoOptions[1]);
    }

    ;

    private PlayCommand makePlayCommand(PlayListDto playListDto) {
        return PlayCommand.builder()
                .withNaturalId(playListDto.getNaturalId())
                .withComposerDto(playListDto.getComposer())
                .withTitle(playListDto.getTitle())
                .build();
    }

    private void putPlayIntoList(PlayListDto playListDto) {
        if (lsOpera.getIndexOfItem(playListDto) > -1) {
            lsOpera.removeItem(playListDto);
        }

        lsOpera.addItem(playListDto);

        lsOpera.setSelectedItem(playListDto);
    }

    private RoleDto createRole(RoleDto roleDto) {
        if (Objects.nonNull(roleDto.getNaturalId())) {
            return roleDto;
        }

        return ContextUtil.getBean(RoleCommandService.class)
                .save(makeRoleCommand(roleDto));
    }

    private RoleDto updateRole(RoleDto roleDto) {
        var newRoleDto = ContextUtil.getBean(RoleCommandService.class)
                .save(makeRoleCommand(roleDto));

        overwriteAllOccurencesOf(newRoleDto);

        return newRoleDto;
    }

    private void overwriteAllOccurencesOf(RoleDto newRoleDto) {
        var selectedRow = tblPerformances.getSelectedRow();

        for (var r = lastConductorRow + 1; r < retrieveModel().getRowCount(); r++) {
            if (r != selectedRow && tblPerformances.getValueAt(r, COLUMN_ROLE) instanceof RoleDto roleDto) {
                if (Objects.equals(newRoleDto.getNaturalId(), roleDto.getNaturalId())) {
                    tblPerformances.setValueAt(newRoleDto, r, COLUMN_ROLE);
                }
            }
        }
    }

    private RoleCommand makeRoleCommand(RoleDto roleDto) {
        return RoleCommand.builder()
                .withPlayNaturalId(selectedPlay.getNaturalId())
                .withNaturalId(roleDto.getNaturalId())
                .withDescription(roleDto.getDescription())
                .build();
    }

    private void deleteRole(RoleDto roleDto) {

    }

    private void createPerformance() {
        addPerformanceColumn();
    }

    private void createPerformance(PerformanceDto performanceDto) {
        var columnIndex = addPerformanceColumn();
        var column = tblPerformances.getColumnModel()
                .getColumn(columnIndex);

        column.setIdentifier(performanceDto.getNaturalId());
    }

    private int addPerformanceColumn() {
        var model = retrieveModel();
        var columnIndex = model.getColumnCount();

        model.addColumn(columnIndex);

        fixColumnWidths();

        return columnIndex;
    }

    private void deletePerformance() {

    }

    private void fixColumnWidths() {
        var columnModel = tblPerformances.getColumnModel();
        var count = columnModel.getColumnCount();

        for (var i = 0; i < count; i++) {
            var column = columnModel.getColumn(i);
            column.setPreferredWidth(calculateColumnWidth(i));

            column.setCellRenderer(operaTableCellRenderer);
        }
    }

    private int calculateColumnWidth(int column) {
        return switch (column) {
            case COLUMN_ROLE -> 140;
            default -> 200;
        };
    }

    private Optional<LocationDto> createLocation() {
        var dialog = new LocationDialog(owner, TITLE_CREATE_LOCATION_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = ContextUtil.getBean(LocationCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private Optional<ArtistListDto> createArtist() {
        var dialog = new ArtistDialog(owner, TITLE_CREATE_ARTIST_DIALOG);
        if (dialog.getDialogStatus() == OK) {
            var savedValue = ContextUtil.getBean(ArtistCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private JSplitPane makeSplitPane() {
        prepareLsOpera();
        prepareTblPerformances();

        var scrollPane = new JScrollPane(tblPerformances);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lsOpera, scrollPane);

        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(2);
        splitPane.setEnabled(false);

        return splitPane;
    }

    private void prepareTblPerformances() {
        tblPerformances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private DefaultTableModel retrieveModel() {
        return (DefaultTableModel) tblPerformances.getModel();
    }

    private void prepareLsOpera() {
        lsOpera.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lsOpera.setTextProvider(composerPlayTextProvider);
        lsOpera.setItemComparator(playDtoByTitleComparator);
        lsOpera.addListSelectionListener(e -> this.refreshDetails());
    }

    private void refreshDetails() {
        var playListDtoOpt = lsOpera.getSelectedItem();

        if (playListDtoOpt.isPresent()) {
            var playListDto = playListDtoOpt.get();
            setSelectedPlay(playListDto);

            ContextUtil.getBean(PlayQueryService.class)
                    .getPlay(playListDto.getNaturalId())
                    .ifPresent(p -> {
                        var performanceSummary = ContextUtil.getBean(PerformanceQueryService.class)
                                .getPerformancesForPlay(selectedPlay);
                        refreshDetails(p, performanceSummary);
                    });

            setOperaDatasEnabled(true);
        }
    }

    private void setSelectedPlay(PlayListDto playListDto) {
        this.selectedPlay = playListDto;
    }

    private void refreshDetails(@NonNull PlayDetailedDto playDetailedDto, @NonNull Optional<PerformanceSummaryDto> performanceSummaryOpt) {
        initTableDataModel(playDetailedDto, performanceSummaryOpt);
    }

    @Override
    public void refreshContent() {
        clearContent();
        lsOpera.addItems(
                ContextUtil.getBean(PlayQueryService.class)
                        .getAllOperas()
        );
    }

    @Override
    public void clearContent() {
        lsOpera.removeAllItems();
    }

    @Override
    protected void setComponentsEnabled(boolean enabled) {
        btCreateOpera.setEnabled(enabled);
        setOperaDatasEnabled(Objects.nonNull(selectedPlay));
    }

    private void setOperaDatasEnabled(boolean enabled) {
        btDeleteOpera.setEnabled(enabled);
        btUpdateOpera.setEnabled(enabled);
        btCreatePerformance.setEnabled(enabled);
        btDeletePerformance.setEnabled(enabled);

        tblPerformances.setEnabled(enabled);
    }

    private int getFirstRoleRow() {
        return lastConductorRow + 1;
    }
}
