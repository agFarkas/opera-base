package hu.agfcodeworks.operangel.application.service.cache;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.service.query.service.RoleQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class RoleCache extends AbstractCache<List<RoleDto>> {

    private final RoleQueryService roleQueryService;

    @Override
    public List<RoleDto> getDefault() {
        return new LinkedList<>();
    }

    @Override
    protected void fillCache() {
        roleQueryService.getAllRoles()
                .forEach(r -> {
                    var playNaturalId = r.getPlayNaturalId();
                    var roleDtos = getOPutDefault(playNaturalId, LinkedList::new);

                    roleDtos.add(r);
                });
    }
}
