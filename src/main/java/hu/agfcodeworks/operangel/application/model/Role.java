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

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "role")
public class Role extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_gen")
    @SequenceGenerator(name = "role_seq_gen", sequenceName = "role_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "play_id", referencedColumnName = "id", nullable = false)
    private Play play;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.role")
    private List<ArtistPerformanceRoleJoin> artistPerformanceRoleJoins;
}
