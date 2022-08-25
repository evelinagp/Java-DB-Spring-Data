package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

//    boolean existsByFirstName(String firstName);
//
//    boolean existsByLastName(String lastName);

    List<Player> findAllByTeam_NameOrderById(String team);

    List<Player> findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal salary);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
