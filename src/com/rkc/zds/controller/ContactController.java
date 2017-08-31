package com.rkc.zds.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.service.ContactService;

@RestController
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping(value = "/api/contact")
public class ContactController {

	final static Logger LOG = LoggerFactory.getLogger(ContactController.class);

	private static final String DEFAULT_PAGE_DISPLAYED_TO_USER = "0";

	@Autowired
	ContactService contactService;

	@Autowired
	private MessageSource messageSource;

	@Value("10")
	private int maxResults;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ContactDto>> findAllContact(Pageable pageable, HttpServletRequest req) {
		Page<ContactDto> page = contactService.findContacts(pageable);
		ResponseEntity<Page<ContactDto>> response = new ResponseEntity<>(page, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContactDto> getContact(@PathVariable int id, HttpServletRequest req) {
		ContactDto contact = contactService.getContact(id);
		return new ResponseEntity<>(contact, HttpStatus.OK);
	}

/*	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContactDto> getContact(@PathVariable String id, HttpServletRequest req) {
		int idInt = Integer.parseInt(id);
		ContactDto contact = contactService.getContact(idInt);
		return new ResponseEntity<>(contact, HttpStatus.OK);
	}
*/	
	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ContactDto>> searchContact(@PathVariable String name, Pageable pageRequest) {
		Page<ContactDto> page = contactService.searchContacts(name);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void createContact(@RequestBody String jsonString) {

		
		//test		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		ObjectMapper mapper = new ObjectMapper();

		ContactDto contactDTO = new ContactDto();
		try {
			contactDTO = mapper.readValue(jsonString, ContactDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		contactService.saveContact(contactDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.PUT, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void updateContact(@RequestBody String jsonString) {
		ObjectMapper mapper = new ObjectMapper();

		ContactDto contact = new ContactDto();
		try {
			contact = mapper.readValue(jsonString, ContactDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		contactService.updateContact(contact);

	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteContact(@PathVariable int id) {
		contactService.deleteContact(id);
		return Integer.toString(id);
	}	
}
