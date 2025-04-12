package com.eMartix.noti_service.noti.service.service.impl;

import com.eMartix.noti_service.noti.service.dto.mapper.NotiMapper;
import com.eMartix.noti_service.noti.service.dto.model.NotiDto;
import com.eMartix.noti_service.noti.service.dto.response.NotiResponseDto;
import com.eMartix.noti_service.noti.service.dto.response.ObjectResponse;
import com.eMartix.noti_service.noti.service.repository.NotiRepository;
import com.eMartix.noti_service.noti.service.service.NotiService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class NotiServiceImpl implements NotiService {
//    private final NotiMapper notiMapper;
//    private NotiRepository notiRepository;
//    private WebClient webClient;


    @Override
    public NotiDto getNotiWithProduct(long productId) {
        return null;
    }

    @Override
    public ObjectResponse<NotiDto> getAllNotifications(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public NotiDto getNotificationById(long id) {
        return null;
    }

    @Override
    public NotiDto createNotification(NotiResponseDto notiDto) {
        return null;
    }

    @Override
    public NotiDto updateNotification(long notiId, NotiResponseDto notiDto) {
        return null;
    }

    @Override
    public void deleteNotification(long id) {

    }

    @Override
    public ObjectResponse<NotiDto> searchNoti(String title, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
