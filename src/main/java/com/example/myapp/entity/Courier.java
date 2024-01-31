package com.example.myapp.entity;

import com.example.myapp.validation.CapitalLetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "courier")
public class Courier {

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

    @Column(name = "first_name")
    @NotNull(message = "First name should not be empty")
    @CapitalLetter // custom validator
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name should not be empty")
    @CapitalLetter // custom validator
    private String lastName;

    @Column(name = "phone")
    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+\\d{11}$",
            message = "Phone number must contain \"+\" and 11 digits")
    private String phone;

    // bidirectional relationship, referencing side
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "courier",
            targetEntity = Address.class,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    private List<Address> addresses;

    public Courier() {
    }

    public Courier(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Courier(Integer id, String firstName, String lastName, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}