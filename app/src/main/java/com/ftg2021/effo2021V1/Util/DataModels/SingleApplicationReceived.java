package com.ftg2021.effo2021V1.Util.DataModels;

public class SingleApplicationReceived {
    int id,employeeId,jobId,status;
    String employeeName,jobTitle,experience,date,salary;

    public SingleApplicationReceived() {
    }

    public SingleApplicationReceived(int id, int employeeId, int jobId, String employeeName, String jobTitle, String experience, String date,int status) {
        this.id = id;
        this.employeeId = employeeId;
        this.jobId = jobId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.experience = experience;
        this.date = date;
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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getemployeeName() {
        return employeeName;
    }

    public void setemployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
