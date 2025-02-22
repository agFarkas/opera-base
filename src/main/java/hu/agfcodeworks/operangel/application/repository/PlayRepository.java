package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayRepository extends JpaRepository<Play, Long> {

    @Query("""
            select p from Play p
            join fetch p.composer
            where p.type = :type
            """)
    List<Play> findOperaHeadsByType(@Param("type") PlayType type);

    @Query("""
            select p from Play p
            join fetch p.composer
            join fetch p.roles
            where p.naturalId = :naturalId
            """)
    Optional<Play> findByNaturalId(@Param("naturalId") UUID naturalId);
}
