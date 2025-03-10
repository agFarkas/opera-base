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

    @Query("""
            select apr from ArtistPerformanceRoleJoin apr
            where apr.id.role = :role
            """)
    List<ArtistPerformanceRoleJoin> findByIdRole(@Param("role") Role role);

    @Modifying
    @Query("""
            update ArtistPerformanceRoleJoin apr set apr.id.role = :newRole
            where apr.id.role = :originalRole
                and apr.id.performance in :performances
                and apr.id.artist in :artists
            """)
    void updateJoinsByRole(
            @Param("originalRole") Role originalRole,
            @Param("newRole") Role newRole,
            @Param("performances") List<Performance> performances,
            @Param("artists") List<Artist> artists
    );
}
