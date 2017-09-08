package com.rkc.zds.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;

public interface ContactService {

    Page<ContactDto> findContacts(Pageable pageable);

    Page<ContactDto> searchContacts(String name);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId);
    
    @Transactional     
    ContactDto getContact(int id);    

    @Transactional    
    public void saveContact(ContactDto contact);

    @Transactional    
    public void updateContact(ContactDto contact);

    @Transactional  
	void deleteContact(int id);

}
