package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity{

    @Column(name = "apartment_type", nullable = false)
    private ApartmentType apartmentType ;
    @Column(unique = true)
    private double area;
    @Column(unique = true)
    private Town town;
    private Set<Offer> offers;

    public Apartment() {
    }
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }


    @Enumerated(EnumType.STRING)
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }
    @ManyToOne(optional = false)
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
