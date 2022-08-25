package hiberspring.repository;

import hiberspring.domain.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    boolean existsByName(String name);

    Optional<Town> findByName(String townName);

}