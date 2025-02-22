package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.util.Set;

import static hu.agfcodeworks.operangel.application.model.enums.PlayType.OPERA;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Table(name = "play")
@Entity
public class Play extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "play_seq_gen")
    @SequenceGenerator(name = "play_seq_gen", sequenceName = "play_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 5)
    private PlayType type = OPERA;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "play")
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "play")
    private List<Performance> performances;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Composer.class)
    @JoinColumn(name = "composer_id", referencedColumnName = "id", nullable = false)
    private Composer composer;
}
