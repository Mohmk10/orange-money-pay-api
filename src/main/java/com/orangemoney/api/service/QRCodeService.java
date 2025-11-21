package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.QRPaymentRequest;
import com.orangemoney.api.dto.response.QRCodeResponse;
import com.orangemoney.api.dto.response.TransferResponse;

public interface QRCodeService {

    QRCodeResponse generateUserQRCode(Long userId);

    QRCodeResponse generateMerchantQRCode(Long userId, String amount, String reference);

    TransferResponse scanAndPay(Long payerId, QRPaymentRequest request);
}