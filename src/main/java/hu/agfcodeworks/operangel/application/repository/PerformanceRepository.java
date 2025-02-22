package hu.agfcodeworks.operangel.application.repository;

import hu.agfcodeworks.operangel.application.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query("""
            select prf from Performance prf
                join fetch prf.location
                left join fetch prf.performanceConductorJoins prfCnd
                left join fetch prf.artistPerformanceRoleJoins apr
            where prf.play.naturalId = :playNaturalId
            order by prf.date
            """)
    List<Performance> findByPlayNaturalId(@Param("playNaturalId") UUID playNaturalId);

}
