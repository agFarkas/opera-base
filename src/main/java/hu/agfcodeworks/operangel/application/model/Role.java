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
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "role")
public class Role extends AbstractEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "play_id", referencedColumnName = "id", nullable = false)
    private Play play;

}
