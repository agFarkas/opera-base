package hu.agfcodeworks.operangel.application.model.embeddable;

import hu.agfcodeworks.operangel.application.model.Colleague;
import hu.agfcodeworks.operangel.application.model.Performance;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Embeddable
public class FunctionalityId implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performance_id", referencedColumnName = "id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "colleague_id", referencedColumnName = "id", nullable = false)
    private Colleague colleague;

}
