package hu.agfcodeworks.operangel.application.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "performance")
public class Performance extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "performance_seq_gen")
    @SequenceGenerator(name = "performance_seq_gen", sequenceName = "performance_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Play.class)
    @JoinColumn(name = "play_id", referencedColumnName = "id", nullable = false)
    private Play play;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Artist.class, cascade = CascadeType.ALL)
    @JoinTable(name = "performance_conductor_join", joinColumns = {
            @JoinColumn(name = "performance_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "conductor_id", nullable = false)
    })
    private List<Artist> conductors;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.performance", cascade = CascadeType.ALL)
    private Set<ArtistPerformanceRoleJoin> artistPerformanceRoleJoins;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Location.class)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

}