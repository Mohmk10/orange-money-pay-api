package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.MerchantPaymentRequest;
import com.orangemoney.api.dto.response.MerchantPaymentResponse;

import java.util.List;

public interface MerchantPaymentService {

    MerchantPaymentResponse payMerchant(Long payerId, MerchantPaymentRequest request);

    List<MerchantPaymentResponse> getMerchantPaymentHistory(Long userId);
}