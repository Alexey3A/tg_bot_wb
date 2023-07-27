package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RequestDetailsServiceImpl implements RequestDetailsService{
    private final RequestDetailsRepository requestDetailsRepository;

    public RequestDetailsServiceImpl(RequestDetailsRepository requestDetailsRepository) {
        this.requestDetailsRepository = requestDetailsRepository;
    }

    @Override
    @Transactional
    public RequestDetails saveRequestDetails(RequestDetails requestDetails) {
        return requestDetailsRepository.save(requestDetails);
    }

}
