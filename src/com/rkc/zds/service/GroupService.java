package com.rkc.zds.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.dto.GroupMemberDto;

public interface GroupService {

    Page<GroupDto> findGroups(Pageable pageable);

    Page<GroupDto> searchGroups(String name);

    @Transactional     
    GroupDto getGroup(int id);    

    @Transactional     
    Page<GroupMemberDto> findGroupMembers(int id); 
    
    @Transactional    
    public void saveGroup(GroupDto group);

    @Transactional    
    public void updateGroup(GroupDto group);

    @Transactional  
	void deleteGroup(int id);

}
