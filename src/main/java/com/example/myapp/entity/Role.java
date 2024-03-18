package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "authority")
    private String authority;

    // bidirectional, owning side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Customer customer;

    public Role() {
    }

    public Role(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public Role(String username, String authority,
                Customer customer) {
        this.username = username;
        this.authority = authority;
        this.customer = customer;
    }

    public Role(Integer id, String username,
                String authority, Customer customer) {
        this.id = id;
        this.username = username;
        this.authority = authority;
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}