package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.embeddable.CastId;
import hu.agfcodeworks.operangel.application.model.enums.Functionality;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "colleague_role")
public class ColleagueRole implements Serializable {

    @EmbeddedId
    private CastId id;

    @Column(name = "functionality", nullable = false)
    private Functionality functionality;
}
