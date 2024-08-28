package com.khaai.NuoiEm.Entities;

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
@Table(name="grade")
public class Grade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="grade_id")
	private Integer gradeID;
	
	@Column(nullable=false,length=10, name="grade_name")
	private String gradeName;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies;

	public Grade() {
		
	}
	


	public Grade(Integer gradeID, String gradeName, Boolean enabled, List<Study> studies) {
		super();
		this.gradeID = gradeID;
		this.gradeName = gradeName;
		this.enabled = enabled;
		this.studies = studies;
	}



	public Integer getGradeID() {
		return gradeID;
	}

	public void setGradeID(Integer gradeID) {
		this.gradeID = gradeID;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}



	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}



	public Boolean getEnabled() {
		return enabled;
	}



	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
