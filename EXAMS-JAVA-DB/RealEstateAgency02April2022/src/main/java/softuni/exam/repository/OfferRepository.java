package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dto.OutputOneOfferTextDTO;
import softuni.exam.models.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

   //List<Offer> findOfferByAgent_FirstNameAndAgent_LastNameAndIdAndApartment_AreaAndApartment_Town_TownNameAndPriceOrderByApartmentDescPriceAsc();

   @Query("SELECT DISTINCT o FROM Offer o JOIN FETCH o.apartment ap JOIN FETCH o.agent ag " +
           " WHERE o.apartment.apartmentType = 'three_rooms'" +
           " ORDER BY o.apartment.area DESC, o.price ASC")
   List<Offer>findBestOffers();

//   @Query("SELECT CONCAT(o.agent.firstName, ' ', o.agent.lastName) AS fullName," +
//           " o.id AS offerId, o.apartment.area AS apartmentArea," +
//           " o.apartment.town.townName AS townName, o.price AS price" +
//           " FROM Offer o " +
//           " WHERE o.apartment.apartmentType = 'three_rooms'" +
//           " ORDER BY apartmentArea DESC, price ASC")
//   List<OutputOneOfferTextDTO>findBestOffers();
}
