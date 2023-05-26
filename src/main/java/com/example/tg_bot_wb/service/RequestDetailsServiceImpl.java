package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestDetailsServiceImpl implements RequestDetailsService{
    private final RequestDetailsRepository requestDetailsRepository;

    public RequestDetailsServiceImpl(RequestDetailsRepository requestDetailsRepository) {
        this.requestDetailsRepository = requestDetailsRepository;
    }

    @Override
    @Transactional
    public void saveRequestDetails(RequestDetails requestDetails) {
        requestDetailsRepository.save(requestDetails);
    }

}
