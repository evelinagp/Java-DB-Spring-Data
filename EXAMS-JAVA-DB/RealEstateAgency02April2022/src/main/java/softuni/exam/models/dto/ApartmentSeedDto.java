package softuni.exam.models.dto;

import softuni.exam.models.entity.ApartmentType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedDto {
    @XmlElement
    private ApartmentType apartmentType ;
    @XmlElement
    private double area;
    @XmlElement
    private String town;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }
    @Min(40)
    @Column(unique = true)
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @NotBlank
    @Column(unique = true)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
