package hu.agfcodeworks.operangel.application.model;

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
@Table(name = "composer")
@Entity
public class Composer extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "composer_seq_gen")
    @SequenceGenerator(name = "composer_seq_gen", sequenceName = "composer_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "composer")
    private List<Play> plays;
}
