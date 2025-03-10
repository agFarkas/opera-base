package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("select a from Artist a")
    List<Artist> findAllArtists();

    Optional<Artist> findByNaturalId(UUID naturalId);

    @Query("""
            select a from Artist a
                where a.naturalId in :naturalIds
            """)
    List<Artist> findByNaturalIds(@Param("naturalIds") List<UUID> naturalIds);
}
