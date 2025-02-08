package com.eMartix.auth_service.model;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_group")
public class Group extends BaseEntity<String> {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String groupDescription;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
}