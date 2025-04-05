package com.eMartix.product_service.product.service.entity;

import com.eMartix.commons.id.GeneratedID;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "product_specifications")
public class ProductSpecification {
    @Id
    @GeneratedID
    private Long id;

    private String name;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
