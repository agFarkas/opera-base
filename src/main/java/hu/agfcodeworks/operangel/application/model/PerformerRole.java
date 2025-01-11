package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.embeddable.PerformerId;
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

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "functionality_type",
        discriminatorType = DiscriminatorType.INTEGER)
@SuperBuilder(setterPrefix = "with")
@DiscriminatorValue("performer_performance_role")
@Entity
public class PerformerRole extends ArtistRole {

    @EmbeddedId
    private PerformerId id;
}
