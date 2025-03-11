package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.model.embeddable.ArtistPerformanceRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistPerformanceRoleJoinRepository extends JpaRepository<ArtistPerformanceRoleJoin, ArtistPerformanceRoleId> {

    @Modifying
    @Query("""
            update ArtistPerformanceRoleJoin apr set apr.id.role = :newRole
            where apr.id.role = :originalRole
                and apr.id.performance in :performance
                and apr.id.artist in :artist
            """)
    void updateJoinsByRole(
            @Param("originalRole") Role originalRole,
            @Param("newRole") Role newRole,
            @Param("performance") Performance performance,
            @Param("artist") Artist artist
    );

    @Modifying
    @Query("""
            update ArtistPerformanceRoleJoin apr set apr.id.artist = :newArtist
            where apr.id.artist = :originalArtist
                and apr.id.performance = :performance
                and apr.id.role = :role
            """)
    void updateJoinsByArtist(
            @Param("originalArtist") Artist originalArtist,
            @Param("newArtist") Artist newArtist,
            @Param("performance") Performance performance,
            @Param("role") Role role

    );
}
