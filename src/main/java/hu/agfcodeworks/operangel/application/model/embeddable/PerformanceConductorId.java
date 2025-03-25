package hu.agfcodeworks.operangel.application.model.embeddable;

import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.Performance;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class PerformanceConductorId implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Artist.class)
    @JoinColumn(name = "conductor_id", referencedColumnName = "id")
    private Artist conductor;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Performance.class)
    @JoinColumn(name = "performance_id", referencedColumnName = "id")
    private Performance performance;

}
