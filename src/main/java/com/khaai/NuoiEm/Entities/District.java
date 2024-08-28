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
@Table(name="district")
public class District {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="district_id")
	private Integer districtID;
	
	@Column(nullable=false,length=20, name="district_name")
	private String districtName;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;
	
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<School> schools;
	
	public District() {
		
	}


	public District(Integer districtID, String districtName, Boolean enabled, Province province, List<School> schools) {
		super();
		this.districtID = districtID;
		this.districtName = districtName;
		this.enabled = enabled;
		this.province = province;
		this.schools = schools;
	}






	public Integer getDistrictID() {
		return districtID;
	}

	public void setDistrictID(Integer districtID) {
		this.districtID = districtID;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}


	public List<School> getSchools() {
		return schools;
	}


	public void setSchools(List<School> schools) {
		this.schools = schools;
	}


	public Boolean getEnabled() {
		return enabled;
	}


	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
	
}
