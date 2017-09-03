package com.rkc.zds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.dto.GroupMemberDto;
import com.rkc.zds.repository.GroupRepository;
import com.rkc.zds.repository.GroupMemberRepository;
import com.rkc.zds.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
	private static final int PAGE_SIZE = 50;

	@Autowired
	private GroupRepository groupRepo;

	@Autowired
	private GroupMemberRepository groupMemberRepo;
	
	@Override
	public Page<GroupDto> findGroups(Pageable pageable) {

		int pageNumber = pageable.getPageNumber();

		PageRequest request = new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, "groupName");

		return groupRepo.findAll(pageable);
	}

	@Override
	public GroupDto getGroup(int id) {
		GroupDto group = groupRepo.findOne(id);

		return group;
	}

	@Override
	public Page<GroupMemberDto> findGroupMembers(int id) {
		GroupDto group = groupRepo.findOne(id);

		final PageRequest pageRequest = new PageRequest(0, 10, sortByNameASC());

		Page page = groupMemberRepo.findByGroupId(pageRequest, id );

		return page;
	}
	
	@Override
	public Page<GroupDto> searchGroups(String name) {

		final PageRequest pageRequest = new PageRequest(0, 10, sortByNameASC());

		return groupRepo.findByGroupNameLike(pageRequest, "%" + name + "%");
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveGroup(GroupDto group) {
		groupRepo.save(group);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateGroup(GroupDto group) {
		groupRepo.saveAndFlush(group);
	}

	@Transactional
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteGroup(int groupId) {
		
		//delete all group members for this group prior to deleting group.		
		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupId);
		
		for(GroupMemberDto element : list){
			groupMemberRepo.delete(element);
		}
		
		groupRepo.delete(groupId);
	}

	private Sort sortByNameASC() {
		return new Sort(Sort.Direction.ASC, "groupName");
	}
}
