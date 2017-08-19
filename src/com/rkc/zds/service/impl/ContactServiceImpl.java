package com.rkc.zds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public void saveContact(ContactDto contact) {
		repo.save(contact);
	}

	@Override
	public void updateContact(ContactDto contact) {
		repo.saveAndFlush(contact);
	}

	@Transactional
	@Override
	public void deleteContact(int id) {
		repo.delete(id);
	}

	private Sort sortByNameASC() {
		return new Sort(Sort.Direction.ASC, "lastName");
	}
}
