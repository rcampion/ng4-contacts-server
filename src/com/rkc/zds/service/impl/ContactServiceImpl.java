package com.rkc.zds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.repository.ContactRepository;
import com.rkc.zds.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
	private static final int PAGE_SIZE = 50;

	@Autowired
	private ContactRepository repo;

	@Override
	public Page<ContactDto> findContacts(Pageable pageable) {

		return repo.findAll(pageable);
	}

	@Override
	public ContactDto getContact(int id) {
		ContactDto contact = repo.findOne(id);

		return contact;
	}

	@Override
	public Page<ContactDto> searchContacts(String name) {

		final PageRequest pageRequest = new PageRequest(0, 10, sortByNameASC());

		return repo.findByLastNameIgnoreCaseLike(pageRequest, "%" + name + "%");
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveContact(ContactDto contact) {
		
		//test		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		repo.save(contact);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateContact(ContactDto contact) {
		
		//test		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		repo.saveAndFlush(contact);
	}

	@Transactional
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteContact(int id) {
		
		//test		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		repo.delete(id);
	}

	private Sort sortByNameASC() {
		return new Sort(Sort.Direction.ASC, "lastName");
	}
}
