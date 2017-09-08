package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.GroupMemberDto;
import com.rkc.zds.dto.GroupMemberElementDto;
import com.rkc.zds.repository.ContactRepository;
import com.rkc.zds.repository.GroupMemberRepository;
import com.rkc.zds.service.ContactService;
import com.rkc.zds.service.GroupMemberService;

@Service
public class ContactServiceImpl implements ContactService {
	private static final int PAGE_SIZE = 50;

	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired	
	private GroupMemberRepository groupMemberRepo;

	@Autowired
	GroupMemberService groupMemberService;

	@Override
	public Page<ContactDto> findContacts(Pageable pageable) {

		return contactRepo.findAll(pageable);
	}

	@Override
	public Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId) {

		List<ContactDto> contacts = contactRepo.findAll();

		List<GroupMemberDto> memberList = groupMemberRepo.findByGroupId(groupId);
		
		List<ContactDto> testList = new ArrayList<ContactDto>();

		List<ContactDto> filteredList = new ArrayList<ContactDto>();

		// build member list of Contacts
		ContactDto contact;
		for (GroupMemberDto element : memberList) {
			contact = contactRepo.findOne(element.getContactId());
			testList.add(contact);
		}

		// check member list of Contacts
		for (ContactDto element : contacts) {
			// if the contact is in the members list, ignore it
			if (!testList.contains(element)) {
				filteredList.add(element);
			}
		}

		PageRequest pageRequest = new PageRequest(0, filteredList.size());

		PageImpl<ContactDto> page = new PageImpl<ContactDto>(filteredList, pageRequest, filteredList.size());

		return page;
	}

	@Override
	public ContactDto getContact(int id) {
		ContactDto contact = contactRepo.findOne(id);

		return contact;
	}

	@Override
	public Page<ContactDto> searchContacts(String name) {

		final PageRequest pageRequest = new PageRequest(0, 10, sortByNameASC());

		return contactRepo.findByLastNameIgnoreCaseLike(pageRequest, "%" + name + "%");
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveContact(ContactDto contact) {

		// test
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		contactRepo.save(contact);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateContact(ContactDto contact) {

		// test
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		contactRepo.saveAndFlush(contact);
	}

	@Transactional
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteContact(int id) {

		// test
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		contactRepo.delete(id);
	}

	private Sort sortByNameASC() {
		return new Sort(Sort.Direction.ASC, "lastName");
	}

}
