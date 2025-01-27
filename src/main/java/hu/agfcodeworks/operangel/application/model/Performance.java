package hu.agfcodeworks.operangel.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "performance_seq_gen")
    @SequenceGenerator(name = "performance_seq_gen", sequenceName = "performance_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Play.class)
    @JoinColumn(name = "play_id", referencedColumnName = "id", nullable = false)
    private Play play;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Artist.class)
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", nullable = false)
    private Artist conductor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.performance")
    private List<ArtistPerformanceRoleJoin> artistPerformanceRoleJoins;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Location.class)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

}