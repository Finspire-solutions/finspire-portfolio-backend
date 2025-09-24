package com.finspire.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class ContactRequestDto {
    @NotBlank(message = "first Name can't be blank")
    private String firstName;
    @NotBlank(message = "first Name can't be blank")
    private String lastName;
    @NotBlank(message = "last Name can't be blank")
    private String phoneNo;
    @NotBlank(message = "phone number can't be blank")
    private String email;
    @NotBlank(message = "message can't be blank")
    private String message;
    @NotBlank(message = "service can't be blank")
    private String interestedService;

}
