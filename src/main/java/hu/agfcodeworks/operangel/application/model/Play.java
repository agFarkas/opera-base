package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static hu.agfcodeworks.operangel.application.model.enums.PlayType.OPERA;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Table(name = "play")
@Entity
public class Play extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 5)
    private PlayType type = OPERA;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "play")
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "play")
    private List<Performance> performances;
}
