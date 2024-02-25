package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.embeddable.FunctionalityId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="functionality_type",
        discriminatorType = DiscriminatorType.INTEGER)
@SuperBuilder(setterPrefix = "with")
@DiscriminatorValue("conductor_performance")
@Entity
public class ConductorRole extends ColleagueRole {

    @EmbeddedId
    private FunctionalityId id;
}
