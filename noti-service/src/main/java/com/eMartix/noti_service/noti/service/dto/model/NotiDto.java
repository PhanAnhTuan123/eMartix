package com.eMartix.noti_service.noti.service.dto.model;

import lombok.Data;

import java.util.Date;

@Data
public class NotiDto {
    private long id;
    private String message;
    private String title;
    private Date timestamp;
}