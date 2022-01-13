package com.ftg2021.effo2021V1.Util.DataModels;

public class SingleEmployeeDataModel {

    int id, gender;//0=M 1=F
    String name, dateOfBirth, email, education, eduField, university;
    boolean isExperienced;//true=experienced || false=fresher
    String experience;
    String workedJobTitle, workedCompanyName;//if employee is experienced save old data here
    String resumeLink;

    public SingleEmployeeDataModel() {
    }

    public SingleEmployeeDataModel(int id, int gender, String name, String dateOfBirth, String email, String education, String eduField, String university, boolean isExperienced, String workedJobTitle, String workedCompanyName, String experience,String resumeLink) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.education = education;
        this.eduField = eduField;
        this.university = university;
        this.isExperienced = isExperienced;
        this.workedJobTitle = workedJobTitle;
        this.workedCompanyName = workedCompanyName;
        this.experience = experience;
        this.resumeLink = resumeLink;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEduField() {
        return eduField;
    }

    public void setEduField(String eduField) {
        this.eduField = eduField;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public boolean isExperienced() {
        return isExperienced;
    }

    public void setExperienced(boolean experienced) {
        isExperienced = experienced;
    }

    public String getWorkedJobTitle() {
        return workedJobTitle;
    }

    public void setWorkedJobTitle(String workedJobTitle) {
        this.workedJobTitle = workedJobTitle;
    }

    public String getWorkedCompanyName() {
        return workedCompanyName;
    }

    public void setWorkedCompanyName(String workedCompanyName) {
        this.workedCompanyName = workedCompanyName;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }
}
