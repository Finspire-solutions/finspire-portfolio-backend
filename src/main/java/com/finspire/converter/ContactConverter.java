package com.finspire.converter;

import com.finspire.controller.dto.ContactRequestDto;
import com.finspire.entity.ContactDetails;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter {
    public ContactDetails convert(ContactRequestDto contactRequestDto){
        return ContactDetails.builder()
                .firstName(contactRequestDto.getFirstName())
                .lastName(contactRequestDto.getLastName())
                .phoneNo(contactRequestDto.getPhoneNo())
                .email(contactRequestDto.getEmail())
                .interestedService(contactRequestDto.getInterestedService())
                .message(contactRequestDto.getMessage())
                .build();
    }
}
