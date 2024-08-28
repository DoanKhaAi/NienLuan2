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
@Table(name="month_year")
public class MonthYear {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="month_year_id")
	private Integer monthYearID;
	
	@Column(nullable=false, name="month")
	private Integer month;
	
	@Column(nullable=false, name="year")
	private Integer year;
	
	
	@OneToMany(mappedBy = "monthYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
	
	public MonthYear() {
	
	}

	public MonthYear(Integer month, Integer year, List<Payment> payments) {
		super();
		this.month = month;
		this.year = year;
		this.payments = payments;
	}

	public Integer getMonthYearID() {
		return monthYearID;
	}

	public void setMonthYearID(Integer monthYearID) {
		this.monthYearID = monthYearID;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}


	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	
	
	
}
