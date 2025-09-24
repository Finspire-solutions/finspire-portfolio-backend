package com.finspire.controller.api;

import com.finspire.controller.dto.BlogsRequestDto;
import com.finspire.controller.dto.ContactRequestDto;
import com.finspire.entity.Blogs;
import com.finspire.entity.ContactDetails;
import com.finspire.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;
    @PostMapping()
    public ResponseEntity<String> saveContactDetails(@RequestPart("file") @Valid MultipartFile file,
                                                  @RequestPart("blogsRequestDto") ContactRequestDto contactRequestDto) throws IOException {
        contactService.saveContactDetails(file,contactRequestDto);
        return ResponseEntity.ok("Successfully saved blog details");
    }

    @GetMapping("/getAll")
    public Page<ContactDetails> getAllContacts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return contactService.getAllContacts(page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateContactsDetails(@PathVariable Long id, @RequestPart(value = "file",required = false) MultipartFile file,
                                                     @RequestPart("blogsRequestDto") ContactRequestDto contactRequestDto) throws IOException {
        return ResponseEntity.ok(contactService.updateContactsDetails(id, file,contactRequestDto));
    }

    @DeleteMapping("/{id}")
    public String deleteContactDetailsById(@PathVariable Long id){
        return contactService.deleteContactsDetailsById(id);
    }

}
