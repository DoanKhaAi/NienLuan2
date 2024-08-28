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
@Table(name="province")
public class Province {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="province_id")
	private Integer provinceID;
	
	@Column(nullable=false,length=20, name="province_name")
	private String provinceName;
	
	@Column(nullable=false, name="enabled")
	private Boolean enabled;
	
	@OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<District> districts;
	
	public Province() {
		
	}

	

	public Province(Integer provinceID, String provinceName, Boolean enabled, List<District> districts) {
		super();
		this.provinceID = provinceID;
		this.provinceName = provinceName;
		this.enabled = enabled;
		this.districts = districts;
	}



	public Integer getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(Integer provinceID) {
		this.provinceID = provinceID;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}



	public Boolean getEnabled() {
		return enabled;
	}



	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	

	

}
