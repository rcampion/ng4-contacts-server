package com.rkc.zds.dto;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PCM_GROUP database table.
 * 
 */
@Entity
@Table(name="PCM_GROUP")
public class GroupDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GROUP_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int groupId;

	@Column(name="GROUP_DESCRIPTION")
	private String groupDescription;

	@Column(name="GROUP_NAME")
	private String groupName;

    public GroupDto() {
    }

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupDescription() {
		return this.groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}