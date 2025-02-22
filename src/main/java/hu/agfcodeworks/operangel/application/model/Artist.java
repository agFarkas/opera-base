package hu.agfcodeworks.operangel.application.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.conductor", targetEntity = PerformanceConductorJoin.class, cascade = CascadeType.ALL)
    private List<PerformanceConductorJoin> performanceConductorJoins;
}
