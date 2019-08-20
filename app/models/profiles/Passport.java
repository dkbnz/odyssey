package models.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Passport extends BaseModel {

    @JsonIgnore
    @ManyToMany
    private List<Profile> profiles;

    private String country;

    public String getCountry() {
        return country;
    }

}
