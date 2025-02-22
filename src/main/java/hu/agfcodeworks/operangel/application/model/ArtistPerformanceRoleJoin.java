package hu.agfcodeworks.operangel.application.model;

import hu.agfcodeworks.operangel.application.model.embeddable.ArtistPerformanceRoleId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@Entity
@Table(name = "artist_performance_role_join")
public class ArtistPerformanceRoleJoin implements Serializable {

    @EmbeddedId
    private ArtistPerformanceRoleId id;
}
