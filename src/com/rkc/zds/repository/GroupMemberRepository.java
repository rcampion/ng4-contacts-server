package com.rkc.zds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.dto.GroupMemberDto;

public interface GroupMemberRepository extends JpaRepository<GroupMemberDto, Integer> {
  
	Page<GroupMemberDto> findByGroupId(Pageable pageable, int groupId);

	List<GroupMemberDto> findByGroupId(int groupId);
	       
}
