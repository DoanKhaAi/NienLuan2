package com.khaai.NuoiEm.Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class MyUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Integer userID;
	
	@Column(nullable=false,length=20, name="username", unique=true)
	private String username;
		
	@Column(nullable=false,length=100, name="password")
	private String password;
	
	@Column(nullable=false,length=50, name="fullname")
	private String fullname;
	
	@Column(name="birthday")
	private LocalDate birthday;
	
	@Column(name="gender")
	private Integer gender;
	
	@Column(name="email", unique=true)
	private String email;
	
	@Column(length=30, name="image")
	private String image;
	
	@Column(length=10, name="role")
	private String role;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Children> children;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Payment> payments;
	
	public MyUser() {
		
	}



	@Override
	public String toString() {
		return "MyUser [userID=" + userID + ", username=" + username + ", password=" + password + ", fullname="
				+ fullname + ", birthday=" + birthday + ", gender=" + gender + ", email=" + email + ", image=" + image
				+ ", role=" + role + ", children=" + children + "]";
	}



	public Integer getUserID() {
		return userID;
	}


	public void setUserID(Integer userID) {
		this.userID = userID;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
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


	

	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public List<Children> getChildren() {
		return children;
	}


	public void setChildren(List<Children> children) {
		this.children = children;
	}
	
	
}
