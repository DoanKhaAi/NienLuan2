package com.khaai.NuoiEm.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="children")
public class Children {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="child_id")
	private Integer childID;
			
	@Column(nullable=false,length=50, name="fullname")
	private String fullname;
	
	@Column(name="birthday")
	private LocalDate birthday;
	
	@Column(name="gender")
	private Integer gender;
	
	@Column(length=30, name="image")
	private String image;
	
	@Column(name="adopted")
	private Boolean apdoted;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUser user;
	
	@OneToMany(mappedBy = "children", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies;
	
	public Children() {
		
	}

	


	public Children(Integer childID, String fullname, LocalDate birthday, Integer gender, String image, Boolean apdoted,
			Boolean enabled, MyUser user, List<Study> studies) {
		super();
		this.childID = childID;
		this.fullname = fullname;
		this.birthday = birthday;
		this.gender = gender;
		this.image = image;
		this.apdoted = apdoted;
		this.enabled = enabled;
		this.user = user;
		this.studies = studies;
	}




	public Integer getChildID() {
		return childID;
	}



	public void setChildID(Integer childID) {
		this.childID = childID;
	}



	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}
	
	public List<Study> getStudies() {
		return studies;
	}


	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}


	public Boolean getApdoted() {
		return apdoted;
	}


	public void setApdoted(Boolean apdoted) {
		this.apdoted = apdoted;
	}


	public Boolean getEnabled() {
		return enabled;
	}



	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	
	
}
