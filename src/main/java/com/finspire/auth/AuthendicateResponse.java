package com.finspire.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthendicateResponse {
    private String token;        // access token
    private String refreshToken; // new field
}
