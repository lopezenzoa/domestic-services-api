package com.portfolio.domestic_services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED) // this Inheritance is to extends the attributes to the Consumers and Providers
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, unique = true, length = 40)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 40, unique = true)
    private String phoneNumber;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    /*
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToOne(mappedBy = "user")
    private Favorites favorites;
     */

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Call> calls;

    /* Credentials section */

    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)", nullable = false)
    private Roles role;
}
