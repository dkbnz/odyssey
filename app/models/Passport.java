package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Passport extends BaseModel {

    public String issueCountry;

    @ManyToOne
    public Profile profile;

    @ManyToOne
    public Nationality nationality;
}
