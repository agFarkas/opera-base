package hu.agfcodeworks.operangel.application.model.embeddable;

import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.model.Role;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class ArtistPerformanceRoleId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Artist.class)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Performance.class)
    @JoinColumn(name = "performance_id", referencedColumnName = "id")
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
