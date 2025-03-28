package hu.agfcodeworks.operangel.application.ui.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.Icon;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ListItemWrapper<DTO> {

    @EqualsAndHashCode.Include
    private DTO dto;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.PRIVATE)
    private boolean toAddNew;

    @Setter(AccessLevel.PRIVATE)
    private boolean empty;

    private Icon icon;

    public ListItemWrapper(DTO dto) {
        this.dto = dto;
    }

    public static <DTO> ListItemWrapper<DTO> of(DTO dto) {
        return new ListItemWrapper<>(dto);
    }

    public static <DTO> ListItemWrapper<DTO> ofEmpty() {
        var dtoListItemWrapper = new ListItemWrapper<DTO>();
        dtoListItemWrapper.setEmpty(true);

        return dtoListItemWrapper;
    }

    public static <DTO> ListItemWrapper<DTO> ofToAddNew() {
        var dtoListItemWrapper = new ListItemWrapper<DTO>();
        dtoListItemWrapper.setToAddNew(true);

        return dtoListItemWrapper;
    }
}
