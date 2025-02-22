package hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.RoleJoinDto;
import hu.agfcodeworks.operangel.application.service.cache.RoleCache;
import hu.agfcodeworks.operangel.application.service.commmandservice.ArtistCommandService;
import hu.agfcodeworks.operangel.application.service.commmandservice.LocationCommandService;
import hu.agfcodeworks.operangel.application.service.queryservice.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.service.queryservice.PlayQueryService;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledList;
import hu.agfcodeworks.operangel.application.ui.dialog.ArtistDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.LocationDialog;
import hu.agfcodeworks.operangel.application.ui.editor.ArtistEditor;
import hu.agfcodeworks.operangel.application.ui.editor.DateEditor;
import hu.agfcodeworks.operangel.application.ui.editor.LocationEditor;
import hu.agfcodeworks.operangel.application.ui.editor.RoleEditor;
import hu.agfcodeworks.operangel.application.ui.renderer.OperaTableCellRenderer;
import hu.agfcodeworks.operangel.application.util.ContextUtil;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.COLUMN_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_ARTIST;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.playDtoByTitleComparator;
import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;

public class OperasTabPane extends AbstractCustomTabPane {

    private static final String PLAY_LIST_ITEM_TEXT_PATTERN = "%s: %s";

    private static final String TITLE_CREATE_LOCATION_DIALOG = "Új helyszín";

    private static final String TITLE_CREATE_ARTIST_DIALOG = "Új énekes";

    private static final String CAPTION_CONDUCTOR = "Karmester";

    private final JLabeledList<PlayDto> lsOpera = new JLabeledList<>("Operák");

    private final JButton btCreateOpera = new JButton("Új opera...");

    private final JButton btUpdateOpera = new JButton("Módosítás");

    private final JButton btDeleteOpera = new JButton("Törlés");

    private final JButton btCreatePerformance = new JButton("Új előadás");

    private final JButton btDeletePerformance = new JButton("Előadás törlése");

    private final JButton btSaveAllPerformance = new JButton("Összes módosítás");

    private PlayDto selectedPlay;

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
                            r -> createRole(r),
                            r -> updateRole(r),
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

    };

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
        initTableDataModel(Optional.empty());
        fixColumnWidths();
    }

    private void initTableDataModel(PlayDetailedDto playDetailedDto, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var roles = playDetailedDto.getRoles();

        initTableDataModel(performanceSummaryDtoOpt);

        fillRoles(roles, performanceSummaryDtoOpt);
        fillPerformances(performanceSummaryDtoOpt.map(PerformanceSummaryDto::getPerformances).orElseGet(Collections::emptyList));

        fixColumnWidths();
    }

    private void fillRoles(List<RoleDto> roles, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var actRowNumber = lastConductorRow + 1;

        for (int i = 0; i < roles.size(); i++) {
            var roleDto = roles.get(i);

            var count = performanceSummaryDtoOpt.map(dto -> dto.getMaxRoleCounts().get(roleDto.getNaturalId())).orElse(1);

            for (var n = 0; n < count; n++) {
                tblPerformances.setValueAt(roleDto, actRowNumber + i + n, COLUMN_ROLE);
            }

            actRowNumber += count;
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
        for (var r = lastConductorRow + 1; r < tblPerformances.getRowCount(); r++) {
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

    private static boolean isRoleEqual(RoleDto roleDto, RoleJoinDto roleJoinDto) {
        return Objects.equals(roleDto.getNaturalId(), roleJoinDto.getNaturalId());
    }

    private boolean isRowEmptyForPerformance(int performanceColumnIndex, int rowIndex) {
        return tblPerformances.getValueAt(rowIndex, performanceColumnIndex) == null;
    }

    private void initTableDataModel(Optional<PerformanceSummaryDto> performanceSummaryOpt) {
        var roleCounts = performanceSummaryOpt.map(PerformanceSummaryDto::getMaxRoleCounts).orElseGet(Collections::emptyMap);

        var maxNumOfConductors = performanceSummaryOpt.map(PerformanceSummaryDto::getMaxConductorCount).orElse(1);
        var maxNumOfRoles = roleCounts.values().stream().mapToInt(Integer::intValue).sum();

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

    private JPanel makeOperaCrudPanel() {
        var operaCrudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btCreateOpera.addActionListener(e -> createNewOpera());
        btUpdateOpera.addActionListener(e -> updateOpera());
        btCreateOpera.addActionListener(e -> deleteOpera());

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

    private void createNewOpera() {

    }

    private void updateOpera() {

    }

    private void deleteOpera() {

    }

    private RoleDto createRole(RoleDto roleDto) {
        var newRoleDto = RoleDto.builder()
                .withDescription(roleDto.getDescription())
                .withNaturalId(UUID.randomUUID())
                .build();

        retrieveModel()
                .addRow(new Vector<>());
        return newRoleDto;
    }

    private RoleDto updateRole(RoleDto roleDto) {
        return RoleDto.builder()
                .withDescription(roleDto.getDescription())
                .withNaturalId(roleDto.getNaturalId())
                .build();
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
            var artistListDto = dialog.getValue();

            var savedValue = ContextUtil.getBean(ArtistCommandService.class)
                    .save(artistListDto);

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
        lsOpera.setTextProvider(p -> PLAY_LIST_ITEM_TEXT_PATTERN.formatted(
                p.getComposer().getFamilyName(),
                p.getTitle()
        ));
        lsOpera.setItemComparator(playDtoByTitleComparator);
        lsOpera.addListSelectionListener(e -> this.refreshDetails());
    }

    private void refreshDetails() {
        var playDto = lsOpera.getSelectedItem();

        if (Objects.nonNull(playDto)) {
            this.selectedPlay = playDto;

            ContextUtil.getBean(PlayQueryService.class)
                    .getPlay(playDto.getNaturalId())
                    .ifPresent(p -> {
                        var performanceSummary = ContextUtil.getBean(PerformanceQueryService.class)
                                .getPerformancesForPlay(selectedPlay);
                        refreshDetails(p, performanceSummary);
                    });

            setOperaDatasEnabled(true);
        }
    }

    private void refreshDetails(PlayDetailedDto playDetailedDto, Optional<PerformanceSummaryDto> performanceSummaryOpt) {
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
}
