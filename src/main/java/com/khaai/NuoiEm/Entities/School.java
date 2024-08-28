package com.khaai.NuoiEm.Entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name="school")
public class School {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="school_id")
	private Integer schoolID;
	
	@Column(nullable=false,length=100, name="school_name")
	private String schoolName;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;
	
	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies;
	
	public School() {
	
	}


	public School(Integer schoolID, String schoolName, Boolean enabled, District district, List<Study> studies) {
		super();
		this.schoolID = schoolID;
		this.schoolName = schoolName;
		this.enabled = enabled;
		this.district = district;
		this.studies = studies;
	}




	public Integer getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(Integer schoolID) {
		this.schoolID = schoolID;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
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
