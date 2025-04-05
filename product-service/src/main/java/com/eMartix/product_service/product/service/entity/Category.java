package com.eMartix.product_service.product.service.entity;

import com.eMartix.commons.id.GeneratedID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedID
    private Long id;

    private String name;

    @Column(unique = true)
    private String urlKey;

    private String thumbnailUrl;
    private Long parentId;
    //private boolean isPrimary;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();
}
