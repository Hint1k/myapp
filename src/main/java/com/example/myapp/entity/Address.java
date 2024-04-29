package com.example.myapp.entity;

import com.example.myapp.validation.CapitalLetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /* Turned out that Google API can't find small cities
    without country name in the request, plus some countries
    have cities with the same name */
    @Column(name = "country_name")
    @NotNull(message = "Country name should not be empty")
    @CapitalLetter // custom validator
    private String countryName;

    @Column(name = "city_name")
    @NotNull(message = "City name should not be empty")
    @CapitalLetter // custom validator
    private String cityName;

    @Column(name = "street_name")
    @NotNull(message = "Street name should not be empty")
    @CapitalLetter // custom validator
    private String streetName;

    @Column(name = "house_number")
    @NotNull(message = "House number cannot be empty")
    @Min(value = 1, message = "Minimum house number = 1")
    @Max(value = 99, message = "Maximum house number = 99")
    private Integer houseNumber;

    //bidirectional relationship, owning side
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "courier_id")
    @NotNull(message = "You have to assign courier")
    private Courier courier;

    public Address() {
    }

    public Address(String countryName, String cityName,
                   String streetName, Integer houseNumber) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    public Address(String countryName, String cityName,
                   String streetName, Integer houseNumber,
                   Courier courier) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.courier = courier;
    }

    public Address(Integer id, String countryName,
                   String cityName, String streetName,
                   Integer houseNumber, Courier courier) {
        this.id = id;
        this.countryName = countryName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.courier = courier;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
                Objects.equals(countryName, address.countryName) &&
                Objects.equals(cityName, address.cityName) &&
                Objects.equals(streetName, address.streetName) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(courier, address.courier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryName, cityName,
                streetName, houseNumber, courier);
    }
}