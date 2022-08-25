package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsById(Long id);

    @Query("SELECT DISTINCT c FROM Car c JOIN FETCH c.pictures AS pic ORDER BY size(pic) DESC, c.make ")
    List<Car>findCarOrderByPicturesCountThenByMake();

}
