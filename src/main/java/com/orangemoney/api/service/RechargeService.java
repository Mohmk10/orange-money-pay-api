package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.RechargeRequest;
import com.orangemoney.api.dto.response.RechargeResponse;

import java.util.List;

public interface RechargeService {

    RechargeResponse performRecharge(Long userId, RechargeRequest request);

    List<RechargeResponse> getRechargeHistory(Long userId);
}