package com.example.myapp.entity;

import com.example.myapp.validation.CapitalLetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "First name should not be empty")
    @Column(name = "first_name")
    @CapitalLetter
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @Column(name = "last_name")
    @CapitalLetter
    private String lastName;

    @NotNull(message = "Email should not be empty")
    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
            message = "Email format must be name@server.domain")
    private String email;

    @Column(name = "username")
    @NotNull(message = "Username should not be empty")
    private String username;

    @Column(name = "password")
    @NotNull(message = "Password should not be empty")
    private String password;

    @Column(name = "enabled")
    private int isEnabled;

    // bidirectional, referencing side
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Role role;

    public Customer() {
    }

    public Customer(String firstName, String lastName,
                    String email, String username,
                    String password, int isEnabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    public Customer(String firstName, String lastName,
                    String email, String username, String password,
                    int isEnabled, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    public Customer(Integer id, String firstName, String lastName,
                    String email, String username, String password,
                    int isEnabled, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}