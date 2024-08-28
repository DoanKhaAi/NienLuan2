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
@Table(name="school_year")
public class SchoolYear {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="schoolYear_id")
	private Integer schoolYearID;
	
	@Column(nullable=false,length=10, name="school_year_name", unique=true)
	private String schoolYearName;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies;
	
	public SchoolYear() {
		
	}
	



	public SchoolYear(Integer schoolYearID, String schoolYearName, Boolean enabled, List<Study> studies) {
		super();
		this.schoolYearID = schoolYearID;
		this.schoolYearName = schoolYearName;
		this.enabled = enabled;
		this.studies = studies;
	}




	public Integer getSchoolYearID() {
		return schoolYearID;
	}

	public void setSchoolYearID(Integer schoolYearID) {
		this.schoolYearID = schoolYearID;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
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
