package models;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.List;

public class Profile_FrontEnd {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date dateOfBirth;
    private List<String> nationalities;
    private String gender;
    private List<String> passports;
    private DateTime creationTime;

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

    public Date getDateofBirth() {
        return dateOfBirth;
    }

    public String getFormattedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = format.format(dateOfBirth);
        return formattedDate;
    }

    public int getAge() {
        long ageMillis = new Date().getTime() - dateOfBirth.getTime();
        double age = ageMillis / 3.15576e+10;
        return(int) age ;
    }

    public List<String> getNationalities() {
        return nationalities;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getPassports() {
        return passports;
    }

    public Profile (){
    }

    public Profile (String firstName, String lastName, String username, String password, Date dateOfBirth, List<String> nationalities, String gender, List<String> passports) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password= password;
        this.dateOfBirth = dateOfBirth;
        this.nationalities = nationalities;
        this.gender = gender;
        this.passports = passports;
        this.creationTime = DateTime.now();
    }
}
