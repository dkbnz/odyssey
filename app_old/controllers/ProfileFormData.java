package controllers;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import java.util.Date;
import java.util.List;

public class ProfileFormData {

    //@Constraints.Required
    private String firstName;
    //@Constraints.Required
    private String lastName;
    //@Constraints.Required
    private String username;
    //@Constraints.Required
    private String password;
    //@Constraints.Required
    private Date dateOfBirth;
    private List<String> nationalities;
    //@Constraints.Required
    private String gender;
    private List<String> passports;
    private List<String> traveller_types;

    public ProfileFormData() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getNationalities() {
        return nationalities;
    }

    public void addNationality(String nationality) {
        this.nationalities.add(nationality);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getPassports() {
        return passports;
    }

    public void addPassport(String passport) {
        this.passports.add(passport);
    }

    public List<String> getTraveller_types() {
        return traveller_types;
    }

    public void setTraveller_types(List<String> traveller_types) {
        this.traveller_types = traveller_types;
    }
}
