package com.ftg2021.effo2021V1.Util.DataModels;

public class SingleJobDataModel {

    int id, companyId;
    String jobTitle;
    String jobDescription;
    String location;
    String jobType;
    String jobPlace;
    String qualification;
    String experience;
    String salary;
    String date;
    String companyName;
    int applicationReceived;
    int status, maxEmployeeCount;
    String searchStatus;
    String communicationSkill;

    public SingleJobDataModel() {
    }

    public SingleJobDataModel(int id, String title, String description, String type, String place, int employeeCount, String qualification, String experience, String salary, int status, String datePosted, String location,String communicationSkill) {

        this.id = id;
        this.jobTitle = title;
        this.jobDescription = description;
        this.location = location;
        this.jobType = type;
        this.jobPlace = place;
        this.maxEmployeeCount = employeeCount;
        this.qualification = qualification;
        this.experience = experience;
        this.salary = salary;
        this.applicationReceived = applicationReceived;
        this.date = datePosted;
        this.status = status;
        this.location = location;
        this.communicationSkill = communicationSkill;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationReceived() {
        return applicationReceived;
    }

    public void setApplicationReceived(int applicationReceived) {
        this.applicationReceived = applicationReceived;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobPlace() {
        return jobPlace;
    }

    public void setJobPlace(String jobPlace) {
        this.jobPlace = jobPlace;
    }

    public int getMaxEmployeeCount() {
        return maxEmployeeCount;
    }

    public void setMaxEmployeeCount(int maxEmployeeCount) {
        this.maxEmployeeCount = maxEmployeeCount;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(String searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getCommunicationSkill() {
        return communicationSkill;
    }

    public void setCommunicationSkill(String communicationSkill) {
        this.communicationSkill = communicationSkill;
    }
}
