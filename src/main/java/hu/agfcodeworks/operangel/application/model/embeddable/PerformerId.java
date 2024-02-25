package hu.agfcodeworks.operangel.application.model.embeddable;

import hu.agfcodeworks.operangel.application.model.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@MappedSuperclass
@Embeddable
public class PerformerId extends FunctionalityId {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
}
