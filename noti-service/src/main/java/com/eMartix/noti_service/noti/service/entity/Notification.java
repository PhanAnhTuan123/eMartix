package com.eMartix.noti_service.noti.service.entity;

import com.eMartix.commons.id.GeneratedID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedID
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;
    private Date timestamp;
    private String message;

}
