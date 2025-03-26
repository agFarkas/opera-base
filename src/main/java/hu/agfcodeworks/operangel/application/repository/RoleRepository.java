package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
            select r from Role r
            join fetch r.play
            """)
    List<Role> findAllRoles();

    void deleteByNaturalId(UUID naturalId);

    Optional<Role> findByNaturalId(UUID naturalId);

    @Query("""
            select r from Role r
                where r.naturalId in :naturalIds
            """)
    Set<Role> findByNaturalIds(@Param("naturalIds") Set<UUID> naturalIds);

}
