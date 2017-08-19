package com.rkc.zds.controller;

import com.rkc.zds.dto.Profile;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UsersController {
    @Autowired
    private UserService userService;
    
    @RequestMapping("/users")
    public List<UserDto> query(){
        return userService.getUsers();
    }

    @RequestMapping("/users/{id}")
    public UserDto query(@PathVariable Integer id){
        return userService.findById(id);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public UserDto update(@RequestBody @Valid UserDto userDTO){
        userService.updateUser(userDTO);
        return userDTO;
    }

    @RequestMapping("/users/profiles")
    public List<String> getProfiles(){
        List<String> profiles = new ArrayList<>();
        for(Profile profile: Profile.values()){
            profiles.add(profile.name());
        }
        return profiles;
    }
}
