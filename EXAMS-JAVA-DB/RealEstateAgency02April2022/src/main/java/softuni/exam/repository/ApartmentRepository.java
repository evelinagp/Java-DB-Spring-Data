package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    boolean existsByArea(double area);

    boolean existsById(Long id);

    //Optional<Apartment> findByApartmentId(long id);

    boolean existsByTown_TownName(String town);


}
