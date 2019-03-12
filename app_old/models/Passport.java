package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Passport extends BaseModel {


    /**
     * The country issuing a passport
     */
    public String issueCountry;


    /**
     * The user profile associated with a passport
     */
    @ManyToOne
    public Profile profile;


    /**
     * The nationality associated with a passport
     */
    @ManyToOne
    public Nationality nationality;


    /**
     * The constructor for Passport
     * @param nationality the nationality associated with a passport
     */
    public Passport(Nationality nationality) {
        this.nationality = nationality;
        this.issueCountry = nationality.country;
    }


    public String getIssueCountry() {
        return issueCountry;
    }
}
