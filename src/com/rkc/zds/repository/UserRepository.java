package com.rkc.zds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.UserDto;

//public interface UserRepository extends CrudRepository<UserDto, Integer> {
public interface UserRepository extends JpaRepository<UserDto, Integer> {
	UserDto findByUserName(String userName);
}
