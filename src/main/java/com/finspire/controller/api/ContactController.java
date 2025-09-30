package com.finspire.controller.api;

import com.finspire.controller.dto.ContactRequestDto;
import com.finspire.entity.ContactDetails;
import com.finspire.service.ContactService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;
    @PostMapping()
    public ResponseEntity<String> saveContactDetails(@RequestBody() ContactRequestDto contactRequestDto) throws MessagingException {
        contactService.saveContactDetails(contactRequestDto);
        return ResponseEntity.ok("Successfully saved blog details");
    }

    @GetMapping("/getAll")
    public Page<ContactDetails> getAllContacts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return contactService.getAllContacts(page, size);
    }

    @DeleteMapping("/{id}")
    public String deleteContactDetailsById(@PathVariable Long id){
        return contactService.deleteContactsDetailsById(id);
    }

}
