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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "artist")
public class Artist extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_seq_gen")
    @SequenceGenerator(name = "artist_seq_gen", sequenceName = "artist_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.artist", targetEntity = ArtistPerformanceRoleJoin.class, cascade = CascadeType.ALL)
    private List<ArtistPerformanceRoleJoin> artistPerformanceRoleJoins;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Performance.class)
    @JoinTable(name = "performance_conductor_join", joinColumns = {
            @JoinColumn(name = "conductor_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "performance_id", nullable = false)
    })
    private List<Performance> conductedPerformances;
}
