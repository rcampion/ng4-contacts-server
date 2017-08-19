package com.rkc.zds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.dto.GroupMemberDto;
import com.rkc.zds.repository.GroupRepository;
import com.rkc.zds.repository.GroupMemberRepository;
import com.rkc.zds.service.GroupMemberService;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

	@Autowired
	private GroupMemberRepository groupMemberRepo;

	@Override
	public Page<GroupMemberDto> findGroupMembers(Pageable pageable, int id) {

		Page<GroupMemberDto> page = groupMemberRepo.findByGroupId(pageable, id);

		return page;
	}

	@Override
	public List<GroupMemberDto> findAllMembers(int groupId) {

		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupId);

		return list;
	}
	
	private Sort sortByIdASC() {
		return new Sort(Sort.Direction.ASC, "groupId");
	}

	@Override
	public void saveGroupMember(GroupMemberDto groupMember) {
		// checking for duplicates
		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupMember.getGroupId());

		//return if duplicate found
		for (GroupMemberDto element : list) {
			if (element.getContactId() == groupMember.getContactId()) {
				return;
			}
		}

		groupMemberRepo.save(groupMember);
	}

	@Override
	public void deleteGroupMember(int id) {

		groupMemberRepo.delete(id);
		
	}	
}
