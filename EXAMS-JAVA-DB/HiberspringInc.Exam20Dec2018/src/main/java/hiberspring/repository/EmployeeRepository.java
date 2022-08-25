package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCard_Number(String cardNumber);

    @Query("SELECT e FROM Employee e WHERE size(e.branch.products) > 0 order by CONCAT( e.firstName,' ', e.lastName)ASC, length(e.position) desc ")
    List<Employee> findByBranchWithAtLeastOneProductOrderByFirstNameAscPositionLengthDesc();
}
