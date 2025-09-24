package com.finspire.service;

import com.finspire.controller.dto.ContactRequestDto;
import com.finspire.converter.ContactConverter;
import com.finspire.entity.Blogs;
import com.finspire.entity.ContactDetails;
import com.finspire.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final ImageService imageService;
    private final ContactConverter contactConverter;
    public void saveContactDetails(MultipartFile file, ContactRequestDto contactRequestDto) throws IOException {
        String url = null;
        if (file!= null && file.isEmpty()){
            url = imageService.uploadImage(file);
        }
        contactRepository.save(ContactConverter.convert(contactRequestDto,url));
    }

    public Page<ContactDetails> getAllContacts(int page, int size) {
    }

    public String updateContactsDetails(Long id, MultipartFile file, ContactRequestDto contactRequestDto) {
    }

    public String deleteContactsDetailsById(Long id) {
    }
}
