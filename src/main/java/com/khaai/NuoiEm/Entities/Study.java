package com.khaai.NuoiEm.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="study")
public class Study {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="study_id")
    private Integer studyID;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Children children;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolYear;
    
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    
    @Column(nullable=false, name="enabled")
	private Boolean enabled;
    
    @Column(nullable=false, name="count")
   	private Integer count;

    public Study() {}



	public Study(Integer studyID, Children children, Grade grade, SchoolYear schoolYear, School school, Boolean enabled,
			Integer count) {
		super();
		this.studyID = studyID;
		this.children = children;
		this.grade = grade;
		this.schoolYear = schoolYear;
		this.school = school;
		this.enabled = enabled;
		this.count = count;
	}


	public Integer getStudyID() {
		return studyID;
	}


	public void setStudyID(Integer studyID) {
		this.studyID = studyID;
	}



	public Children getChildren() {
		return children;
	}

	public void setChildren(Children children) {
		this.children = children;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}




	public School getSchool() {
		return school;
	}


	public void setSchool(School school) {
		this.school = school;
	}



	public Boolean getEnabled() {
		return enabled;
	}



	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}



	public Integer getCount() {
		return count;
	}



	public void setCount(Integer count) {
		this.count = count;
	}

	
   
}
