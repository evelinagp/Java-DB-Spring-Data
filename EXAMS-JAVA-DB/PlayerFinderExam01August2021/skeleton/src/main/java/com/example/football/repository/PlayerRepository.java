package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByEmail(String email);

    //    @Query("SELECT p from Player p ORDER BY p.stat.shooting DESC, p.stat.passing DESC, p.stat.endurance DESC, p.lastName ASC")
//    List<Player> findByBirthDateBetween(LocalDate after, LocalDate before);
    //!Both methods working
    List<Player> findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(LocalDate after, LocalDate before);
}
