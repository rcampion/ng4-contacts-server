package com.rkc.zds.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the PCM_USERS database table.
 * 
 */
@Transactional
@Entity
@Table(name="PCM_USERS")
public class UserDto implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="LOGIN")
    @NotEmpty
    private String login;
	
	@Column(name="USERNAME")
	private String userName;

	@Column(name="ENABLED")	
	private int enabled;

	@Column(name="PASSWORD")	
	private String password;
	
	@Column(name="FIRSTNAME")	
	private String firstName;
	
	@Column(name="LASTNAME")	
	private String lastName;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
//	@JoinColumn(name = "USERNAME")

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "userName", referencedColumnName = "userName")
	@ElementCollection(targetClass=AuthorityDto.class)
	private Set<AuthorityDto> authorities = new HashSet<AuthorityDto>(0);

	@JsonIgnore
	@Column(name="PUBLIC_SECRET")
    private String publicSecret;

    @JsonIgnore
	@Column(name="PRIVATE_SECRET")
    private String privateSecret;

    private Profile profile;
    
//	private List<String> authorities;
    
    public UserDto() {
    }
    
	public UserDto(String username, String password, int enabled) {
		this.userName = username;
		this.password = password;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Set<AuthorityDto> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(Set<AuthorityDto> authorityDto) {
		this.authorities= authorityDto;
	}

	public boolean isEnabled() {
		if(this.enabled == 1)
			return true;
		return false;
	}
    public String getPublicSecret() {
		return publicSecret;
	}

	public void setPublicSecret(String publicSecret) {
		this.publicSecret = publicSecret;
	}

	public String getPrivateSecret() {
		return privateSecret;
	}

	public void setPrivateSecret(String privateSecret) {
		this.privateSecret = privateSecret;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
/*
    public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
*/
}