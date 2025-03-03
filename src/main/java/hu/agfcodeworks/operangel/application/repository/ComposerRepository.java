package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Composer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {

    @Query("select c from Composer c")
    List<Composer> findAllComposers();

    Optional<Composer> findByNaturalId(UUID naturalId);
}
