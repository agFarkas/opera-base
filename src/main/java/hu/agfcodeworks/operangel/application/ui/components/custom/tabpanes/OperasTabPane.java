package hu.agfcodeworks.operangel.application.ui.components.custom.tabpanes;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistRoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.LocationSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayCommand;
import hu.agfcodeworks.operangel.application.dto.command.PlayPerformanceChangeCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleChangeCommand;
import hu.agfcodeworks.operangel.application.dto.state.PerformanceStateDto;
import hu.agfcodeworks.operangel.application.dto.state.PlayStateDto;
import hu.agfcodeworks.operangel.application.exception.ValidationException;
import hu.agfcodeworks.operangel.application.service.cache.global.ArtistCache;
import hu.agfcodeworks.operangel.application.service.commmand.service.ArtistCommandService;
import hu.agfcodeworks.operangel.application.service.commmand.service.LocationCommandService;
import hu.agfcodeworks.operangel.application.service.commmand.service.PlayCommandService;
import hu.agfcodeworks.operangel.application.service.commmand.service.PlayPerformanceCommandService;
import hu.agfcodeworks.operangel.application.service.query.service.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.PlayQueryService;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledList;
import hu.agfcodeworks.operangel.application.ui.dialog.ArtistDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.LocationDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.PlayDialog;
import hu.agfcodeworks.operangel.application.ui.dialog.enums.ArtistPosition;
import hu.agfcodeworks.operangel.application.ui.editor.ArtistEditor;
import hu.agfcodeworks.operangel.application.ui.editor.DateEditor;
import hu.agfcodeworks.operangel.application.ui.editor.LocationEditor;
import hu.agfcodeworks.operangel.application.ui.editor.OperationOnChangingValue;
import hu.agfcodeworks.operangel.application.ui.editor.RoleEditor;
import hu.agfcodeworks.operangel.application.ui.renderer.OperaTableCellRenderer;
import hu.agfcodeworks.operangel.application.ui.util.DialogUtil;
import hu.agfcodeworks.operangel.application.util.ContextUtil;
import hu.agfcodeworks.operangel.application.util.PlayStateUtil;
import hu.agfcodeworks.operangel.application.util.ValidationUtil;
import hu.agfcodeworks.operangel.application.validation.PlayPerformanceChangeValidator;
import hu.agfcodeworks.operangel.application.validation.error.PlayPerformanceValidationErrorDto;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.COLUMN_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_ARTIST;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_FIRST_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.INVALID_OPERATION;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.LIST_LINE_BREAK;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.RETURN_AND_LINE_BREAK;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.SYSTEM_ERROR;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.roleChangeOperationOptions;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.yesNoOptions;
import static hu.agfcodeworks.operangel.application.ui.dto.DialogStatus.OK;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.playDtoByTitleComparator;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.artistTextProvider;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.composerPlayTextProvider;

public class OperasTabPane extends AbstractCustomTabPane {

    private static final String CAPTION_CONDUCTOR = "Karmester";

    private static final String CAPTION_DATE = "Dátum";

    private static final String CAPTION_LOCATION = "Helyszín";

    private static final String ROLE_CHANGE_QUESTION_PATTERN = "A '%s' szerep megnevezését átírva a következőre: '%s'.\r\nMódosítod a leírást a meglévő szerepnél, vagy új szerepet hozol létre az új leírással?";

    private static final String OPERA_DELETE_QUESTION_PATTERN = "Biztosan törlöd ezt az operát: '%s'?";

    private static final String CHOOSING_OPERATION_TITLE = "Műveletválasztás";

    private static final String ARTIST_PERFORMANCE_ASSOCIATION_WARNING_PATTERN = "'%s' szerepben többször szerepelnek a következő énekesek: %s%s%sEzek visszavonásra kerülnek.";

    private static final String DUPLICATE_ARTIST_PERFORMANCE_ASSOCIATIONS_WARNING_TITLE = "Többszörös szerep-énekes párosítás";

    private static final String ARTIST_PERFORMANCE_TEXT_PATTERN = "%s-i előadásban %s";

    private static final String NO_PERFORMANCE_SELECTED_ERROR_MESSAGE = "Nincs előadás kiválasztva.";

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final JLabeledList<PlayListDto> lsOpera = new JLabeledList<>("Operák");

    private final JButton btCreateOpera = new JButton("Új opera...");

    private final JButton btUpdateOpera = new JButton("Módosítás...");

    private final JButton btDeleteOpera = new JButton("Törlés");

    private final JButton btRefresh = new JButton("Frissítés");

    private final JButton btCreatePerformance = new JButton("Új előadás");

    private final JButton btDeletePerformance = new JButton("Előadás törlése");

    private final JButton btAddConductor = new JButton("Karmester hozzáadása");

    private final JButton btSave = new JButton("Változtatások mentése");

    private int lastConductorRow = ROW_FIRST_CONDUCTOR;

    private final OperaTableCellRenderer operaTableCellRenderer = new OperaTableCellRenderer(ROW_FIRST_CONDUCTOR);

    private PlayListDto selectedPlay;

    private List<PerformanceSimpleDto> performances;

    private PlayStateDto originalState;

    private PlayStateDto newState;

    private boolean stateChanged;

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
                            readRoleDtosFromCache()
                    );
                }
            }

            return switch (row) {
                case ROW_DATE -> new DateEditor((originalDate, newDate) -> changeDate(originalDate, newDate));
                case ROW_LOCATION -> new LocationEditor(
                        () -> createLocation(),
                        (originalLocation, newLocation) -> changeLocation(originalLocation, newLocation)
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
                            (originalSinger, newSinger) -> changeSinger(originalSinger, newSinger)
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

    private List<RoleDto> readRoleDtosFromCache() {
        return newState.getRoles();
    }

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
        var rowCount = retrieveModel()
                .getRowCount();

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
        var value = readRoleDto(row);

        if (value instanceof RoleDto r) {
            return Objects.equals(r.getNaturalId(), roleDto.getNaturalId());
        }

        return false;
    }

    private int getRoleUpdateOperationIndex(RoleDto originalRole, String newRoleDescription) {
        return DialogUtil.showCustomQuestionDialog(
                owner,
                CHOOSING_OPERATION_TITLE,
                ROLE_CHANGE_QUESTION_PATTERN.formatted(originalRole.getDescription(), newRoleDescription),
                roleChangeOperationOptions,
                0
        );
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

        var performanceDtos = performanceSummaryDtoOpt.map(PerformanceSummaryDto::getPerformances)
                .orElseGet(Collections::emptyList);

        fillPerformances(performanceDtos);

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
        return perfSumDto.getMaxRoleCounts()
                .getOrDefault(roleDto.getNaturalId(), 1);
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

        performances.add(createPerformanceSimpleDto(performance.getNaturalId()));

        var conductors = performance.getConductors();
        if (!CollectionUtils.isEmpty(conductors)) {
            fillConductors(performanceColumnIndex, conductors);
        }


    }

    private void fillConductors(int performanceColumnIndex, List<ArtistListDto> conductors) {
        for (var i = 0; i < conductors.size(); i++) {
            var conductor = conductors.get(i);
            tblPerformances.setValueAt(conductor, ROW_FIRST_CONDUCTOR + i, performanceColumnIndex);
        }
    }

    private PerformanceSimpleDto createPerformanceSimpleDto(UUID naturalId) {
        return PerformanceSimpleDto.builder()
                .withNaturalId(naturalId)
                .build();
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
            var roleValue = readRoleDto(r);

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

        lastConductorRow = calculateLastConductorRow(maxNumOfConductors);
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

    private int calculateLastConductorRow(Integer maxNumOfConductors) {
        if (maxNumOfConductors > 0) {
            return ROW_FIRST_CONDUCTOR - 1 + maxNumOfConductors;
        }

        return ROW_FIRST_CONDUCTOR;
    }

    private int calculateInitialRowCount(int maxNumOfRoles, Integer maxNumOfConductors) {
        var actuallyNeededNumOfConductorRows = maxNumOfConductors > 0 ? maxNumOfConductors : 1;

        return 2 + actuallyNeededNumOfConductorRows + maxNumOfRoles + 1;
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
        var roleCountsSumInPerformances = roleCountsFromPerformanceSummeries.values()
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

        buildEventListenersForPerformanceCrudButtons();
        addButtonsToPerformanceCrudPanel(performanceCrudPanel);

        return performanceCrudPanel;
    }

    private void buildEventListenersForPerformanceCrudButtons() {
        btCreatePerformance.addActionListener(e -> createPerformance());
        btDeletePerformance.addActionListener(e -> deletePerformance());
        btAddConductor.addActionListener(e -> addConductor());
        btSave.addActionListener(e -> executor.submit(this::saveChanges));
        btRefresh.addActionListener(e -> refreshDetails());
    }

    private void addButtonsToPerformanceCrudPanel(JPanel performanceCrudPanel) {
        performanceCrudPanel.add(btCreatePerformance);
        performanceCrudPanel.add(btDeletePerformance);
        performanceCrudPanel.add(btAddConductor);
        performanceCrudPanel.add(btSave);
        performanceCrudPanel.add(btRefresh);
    }

    private void saveChanges() {
        try {
            btSave.setEnabled(false);

            var playPerformanceChangeCommand = createPlayPerformanceChangeCommand();

            validatePlayPerformanceChange(playPerformanceChangeCommand);
            executeSaveProcedure(playPerformanceChangeCommand);
        } catch (ValidationException ex) {
            showErrorMessageAndMarkers(ex);
        } catch (Exception ex) {
            showSystemErrorMessage(ex);
        } finally {
            btSave.setEnabled(true);
        }
    }

    private void executeSaveProcedure(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        ContextUtil.getBean(PlayPerformanceCommandService.class)
                .save(playPerformanceChangeCommand);

        updateOriginalState(playPerformanceChangeCommand.getNewPlayState());
        updateOriginalState(newState);
    }


    private void showErrorMessageAndMarkers(ValidationException ex) {
        DialogUtil.showWarningMessageByValidation(owner, ex);
        markInvalidPerformances(ValidationUtil.getGenericErrorDto(ex));
    }

    private void validatePlayPerformanceChange(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        ContextUtil.getBean(PlayPerformanceChangeValidator.class)
                .validate(playPerformanceChangeCommand);
    }

    private void markInvalidPerformances(List<PlayPerformanceValidationErrorDto> errorDtos) {
        //TODO implement!
    }

    private void showSystemErrorMessage(Exception ex) {
        DialogUtil.showErrorMessageByException(
                owner,
                SYSTEM_ERROR,
                ex
        );
    }

    private List<PerformanceSimpleDto> makePerformanceSimpleDtos(PlayStateDto playStateDto) {
        return playStateDto.getPerformanceStateDtos()
                .stream()
                .map(PerformanceStateDto::getNaturalId)
                .map(this::createPerformanceSimpleDto)
                .toList();
    }

    private PlayPerformanceChangeCommand createPlayPerformanceChangeCommand() {
        this.newState.clearRoles();

        collectRoleDtosFromTable()
                .forEach(this.newState::store);

        var performanceStateDtos = this.newState.getPerformanceStateDtos();
        performanceStateDtos.clear();
        performanceStateDtos.addAll(collectPerformanceStateDtosFromTable());

        return PlayPerformanceChangeCommand.builder()
                .withPlayNaturalId(this.selectedPlay.getNaturalId())
                .withOriginalPlayState(this.originalState)
                .withNewPlayState(this.newState)
                .build();
    }

    private void changeDate(LocalDate originalDate, LocalDate newDate) {
        markStateChanged();
    }

    private void changeLocation(LocationDto originalLocation, LocationDto newLocation) {
        markStateChanged();
    }

    private void changeConductor(ArtistListDto originalConductor, ArtistListDto newConductor) {
        markStateChanged();
    }

    private void changeSinger(ArtistListDto originalSinger, ArtistListDto newSinger) {
        markStateChanged();
    }

    private boolean hasValue(int row, int column) {
        return Objects.nonNull(tblPerformances.getValueAt(row, column));
    }

    private int calculateSelectedPerformanceIndex() {
        return tblPerformances.getSelectedColumn() - 1;
    }

    private void createOpera() {
        var dialog = new PlayDialog(owner);

        if (dialog.getDialogStatus() == OK) {
            var playListDto = ContextUtil.getBean(PlayCommandService.class)
                    .saveOpera(makePlayCommand(dialog.getValue()));

            putPlayIntoList(playListDto);
        }
    }

    private void updateOpera() {
        var dialog = new PlayDialog(owner, selectedPlay);

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

        return DialogUtil.showCustomQuestionDialog(
                owner,
                CHOOSING_OPERATION_TITLE,
                OPERA_DELETE_QUESTION_PATTERN.formatted(composerPlayTextProvider.provide(selectedPlay)),
                yesNoOptions,
                1
        );
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
        markStateChanged();

        if (Objects.nonNull(roleDto.getNaturalId())) {
            return roleDto;
        }

        return newState.store(roleDto);
    }

    private RoleDto updateRole(RoleDto roleDto) {
        var newRoleDto = newState.store(roleDto);

        overwriteAllOccurencesOf(newRoleDto);

        return newRoleDto;
    }

    private void overwriteAllOccurencesOf(RoleDto newRoleDto) {
        var selectedRow = tblPerformances.getSelectedRow();
        var rowCount = retrieveModel()
                .getRowCount();

        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var originalRowDto = readRoleDto(r);

            if (r != selectedRow && originalRowDto instanceof RoleDto roleDto) {
                if (Objects.equals(newRoleDto.getNaturalId(), roleDto.getNaturalId())) {
                    tblPerformances.setValueAt(newRoleDto, r, COLUMN_ROLE);
                }
            }
        }
    }

    private RoleDto readRoleDto(int row) {
        return (RoleDto) tblPerformances.getValueAt(row, COLUMN_ROLE);
    }

    private void changeRole(RoleDto originalRole, RoleDto newRoleDto) {
        updateArtistRoleSimpleDtosInOriginalState();
    }

    private void updateArtistRoleSimpleDtosInOriginalState() {
        this.originalState.getPerformanceStateDtos()
                .forEach(performanceStateDto -> {
                    var performanceSimpleDto = findPerformanceSimpleDtoBy(
                            performanceStateDto.getNaturalId()
                    );
                    var performanceColumn = findPerformanceColumnBy(performanceSimpleDto);

                    if (performanceColumn > -1) {
                        var artistRoleSimpleDtos = createPerformanceStateDto(performanceColumn, performanceSimpleDto)
                                .getArtistRoleSimpleDtos();
                        performanceStateDto.setArtistRoleSimpleDtos(artistRoleSimpleDtos);
                    }

                });
    }

    private void clearDuplicateAssociationInPerformanceColumn(RoleChangeCommand roleChangeCommand, int performanceColumn, ArtistPerformanceSimpleDto artistPerformanceSimpleDto) {
        var artists = new HashSet<ArtistListDto>();

        var rowCount = tblPerformances.getRowCount();
        for (var r = findFirstRoleRow(); r < rowCount; r++) {
            var roleDto = readRoleDto(r);

            if (Objects.isNull(roleDto)) {
                continue;
            }

            var actRoleNaturalId = roleDto.getNaturalId();
            if (Objects.equals(actRoleNaturalId, roleChangeCommand.getOriginalRole().getNaturalId()) || Objects.equals(actRoleNaturalId, roleChangeCommand.getNewRole().getNaturalId())) {
                var artistDto = readArtistListDtoFromTable(r, performanceColumn);

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

    private void showWarningOfClearingDuplicateAssociations(RoleDto roleDto, Set<ArtistPerformanceSimpleDto> duplicateAssociations) {
        var associations = collectAssociationsByLocalDate(duplicateAssociations);
        var artistNamesByPerformanceDateText = textifyAssociations(associations);

        DialogUtil.showWarningMessage(owner,
                DUPLICATE_ARTIST_PERFORMANCE_ASSOCIATIONS_WARNING_TITLE,
                ARTIST_PERFORMANCE_ASSOCIATION_WARNING_PATTERN.formatted(roleDto.getDescription(), LIST_LINE_BREAK, artistNamesByPerformanceDateText, RETURN_AND_LINE_BREAK)
        );
    }

    private String textifyAssociations(HashMap<LocalDate, Set<ArtistListDto>> associations) {
        return associations.keySet()
                .stream()
                .map(date -> ARTIST_PERFORMANCE_TEXT_PATTERN.formatted(
                        dateFormatter.format(date), joinArtistNames(associations.get(date))
                )).collect(Collectors.joining(LIST_LINE_BREAK));
    }

    private String joinArtistNames(Set<ArtistListDto> artistListDtos) {
        return artistListDtos.stream()
                .map(artistTextProvider::provide)
                .collect(Collectors.joining(" , "));
    }

    private HashMap<LocalDate, Set<ArtistListDto>> collectAssociationsByLocalDate(Set<ArtistPerformanceSimpleDto> duplicateAssociations) {
        var associations = new HashMap<LocalDate, Set<ArtistListDto>>();

        duplicateAssociations.forEach(artistPerformanceSimpleDto -> {
            var date = readDateBy(artistPerformanceSimpleDto.getPerformanceSimpleDto());
            if (Objects.isNull(date)) {
                return;
            }

            if (!associations.containsKey(date)) {
                associations.put(date, new HashSet<>());
            }

            associations.get(date)
                    .add(findArtistListDtoBy(artistPerformanceSimpleDto.getArtistSimpleDto()));
        });

        return associations;
    }

    private ArtistListDto findArtistListDtoBy(ArtistSimpleDto artistSimpleDto) {
        return ContextUtil.getBean(ArtistCache.class)
                .get(artistSimpleDto.getNaturalId());
    }

    private LocalDate readDateBy(PerformanceSimpleDto performanceSimpleDto) {
        return (LocalDate) tblPerformances.getValueAt(ROW_DATE, findPerformanceColumnBy(performanceSimpleDto));
    }

    private int findPerformanceColumnBy(PerformanceSimpleDto performanceSimpleDto) {
        return findFirstPerformanceColumn() + performances.indexOf(performanceSimpleDto);
    }

    private PerformanceSimpleDto findPerformanceSimpleDtoBy(UUID naturalId) {
        return performances.stream()
                .filter(psd -> Objects.equals(psd.getNaturalId(), naturalId))
                .findFirst()
                .orElse(null);
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
            var roleDto = readRoleDto(r);

            if (Objects.equals(roleDto, originalRole) || Objects.equals(roleDto, newRoleDto)) {
                var artistListDto = readArtistListDtoFromTable(r, findFirstPerformanceColumn() + performanceIndex);
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
            var artistDto = readArtistListDtoFromTable(selectedRow, firstPerformanceColumn + c);

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
        newState.delete(roleDto);
    }

    private void createPerformance() {
        addPerformanceColumn();
        performances.add(makeNewPerformanceSingleDto());
    }

    private static PerformanceSimpleDto makeNewPerformanceSingleDto() {
        return PerformanceSimpleDto.builder()
                .withNaturalId(UUID.randomUUID())
                .build();
    }

    private int addPerformanceColumn() {
        var model = retrieveModel();
        var newColumnIndex = model.getColumnCount();

        model.addColumn(newColumnIndex);
        actualizeTableShow();

        return newColumnIndex;
    }

    private void deletePerformance() {
        var columnModel = retrieveColumnModel();
        var selectedColumn = tblPerformances.getSelectedColumn();
        var selectedPerformanceIndex = calculateSelectedPerformanceIndex();

        if (selectedColumn > 0) {
            markStateChanged();

            columnModel.removeColumn(
                    columnModel.getColumn(selectedColumn)
            );

            performances.remove(selectedPerformanceIndex);
        } else {
            showNoPerformanceSelectedErrorMessage();
        }
    }

    private void showNoPerformanceSelectedErrorMessage() {
        DialogUtil.showWarningMessage(owner, INVALID_OPERATION, NO_PERFORMANCE_SELECTED_ERROR_MESSAGE);
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
        var dialog = new LocationDialog(owner);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = ContextUtil.getBean(LocationCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private Optional<ArtistListDto> createConductor() {
        var dialog = new ArtistDialog(owner, ArtistPosition.CONDUCTOR);

        if (dialog.getDialogStatus() == OK) {
            var savedValue = ContextUtil.getBean(ArtistCommandService.class)
                    .save(dialog.getValue());

            return Optional.of(savedValue);
        }

        return Optional.empty();
    }

    private Optional<ArtistListDto> createSinger() {
        var dialog = new ArtistDialog(owner, ArtistPosition.SINGER);

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

    private RoleSimpleDto convertToRoleSimpleDto(RoleDto roleDto) {
        return RoleSimpleDto.builder()
                .withNaturalId(roleDto.getNaturalId())
                .build();
    }

    private void prepareTblPerformances() {
        tblPerformances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private DefaultTableModel retrieveModel() {
        return (DefaultTableModel) tblPerformances.getModel();
    }

    private DefaultTableColumnModel retrieveColumnModel() {
        return (DefaultTableColumnModel) tblPerformances.getColumnModel();
    }

    private void prepareLsOpera() {
        lsOpera.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lsOpera.setTextProvider(composerPlayTextProvider);
        lsOpera.setItemComparator(playDtoByTitleComparator);
        lsOpera.addListSelectionListener(this::handleOperaSelectionChange);
    }

    private void handleOperaSelectionChange(ListSelectionEvent event) {
        this.refreshDetails();
    }

    private void refreshDetails() {
        var playListDtoOpt = lsOpera.getSelectedItem();

        if (playListDtoOpt.isPresent()) {
            var playListDto = playListDtoOpt.get();

            setSelectedPlay(playListDto);

            ContextUtil.getBean(PlayQueryService.class)
                    .getPlay(playListDto.getNaturalId())
                    .ifPresent(this::loadPerformances);

            setUpOriginalState();
            setOperaDatasEnabled(true);
        }
    }

    private void loadPerformances(PlayDetailedDto playDetailedDto) {
        var performanceSummary = ContextUtil.getBean(PerformanceQueryService.class)
                .getPerformancesForPlay(selectedPlay);

        refreshDetails(playDetailedDto, performanceSummary);
    }

    private void setSelectedPlay(PlayListDto playListDto) {
        this.selectedPlay = playListDto;
    }

    private void setUpOriginalState() {
        var playState = createPlayStateFromTable();
        updateOriginalState(playState);
    }


    private void updateOriginalState(PlayStateDto playState) {
        this.originalState = playState;
        this.newState = cloneState(playState);

        this.stateChanged = false;

    }

    private static PlayStateDto cloneState(PlayStateDto playState) {
        return PlayStateUtil.clone(playState);
    }

    private PlayStateDto createPlayStateFromTable() {
        return PlayStateDto.builder()
                .withPlayNaturalId(selectedPlay.getNaturalId())
                .withPerformanceStateDtos(collectPerformanceStateDtosFromTable())
                .withRoles(collectRoleDtosFromTable())
                .build();
    }

    private List<RoleDto> collectRoleDtosFromTable() {
        var roles = new LinkedList<RoleDto>();

        for (int row = findFirstRoleRow(); row < tblPerformances.getRowCount(); row++) {
            var value = tblPerformances.getValueAt(row, COLUMN_ROLE);

            if (Objects.nonNull(value) && !roles.contains(value) && value instanceof RoleDto roleDto) {
                roles.add(roleDto);
            }
        }

        return roles;
    }

    private List<PerformanceStateDto> collectPerformanceStateDtosFromTable() {
        var columnCount = tblPerformances.getColumnCount();
        var performanceStateDtos = new LinkedList<PerformanceStateDto>();
        var firstPerformanceColumn = findFirstPerformanceColumn();

        for (var p = firstPerformanceColumn; p < columnCount; p++) {
            performanceStateDtos.add(
                    createPerformanceStateDto(p, performances.get(p - firstPerformanceColumn))
            );
        }

        return performanceStateDtos;
    }

    private PerformanceStateDto createPerformanceStateDto(int column, PerformanceSimpleDto performanceSimpleDto) {
        return PerformanceStateDto.builder()
                .withNaturalId(performanceSimpleDto.getNaturalId())
                .withDate(readDateFromTable(column))
                .withLocation(readLocationFromTable(column))
                .withConductors(collectConductorsFromTable(column))
                .withArtistRoleSimpleDtos(collectArtistRoleDtosFromTable(column))
                .build();
    }

    private LocalDate readDateFromTable(int column) {
        return (LocalDate) tblPerformances.getValueAt(ROW_DATE, column);
    }

    private LocationSimpleDto readLocationFromTable(int column) {
        var locationDto = readLocationDto(column);

        if (Objects.isNull(locationDto)) {
            return null;
        }

        return convertToLocationSimpleDto(
                locationDto
        );
    }

    private LocationSimpleDto convertToLocationSimpleDto(LocationDto locationDto) {
        return LocationSimpleDto.builder()
                .withNaturalId(locationDto.getNaturalId())
                .build();
    }

    private LocationDto readLocationDto(int column) {
        return (LocationDto) tblPerformances.getValueAt(ROW_LOCATION, column);
    }

    private List<ArtistSimpleDto> collectConductorsFromTable(int column) {
        var conductors = new LinkedList<ArtistSimpleDto>();

        for (int conductorRow = ROW_FIRST_CONDUCTOR; conductorRow < findFirstRoleRow(); conductorRow++) {
            readArtistSimpleDtoFromTable(conductorRow, column)
                    .ifPresent(conductors::add);
        }

        return conductors;
    }

    private Optional<ArtistSimpleDto> readArtistSimpleDtoFromTable(int row, int column) {
        return Optional.ofNullable(
                readArtistListDtoFromTable(row, column)
        ).map(this::convertToArtistSimpleDto);
    }

    private List<ArtistRoleSimpleDto> collectArtistRoleDtosFromTable(int column) {
        var rowCount = tblPerformances.getRowCount();
        var artistRoleSimpleDtos = new LinkedList<ArtistRoleSimpleDto>();

        for (int artistRow = findFirstRoleRow(); artistRow < rowCount; artistRow++) {
            var aRow = artistRow;
            readArtistSimpleDtoFromTable(artistRow, column)
                    .map(a -> makeArtistRoleSimpleDtoFromTable(a, aRow))
                    .ifPresent(artistRoleSimpleDtos::add);
        }
        return artistRoleSimpleDtos;
    }

    private ArtistListDto readArtistListDtoFromTable(int row, int column) {
        return (ArtistListDto) tblPerformances.getValueAt(row, column);
    }

    private ArtistRoleSimpleDto makeArtistRoleSimpleDtoFromTable(ArtistSimpleDto artistSimpleDto, int artistRow) {
        return ArtistRoleSimpleDto.builder()
                .withArtistSimpleDto(artistSimpleDto)
                .withRoleSimpleDto(convertToRoleSimpleDto(
                        readRoleDto(artistRow)
                )).build();
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
        btAddConductor.setEnabled(enabled);

        btSave.setEnabled(enabled);
        btRefresh.setEnabled(enabled);

        tblPerformances.setEnabled(enabled);
    }

    private void clearCell(int row, int column) {
        tblPerformances.setValueAt(null, row, column);
    }

    private void markStateChanged() {
        stateChanged = true;
    }
}
