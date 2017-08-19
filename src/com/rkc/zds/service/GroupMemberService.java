package com.rkc.zds.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.GroupMemberDto;

public interface GroupMemberService {
    Page<GroupMemberDto> findGroupMembers(Pageable pageable, int groupId);
    
    List<GroupMemberDto> findAllMembers(int groupId);

    @Transactional    
    public void saveGroupMember(GroupMemberDto groupMember);    

    @Transactional  
	void deleteGroupMember(int id);
}
