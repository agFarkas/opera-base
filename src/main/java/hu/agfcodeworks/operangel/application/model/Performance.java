package hu.agfcodeworks.operangel.application.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "performance")
public class Performance extends AbstractEntity {

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opera_id", referencedColumnName = "id", nullable = false)
    private Opera opera;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", nullable = false)
    private Conductor conductor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.performance")
    private List<ColleagueRole> colleagueRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

}