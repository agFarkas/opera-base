package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select l from Location l")
    List<Location> findAllLocations();

    Optional<Location> findByNaturalId(UUID naturalId);
}
