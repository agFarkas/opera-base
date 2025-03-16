package hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.RolePerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.command.ArtistPerformanceRoleJoinDeleteCommand;
import hu.agfcodeworks.operangel.application.dto.command.ArtistRoleChangeCommand;
import hu.agfcodeworks.operangel.application.dto.command.PlayCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleChangeCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.service.cache.RoleCache;
import hu.agfcodeworks.operangel.application.service.commmandservice.ArtistCommandService;
import hu.agfcodeworks.operangel.application.service.commmandservice.ArtistPerformanceRoleJoinCommandService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.COLUMN_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_ARTIST;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_FIRST_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.roleChangeOperationOptions;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.yesNoOptions;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.playDtoByTitleComparator;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.composerPlayTextProvider;
import static hu.agfcodeworks.operangel.application.ui.uidto.DialogStatus.OK;
import static hu.agfcodeworks.operangel.application.util.ContextUtil.getBean;

public class OperasTabPane extends AbstractCustomTabPane {

    private static final String TITLE_CREATE_LOCATION_DIALOG = "Új helyszín";

    private static final String TITLE_CREATE_SINGER_DIALOG = "Új énekes";

    private static final String TITLE_CREATE_CONDUCTOR_DIALOG = "Új karmester";

    private static final String CAPTION_CONDUCTOR = "Karmester";

    private static final String CAPTION_DATE = "Dátum";

    private static final String CAPTION_LOCATION = "Helyszín";

    private static final String ROLE_CHANGE_QUESTION = "A '%s' szerep megnevezését átírtad a következőre: '%s'.\r\nMódosítod a leírást a meglévő szerepnél, vagy új szerepet hozol létre az új leírással?";

    public static final String OPERA_DELETE_QUESTION = "Biztosan törlöd ezt az operát: '%s'?";

    public static final String CHOOSING_OPERATION_TITLE = "Műveletválasztás";

    public static final String TITLE_CREATE_OPERA_DIALOG = "Új opera";

    public static final String TITLE_UPDATE_OPERA_DIALOG = "Opera módosítása";


    private final JLabeledList<PlayListDto> lsOpera = new JLabeledList<>("Operák");

    private final JButton btCreateOpera = new JButton("Új opera...");

    private final JButton btUpdateOpera = new JButton("Módosítás...");

    private final JButton btDeleteOpera = new JButton("Törlés");

    private final JButton btRefresh = new JButton("Frissítés");

    private final JButton btCreatePerformance = new JButton("Új előadás");

    private final JButton btDeletePerformance = new JButton("Előadás törlése");

    private final JButton btAddConductor = new JButton("Karmester hozzáadása");

    private int lastConductorRow = ROW_FIRST_CONDUCTOR;

    private final OperaTableCellRenderer operaTableCellRenderer = new OperaTableCellRenderer(ROW_FIRST_CONDUCTOR);

    private PlayListDto selectedPlay;

    private List<PerformanceSimpleDto> performances;

    private final JTable tblPerformances = new JTable() {

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == COLUMN_ROLE && row <= lastConductorRow) {
                return false;
            }

            if (column > COLUMN_ROLE) {
                if (isConductorRow(row)) {
                    return hasValue(row, column) || isFirstFreeRowOfConductor(row, column);
                }

                if (isRoleRow(row)) {
                    return hasRole(row) &&
                            (hasValue(row, column) || isFirstFreeRowOfRole(row, column, readSelectedRole(row)));
                }
            }

            return super.isCellEditable(row, column);
        }


        private boolean hasValue(int row, int column) {
            return Objects.nonNull(getValueAt(row, column));
        }

        private boolean hasRole(int row) {
            return hasValue(row, COLUMN_ROLE);
        }

        private boolean isFirstFreeRowOfConductor(int row, int column) {
            return row == findFirstFreeRowOfConductor(column);
        }

        private int findFirstFreeRowOfConductor(int column) {
            for (var r = ROW_FIRST_CONDUCTOR; r <= lastConductorRow; r++) {
                if (!hasValue(r, column)) {
                    return r;
                }
            }

            return -1;
        }

        private boolean isFirstFreeRowOfRole(int row, int column, RoleDto roleDto) {
            return row == findFirstEmptyRowFor(roleDto, column);
        }


        private int findFirstEmptyRowFor(RoleDto roleDto, int column) {
            var rowCount = getRowCount();

            for (var r = findFirstRoleRow(); r < rowCount; r++) {
                if (Objects.equals(getRoleAt(r), roleDto) && !hasValue(r, column)) {
                    return r;
                }
            }

            return -1;
        }

        private RoleDto getRoleAt(int row) {
            return (RoleDto) getValueAt(row, COLUMN_ROLE);
        }

        private RoleDto readSelectedRole(int row) {
            return (RoleDto) retrieveModel()
                    .getValueAt(row, COLUMN_ROLE);
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == COLUMN_ROLE) {
                if (isRoleRow(row)) {
                    return new RoleEditor(
                            row,
                            r -> createRole(r),
                            r -> updateRole(r),
                            (originalRole, newRole) -> changeRole(originalRole, newRole),
                            this::executeMandatoryOperationsOnAnyRoleValueChange,
                            (originalRole, newRoleDescription) -> chooseOperationForRole(originalRole, newRoleDescription),
                            getBean(RoleCache.class).get(selectedPlay.getNaturalId())
                    );
                }
            }

            return switch (row) {
                case ROW_DATE -> new DateEditor((originalDate, newDate) -> dateEditingFinished(originalDate, newDate));
                case ROW_LOCATION -> new LocationEditor(
                        () -> createLocation(),
                        (originalLocation, newLocation) -> locationChanged(originalLocation, newLocation)
                );
                default -> {
                    if (isConductorRow(row)) {
                        yield new ArtistEditor(
                                () -> createConductor(),
                                FONT_STYLE_CONDUCTOR,
                                (originalConductor, newConductor) -> changeConductor(originalConductor, newConductor)
                        );
                    }

                    yield isRoleRow(row) ? new ArtistEditor(
                            () -> createSinger(),
                            FONT_STYLE_ARTIST,
                            (originalSinger, newSinger) -> singerChanged(originalSinger, newSinger)
                    ) : super.getCellEditor(row, column);
                }
            };
        }

        private void executeMandatoryOperationsOnAnyRoleValueChange(int row) {
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

    private Optional<OperationOnChangingValue> chooseOperationForRole(RoleDto originalRole, String newRoleDescription) {
        if (occursOnlyOnce(originalRole)) {
            return Optional.of(OperationOnChangingValue.UPDATE_ALL_OCCURENCES);
        }

        var index = getRoleUpdateOperationIndex(originalRole, newRoleDescription);

        return switch (index) {
            case 0 -> Optional.of(OperationOnChangingValue.CREATE_NEW);
            case 1 -> Optional.of(OperationOnChangingValue.UPDATE_ALL_OCCURENCES);
            default -> Optional.empty();
        };
    }

    private boolean occursOnlyOnce(RoleDto roleDto) {
        var count = 0;
        var rowCount = retrieveModel().getRowCount();

        for (var r = findFirstRoleRow(); r < rowCount; r++) {
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
        var value = getRoleAt(row);

        if (value instanceof RoleDto r) {
            return Objects.equals(r.getNaturalId(), roleDto.getNaturalId());
        }

        return false;
    }

    private int getRoleUpdateOperationIndex(RoleDto originalRole, String newRoleDescription) {
        return JOptionPane.showOptionDialog(owner,
                ROLE_CHANGE_QUESTION.formatted(originalRole.getDescription(), newRoleDescription),
                CHOOSING_OPERATION_TITLE,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, roleChangeOperationOptions, roleChangeOperationOptions[0]);
    }

    private boolean isConductorRow(int row) {
        return row >= ROW_FIRST_CONDUCTOR && row <= lastConductorRow;
    }

    private boolean isRoleRow(int row) {
        return row > lastConductorRow;
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
        actualizeTableShow();
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

        actualizeTableShow();
    }

    private void fillRoles(List<RoleDto> roles, Optional<PerformanceSummaryDto> performanceSummaryDtoOpt) {
        var firstRoleRow = findFirstRoleRow();
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
        var firstRoleRow = findFirstRoleRow();

        for (int i = 0; i < roles.size(); i++) {
            var roleDto = roles.get(i);
            tblPerformances.setValueAt(roleDto, firstRoleRow + i, COLUMN_ROLE);
        }
    }

    private void fillPerformances(List<PerformanceDto> performances) {
        var firstPerformanceColumn = findFirstPerformanceColumn();
        this.performances = new ArrayList<>();

        for (var c = 0; c < performances.size(); c++) {
            var performanceColumnIndex = firstPerformanceColumn + c;
            var performance = performances.get(c);

            fillPerformanceHeadDatas(performance, performanceColumnIndex);
            fillPerformanceRoleDatas(performance, performanceColumnIndex);
        }
    }

    private void fillPerformanceHeadDatas(PerformanceDto performance, int performanceColumnIndex) {
        tblPerformances.setValueAt(performance.getDate(), ROW_DATE, performanceColumnIndex);
        tblPerformances.setValueAt(performance.getLocation(), ROW_LOCATION, performanceColumnIndex);

        performances.add(PerformanceSimpleDto.builder()
                .withNaturalId(performance.getNaturalId())
                .build());

        var conductors = performance.getConductors();

        for (var i = 0; i < conductors.size(); i++) {
            var conductor = conductors.get(i);
            tblPerformances.setValueAt(conductor, ROW_FIRST_CONDUCTOR + i, performanceColumnIndex);
        }
    }

    private void fillPerformanceRoleDatas(PerformanceDto performanceDto, int performanceColumnIndex) {
        for (var join : performanceDto.getRoleArtists()) {
            var rowNumber = seekFirstEmptyPerformanceRowFor(performanceColumnIndex, join.getRoleSimpleDto());

            tblPerformances.setValueAt(join.getArtistListDto(), rowNumber, performanceColumnIndex);
        }
    }

    private int seekFirstEmptyPerformanceRowFor(int performanceColumnIndex, RoleSimpleDto roleSimpleDto) {
        var rowCount = retrieveModel().getRowCount();

        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var roleValue = getRoleAt(r);

            if (roleValue instanceof RoleDto roleDto
                    && isRoleEqual(roleDto, roleSimpleDto)
                    && isRowEmptyForPerformance(performanceColumnIndex, r)
            ) {
                return r;
            }
        }

        return -1;
    }

    private boolean isRoleEqual(RoleDto roleDto, RoleSimpleDto roleSimpleDto) {
        return Objects.equals(roleDto.getNaturalId(), roleSimpleDto.getNaturalId());
    }

    private boolean isRowEmptyForPerformance(int performanceColumnIndex, int rowIndex) {
        return tblPerformances.getValueAt(rowIndex, performanceColumnIndex) == null;
    }

    private void initTableDataModel(@NonNull Optional<PerformanceSummaryDto> performanceSummaryOpt, int maxNumOfRoles) {
        var maxNumOfConductors = performanceSummaryOpt.map(PerformanceSummaryDto::getMaxConductorCount).orElse(1);
        var numOfPerformances = performanceSummaryOpt.map(ps -> ps.getPerformances().size()).orElse(0);

        var rowCount = calculateInitialRowCount(maxNumOfRoles, maxNumOfConductors);
        var columnCount = calculateInitialColumnCount(numOfPerformances);

        lastConductorRow = ROW_FIRST_CONDUCTOR - 1 + maxNumOfConductors;
        operaTableCellRenderer.setLastConductorRow(lastConductorRow);

        var model = new DefaultTableModel(rowCount, columnCount);

        model.setValueAt(CAPTION_DATE, ROW_DATE, COLUMN_ROLE);
        model.setValueAt(CAPTION_LOCATION, ROW_LOCATION, COLUMN_ROLE);

        for (var r = ROW_FIRST_CONDUCTOR; r <= lastConductorRow; r++) {
            model.setValueAt(CAPTION_CONDUCTOR, r, COLUMN_ROLE);
        }

        tblPerformances.setModel(model);
        tblPerformances.setTableHeader(null);
    }

    private int calculateInitialRowCount(int maxNumOfRoles, Integer maxNumOfConductors) {
        return 2 + maxNumOfConductors + maxNumOfRoles + 1;
    }

    private int calculateInitialColumnCount(int numOfPerformances) {
        return 1 + numOfPerformances;
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
        btAddConductor.addActionListener(e -> addConductor());
        btRefresh.addActionListener(e -> refreshDetails());

        performanceCrudPanel.add(btCreatePerformance);
        performanceCrudPanel.add(btDeletePerformance);
        performanceCrudPanel.add(btAddConductor);
        performanceCrudPanel.add(btRefresh);

        return performanceCrudPanel;
    }

    private void dateEditingFinished(LocalDate originalDate, LocalDate newDate) {
        if (!Objects.equals(originalDate, newDate)) {
            tryToSavePerformance();
        }
    }

    private void locationChanged(LocationDto originalLocation, LocationDto newLocation) {
        if (!Objects.equals(originalLocation, newLocation)) {
            tryToSavePerformance();
        }
    }

    private void changeConductor(ArtistListDto originalConductor, ArtistListDto newConductor) {
        if (!Objects.equals(originalConductor, newConductor)) {
            if (Objects.nonNull(newConductor)) {
                tryToDeleteConductorJoin(originalConductor);
            } else {

                tryToSavePerformance();
            }
        }
    }

    private void tryToDeleteConductorJoin(ArtistListDto originalConductor) {
        var performanceSimpleDto = findSelectedPerformance();

        if (Objects.nonNull(performanceSimpleDto.getNaturalId())) {
            var selectedColumn = tblPerformances.getSelectedColumn();
            var countOfConductors = countConductorsInSelectedPerformance();

            if (countOfConductors > 0) {

            }
        }
    }

    private int countConductorsInSelectedPerformance() {
        var count = 0;
        var selectedColumn = tblPerformances.getSelectedColumn();

        for (var r = ROW_FIRST_CONDUCTOR; r <= lastConductorRow; r++) {
            if (hasValue(r, selectedColumn)) {
                count++;
            }
        }

        return count;
    }

    private boolean hasValue(int row, int column) {
        return Objects.nonNull(tblPerformances.getValueAt(row, column));
    }

    private int calculateSelectedPerformanceIndex() {
        return tblPerformances.getSelectedColumn() - 1;
    }

    private void createOpera() {
        var dialog = new PlayDialog(owner, TITLE_CREATE_OPERA_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var playListDto = getBean(PlayCommandService.class)
                    .saveOpera(makePlayCommand(dialog.getValue()));

            putPlayIntoList(playListDto);
        }
    }

    private void updateOpera() {
        var dialog = new PlayDialog(owner, TITLE_UPDATE_OPERA_DIALOG, selectedPlay);

        if (dialog.getDialogStatus() == OK) {
            var playListDto = getBean(PlayCommandService.class)
                    .saveOpera(makePlayCommand(dialog.getValue()));

            putPlayIntoList(playListDto);
        }
    }

    private void deleteOpera() {
        if (showConfirmationForDeleteOpera() != 0) {
            return;
        }

        getBean(PlayCommandService.class)
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

        return getBean(RoleCommandService.class)
                .save(makeRoleCommand(roleDto));
    }

    private RoleDto updateRole(RoleDto roleDto) {
        var newRoleDto = getBean(RoleCommandService.class)
                .save(makeRoleCommand(roleDto));

        overwriteAllOccurencesOf(newRoleDto);

        return newRoleDto;
    }

    private void overwriteAllOccurencesOf(RoleDto newRoleDto) {
        var selectedRow = tblPerformances.getSelectedRow();
        var rowCount = retrieveModel()
                .getRowCount();

        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var originalRowDto = getRoleAt(r);

            if (r != selectedRow && originalRowDto instanceof RoleDto roleDto) {
                if (Objects.equals(newRoleDto.getNaturalId(), roleDto.getNaturalId())) {
                    tblPerformances.setValueAt(newRoleDto, r, COLUMN_ROLE);
                }
            }
        }
    }

    private RoleDto getRoleAt(int row) {
        return (RoleDto) tblPerformances.getValueAt(row, COLUMN_ROLE);
    }

    private RoleCommand makeRoleCommand(RoleDto roleDto) {
        return RoleCommand.builder()
                .withPlayNaturalId(selectedPlay.getNaturalId())
                .withNaturalId(roleDto.getNaturalId())
                .withDescription(roleDto.getDescription())
                .build();
    }

    private void changeRole(RoleDto originalRole, RoleDto newRoleDto) {
        changeRoleForOneOccurence(originalRole, newRoleDto);
    }

    private void changeRoleForOneOccurence(RoleDto originalRole, RoleDto newRoleDto) {
        var duplicateAssociations = collectDuplicateArtistPerformanceAssociations(originalRole, newRoleDto);
        var roleChangeCommand = makeRoleChangeCommandExceptDuplicateAssociations(originalRole, newRoleDto, duplicateAssociations);

        getBean(ArtistPerformanceRoleJoinCommandService.class)
                .updateFromRoleToRole(roleChangeCommand);

        if (isOnlyRowWithRole(originalRole)) {
            deleteRole(originalRole);
        }

        clearDuplicateAssociationsInTable(roleChangeCommand, duplicateAssociations);
    }

    private void clearDuplicateAssociationsInTable(RoleChangeCommand roleChangeCommand, Set<ArtistPerformanceSimpleDto> duplicateAssociations) {
        duplicateAssociations.forEach(artistPerformanceSimpleDto -> {
            var performanceColumn = calculateSelectedPerformanceColumn(artistPerformanceSimpleDto.getPerformanceSimpleDto());

            clearDuplicateAssociationInPerformanceColumn(roleChangeCommand, performanceColumn, artistPerformanceSimpleDto);
        });

    }

    private void clearDuplicateAssociationInPerformanceColumn(RoleChangeCommand roleChangeCommand, int performanceColumn, ArtistPerformanceSimpleDto artistPerformanceSimpleDto) {
        var artists = new HashSet<ArtistListDto>();

        var rowCount = tblPerformances.getRowCount();
        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var roleDto = getRoleAt(r);

            if (Objects.isNull(roleDto)) {
                continue;
            }

            var actRoleNaturalId = roleDto.getNaturalId();
            if (Objects.equals(actRoleNaturalId, roleChangeCommand.getOriginalRole().getNaturalId()) || Objects.equals(actRoleNaturalId, roleChangeCommand.getNewRole().getNaturalId())) {
                var artistDto = (ArtistListDto) tblPerformances.getValueAt(r, performanceColumn);

                if (Objects.nonNull(artistDto) && Objects.equals(artistDto.getNaturalId(), artistPerformanceSimpleDto.getArtistSimpleDto().getNaturalId())) {
                    if (artists.contains(artistDto)) {
                        clearCell(r, performanceColumn);
                    } else {
                        artists.add(artistDto);
                    }
                }
            }
        }
    }

    private int calculateSelectedPerformanceColumn(PerformanceSimpleDto performanceSimpleDto) {
        return findFirstPerformanceColumn() + performances.indexOf(performanceSimpleDto);
    }

    private Set<ArtistPerformanceSimpleDto> collectDuplicateArtistPerformanceAssociations(RoleDto originalRole, RoleDto newRoleDto) {
        var duplicateAssociations = new HashSet<ArtistPerformanceSimpleDto>();

        for (var i = 0; i < performances.size(); i++) {
            duplicateAssociations.addAll(
                    collectDuplicateArtistPerformanceAssociationsForPerformance(originalRole, newRoleDto, i)
            );
        }

        return duplicateAssociations;
    }

    private Set<ArtistPerformanceSimpleDto> collectDuplicateArtistPerformanceAssociationsForPerformance(RoleDto originalRole, RoleDto newRoleDto, int performanceIndex) {
        var allAssociations = new HashSet<ArtistPerformanceSimpleDto>();
        var duplicateAssociations = new HashSet<ArtistPerformanceSimpleDto>();
        var performanceSimpleDto = performances.get(performanceIndex);

        var rowCount = tblPerformances.getRowCount();
        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var roleDto = getRoleAt(r);

            if (Objects.equals(roleDto, originalRole) || Objects.equals(roleDto, newRoleDto)) {
                var artistListDto = (ArtistListDto) tblPerformances.getValueAt(r, findFirstPerformanceColumn() + performanceIndex);
                var artistPerformanceSimpleDto = Objects.nonNull(artistListDto) ? makeArtistPerformanceSimpleDto(performanceSimpleDto, convertToArtistSimpleDto(artistListDto)) : null;

                if (allAssociations.contains(artistPerformanceSimpleDto)) {
                    duplicateAssociations.add(artistPerformanceSimpleDto);
                } else if (Objects.nonNull(artistPerformanceSimpleDto)) {
                    allAssociations.add(artistPerformanceSimpleDto);
                }
            }
        }

        return duplicateAssociations;
    }

    private RoleChangeCommand makeRoleChangeCommandExceptDuplicateAssociations(RoleDto originalRole, RoleDto newRoleDto, Set<ArtistPerformanceSimpleDto> duplicateAssociations) {
        var artistPerformanceSimpleDtos = collectArtistPerformanceJoinsForSelectedRow();
        artistPerformanceSimpleDtos.removeAll(duplicateAssociations);

        return RoleChangeCommand.builder()
                .withOriginalRole(convertToRoleSimpleDto(originalRole))
                .withNewRole(convertToRoleSimpleDto(newRoleDto))
                .withArtistPerformanceSimpleDtos(artistPerformanceSimpleDtos)
                .build();
    }

    private List<ArtistPerformanceSimpleDto> collectArtistPerformanceJoinsForSelectedRow() {
        var artistPerformanceSimpleDtos = new LinkedList<ArtistPerformanceSimpleDto>();

        var selectedRow = tblPerformances.getSelectedRow();
        var firstPerformanceColumn = findFirstPerformanceColumn();

        for (var c = 0; c < performances.size(); c++) {
            var performanceSimpleDto = performances.get(c);
            var artistDto = (ArtistListDto) tblPerformances.getValueAt(selectedRow, firstPerformanceColumn + c);

            if (Objects.nonNull(performanceSimpleDto.getNaturalId()) && Objects.nonNull(artistDto)) {
                artistPerformanceSimpleDtos.add(
                        makeArtistPerformanceSimpleDto(performanceSimpleDto, convertToArtistSimpleDto(artistDto))
                );
            }
        }

        return artistPerformanceSimpleDtos;
    }

    private ArtistPerformanceSimpleDto makeArtistPerformanceSimpleDto(@NonNull PerformanceSimpleDto performanceSimpleDto, ArtistSimpleDto artistSimpleDto) {
        return ArtistPerformanceSimpleDto.builder()
                .withPerformanceSimpleDto(performanceSimpleDto)
                .withArtistSimpleDto(artistSimpleDto)
                .build();
    }

    private ArtistSimpleDto convertToArtistSimpleDto(ArtistListDto artistDto) {
        return ArtistSimpleDto.builder()
                .withNaturalId(artistDto.getNaturalId())
                .build();
    }

    private boolean isOnlyRowWithRole(RoleDto originalRole) {
        var selectedRow = tblPerformances.getSelectedRow();
        var rowCount = retrieveModel()
                .getRowCount();

        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            if (r != selectedRow && foundOriginalRoleInRow(r, originalRole)) {
                return false;
            }
        }

        return true;
    }

    private boolean foundOriginalRoleInRow(int row, RoleDto originalRole) {
        return tblPerformances.getValueAt(row, COLUMN_ROLE) instanceof RoleDto roleDto
                && Objects.equals(roleDto, originalRole);
    }

    private int findFirstRoleRow() {
        return lastConductorRow + 1;
    }

    private int findFirstPerformanceColumn() {
        return COLUMN_ROLE + 1;
    }

    private void deleteRole(RoleDto roleDto) {
        getBean(RoleCommandService.class)
                .delete(makeRoleCommand(roleDto));
    }

    private void createPerformance() {
        addPerformanceColumn();
        performances.add(PerformanceSimpleDto.builder()
                .build());
    }

    private int addPerformanceColumn() {
        var model = retrieveModel();
        var newColumnIndex = model.getColumnCount();

        model.addColumn(newColumnIndex);
        actualizeTableShow();

        return newColumnIndex;
    }

    private void deletePerformance() {

    }

    private void addConductor() {
        retrieveModel()
                .insertRow(++lastConductorRow, new Vector<>());
        tblPerformances.setValueAt(CAPTION_CONDUCTOR, lastConductorRow, COLUMN_ROLE);

        operaTableCellRenderer.setLastConductorRow(lastConductorRow);
    }

    private void actualizeTableShow() {
        var columnModel = tblPerformances.getColumnModel();
        var columnCount = columnModel.getColumnCount();

        for (var c = 0; c < columnCount; c++) {
            var column = columnModel.getColumn(c);

            column.setPreferredWidth(calculateColumnWidth(c));
            column.setCellRenderer(operaTableCellRenderer);
        }
    }

    private int calculateColumnWidth(int column) {
        return switch (column) {
            case COLUMN_ROLE -> 140;
            default -> 230;
        };
    }

    private Optional<LocationDto> createLocation() {
        var dialog = new LocationDialog(owner, TITLE_CREATE_LOCATION_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = getBean(LocationCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private Optional<ArtistListDto> createConductor() {
        var dialog = new ArtistDialog(owner, TITLE_CREATE_CONDUCTOR_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = getBean(ArtistCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private Optional<ArtistListDto> createSinger() {
        var dialog = new ArtistDialog(owner, TITLE_CREATE_SINGER_DIALOG);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = getBean(ArtistCommandService.class)
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

    private void singerChanged(ArtistListDto originalSinger, ArtistListDto newSinger) {
        var performanceSimpleDto = findSelectedPerformance();

        if (Objects.isNull(performanceSimpleDto.getNaturalId())) {
            tryToSavePerformance();
        } else {
            updateOrDeleteArtistPerformanceRoleJoin(originalSinger, newSinger);
        }
    }

    private void updateOrDeleteArtistPerformanceRoleJoin(ArtistListDto originalSinger, ArtistListDto newSinger) {
        if (Objects.nonNull(newSinger)) {
            updateArtistPerformanceRoleJoin(originalSinger, newSinger);
        } else {
            deleteArtistPerformanceRoleJoin(originalSinger);
        }
    }

    private void updateArtistPerformanceRoleJoin(ArtistListDto originalSinger, ArtistListDto newSinger) {
        getBean(ArtistPerformanceRoleJoinCommandService.class)
                .saveArtistRoleJoin(
                        makeArtisRoleChangeCommand(originalSinger, newSinger)
                );
    }

    private ArtistRoleChangeCommand makeArtisRoleChangeCommand(ArtistListDto originalSinger, ArtistListDto newSinger) {
        var originalSingerSimpleDto = Objects.nonNull(originalSinger) ? convertToArtistSimpleDto(originalSinger) : null;

        return ArtistRoleChangeCommand.builder()
                .withOriginalArtist(originalSingerSimpleDto)
                .withNewArtist(convertToArtistSimpleDto(newSinger))
                .withRolePerformanceSimpleDto(
                        makeRolePerformanceSimpleDtoFromSelectedRow()
                ).build();
    }

    private RolePerformanceSimpleDto makeRolePerformanceSimpleDtoFromSelectedRow() {
        return RolePerformanceSimpleDto.builder()
                .withPerformanceSimpleDto(findSelectedPerformance())
                .withRoleSimpleDto(obtainSelectedRoleSimpleDto())
                .build();
    }

    private void deleteArtistPerformanceRoleJoin(ArtistListDto originalSinger) {
        var deleteCommand = ArtistPerformanceRoleJoinDeleteCommand.builder()
                .withOriginalArtist(convertToArtistSimpleDto(originalSinger))
                .withPerformance(findSelectedPerformance())
                .withRole(obtainSelectedRoleSimpleDto())
                .build();

        getBean(ArtistPerformanceRoleJoinCommandService.class)
                .delete(deleteCommand);
    }

    private RoleSimpleDto obtainSelectedRoleSimpleDto() {
        var selectedRow = tblPerformances.getSelectedRow();
        var roleDto = getRoleAt(selectedRow);

        return convertToRoleSimpleDto(roleDto);
    }

    private RoleSimpleDto convertToRoleSimpleDto(RoleDto roleDto) {
        return RoleSimpleDto.builder()
                .withNaturalId(roleDto.getNaturalId())
                .build();
    }

    private PerformanceSimpleDto findSelectedPerformance() {
        return performances.get(calculateSelectedPerformanceIndex());
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

            getBean(PlayQueryService.class)
                    .getPlay(playListDto.getNaturalId())
                    .ifPresent(p -> {
                        var performanceSummary = getBean(PerformanceQueryService.class)
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
                getBean(PlayQueryService.class)
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
        btAddConductor.setEnabled(enabled);
        btRefresh.setEnabled(enabled);

        tblPerformances.setEnabled(enabled);
    }

    private void tryToSavePerformance() {

    }

    private void clearCell(int row, int column) {
        tblPerformances.setValueAt(null, row, column);
    }
}
