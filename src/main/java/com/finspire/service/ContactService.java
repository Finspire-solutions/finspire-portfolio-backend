package com.finspire.service;

import com.finspire.constants.ApplicationConstants;
import com.finspire.controller.dto.ContactRequestDto;
import com.finspire.converter.ContactConverter;
import com.finspire.email.EmailService;
import com.finspire.entity.Blogs;
import com.finspire.entity.ContactDetails;
import com.finspire.exception.ServiceException;
import com.finspire.repository.ContactRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final EmailService emailService;
    private final ContactConverter contactConverter;
    public void saveContactDetails(ContactRequestDto contactRequestDto) throws MessagingException {
        contactRepository.save(contactConverter.convert(contactRequestDto));
        emailService.sendContactEmail(contactRequestDto);

    }

    public Page<ContactDetails> getAllContacts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return contactRepository.findAll(pageable);
    }

    public String deleteContactsDetailsById(Long id) {
        ContactDetails contactDetails = findContactDetailsById(id);
        contactRepository.deleteById(id);
        return "successfully deleted";
    }

    private ContactDetails findContactDetailsById(Long id){
        return contactRepository.findById(id).orElseThrow(() -> new ServiceException(ApplicationConstants.CONTACT_DETAILS_NOT_FOUND,ApplicationConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST));
    }
}
