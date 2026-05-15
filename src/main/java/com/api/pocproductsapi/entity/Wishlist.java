package com.api.pocproductsapi.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WISHLISTS")
@Getter
@Setter
public class Wishlist {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "WISHLIST_PRODUCTS",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
}
