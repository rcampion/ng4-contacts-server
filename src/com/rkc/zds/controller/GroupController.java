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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.service.GroupService;

@RestController
@RequestMapping(value = "/api/group")
public class GroupController {

	final static Logger LOG = LoggerFactory.getLogger(GroupController.class);

	private static final String DEFAULT_PAGE_DISPLAYED_TO_USER = "0";

	// @Inject
	@Autowired
	GroupService groupService;

	@Autowired
	private MessageSource messageSource;

	@Value("10")
	private int maxResults;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<GroupDto>> findAllGroups(Pageable pageable, HttpServletRequest req) {
		Page<GroupDto> page = groupService.findGroups(pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupDto> getGroup(@PathVariable int id, HttpServletRequest req) {
		GroupDto group = groupService.getGroup(id);
		return new ResponseEntity<>(group, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<GroupDto>> searchGroup(@PathVariable String name, Pageable pageRequest) {
		Page<GroupDto> page = groupService.searchGroups(name);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void createGroup(@RequestBody String jsonString) {

		ObjectMapper mapper = new ObjectMapper();

		GroupDto groupDTO = new GroupDto();
		try {
			groupDTO = mapper.readValue(jsonString, GroupDto.class);
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

		groupService.saveGroup(groupDTO);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void updateGroup(@RequestBody String jsonString) {
		ObjectMapper mapper = new ObjectMapper();

		GroupDto group = new GroupDto();
		try {
			group = mapper.readValue(jsonString, GroupDto.class);
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

		groupService.updateGroup(group);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteGroup(@PathVariable int id) {
		groupService.deleteGroup(id);
		return Integer.toString(id);
	}	
}
