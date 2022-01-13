package com.ftg2021.effo2021V1.Util.DataModels;

public class SingleCompanyDataModel {

    int id;
    String name,type,date,website,companyDocument,companyImage,email,contactNo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SingleCompanyDataModel() {
    }

    public SingleCompanyDataModel(int id, String name, String type, String date, String website, String companyDocument, String companyImage, String email) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.website = website;
        this.companyDocument = companyDocument;
        this.companyImage = companyImage;
        this.email=email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyDocument() {
        return companyDocument;
    }

    public void setCompanyDocument(String companyDocument) {
        this.companyDocument = companyDocument;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
