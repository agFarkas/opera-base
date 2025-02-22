package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
            select r from Role r
            join fetch r.play
            """)
    List<Role> findAllRoles();


}
