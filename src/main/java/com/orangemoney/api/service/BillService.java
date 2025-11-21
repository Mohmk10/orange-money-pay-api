package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.BillPaymentRequest;
import com.orangemoney.api.dto.response.BillResponse;

import java.util.List;

public interface BillService {

    BillResponse payBill(Long userId, BillPaymentRequest request);

    List<BillResponse> getBillHistory(Long userId);
}