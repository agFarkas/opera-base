package hu.agfcodeworks.operangel.application.ui.components.custom.uidto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.Icon;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ListItemWrapper<DTO> {

    @EqualsAndHashCode.Include
    private DTO dto;

    private Icon icon;

    public ListItemWrapper(DTO dto) {
        this.dto = dto;
    }

    public static <DTO> ListItemWrapper<DTO> of(DTO dto) {
        return new ListItemWrapper<>(dto);
    }

    public static <DTO> ListItemWrapper<DTO> ofEmpty() {
        return new ListItemWrapper<>();
    }

    public boolean isToAddNew() {
        return Objects.isNull(dto);
    }
}
