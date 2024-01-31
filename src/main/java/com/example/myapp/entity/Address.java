package com.example.myapp.entity;

import com.example.myapp.validation.CapitalLetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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

    @Column(name = "street_name")
    @NotNull(message = "Street name should not be empty")
    @CapitalLetter // custom validator
    private String streetName;

    @Column(name = "house_number")
    @NotNull(message = "House number cannot be empty")
    @Min(value = 1, message = "Minimum house number = 1")
    @Max(value = 99, message = "Maximum house number = 99")
    private Integer houseNumber;

    @Column(name = "flat_number")
    @NotNull(message = "Flat number cannot be empty")
    @Min(value = 1, message = "Minimum flat number = 1")
    @Max(value = 99, message = "Maximum flat number = 99")
    private Integer flatNumber;

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

    public Address(String streetName, Integer houseNumber, Integer flatNumber) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }

    public Address(Integer id, String streetName, Integer houseNumber, Integer flatNumber) {
        this.id = id;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }

    public Address(String streetName, Integer houseNumber, Integer flatNumber, Courier courier) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
        this.courier = courier;
    }

    public Address(Integer id, String streetName, Integer houseNumber, Integer flatNumber, Courier courier) {
        this.id = id;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
        this.courier = courier;
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

    public Integer getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}