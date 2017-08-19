package com.rkc.zds.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the PCM_AUTHORITIES database table.
 * 
 */
@Entity
//@Table(name="PCM_AUTHORITIES")
@Table(name = "PCM_AUTHORITIES", catalog = "pcm", uniqueConstraints = @UniqueConstraint(columnNames = { "AUTHORITY", "USERNAME" }))
public class AuthorityDto implements Serializable {
	private static final long serialVersionUID = 1L;

//	@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.EAGER)
	private UserDto user;

	private UserDto getUser() {
		return this.user;
	}
	
    private void setUser(UserDto userDto) {
        this.user = userDto;
    }
    
	@Id
	@Column(name="USERNAME")
	@JoinColumn(name = "USERNAME", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String userName;

	@Column(name = "AUTHORITY", nullable = false, length = 45)
	private String authority;

    public AuthorityDto() {
    }

	public AuthorityDto(UserDto user, String role) {
		this.user = user;
		this.authority = role;
	}
	
	public String getUsername() {
		return this.userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}