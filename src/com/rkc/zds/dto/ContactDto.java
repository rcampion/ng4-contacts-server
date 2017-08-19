package com.rkc.zds.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PcmContacts generated by hbm2java
 */
@Entity
@Table(name = "PCM_CONTACTS")
public class ContactDto implements java.io.Serializable {
/*
	
	private String name;
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
*/	
    private static final long serialVersionUID = -6809049173391335091L;
	
	@Id
	@Column(name="CONTACT_ID", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
//	@JsonProperty("first_name")
	private String firstName;
//	@JsonProperty("last_name")
	private String lastName;
	private String title;
	private String company;

	public ContactDto() {
	}

	public ContactDto(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public ContactDto(int id, String firstName, String lastName, String title, String company) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.company = company;
	}

//	@Id

//	@Column(name = "CONTACT_ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "FIRSTNAME", nullable = false, length = 100)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LASTNAME", nullable = false, length = 100)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "COMPANY", length = 100)
	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
