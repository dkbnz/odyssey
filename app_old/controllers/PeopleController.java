package controllers;

import models.Person;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to for the People destinationsPage functionality
 */
public class PeopleController extends Controller {


    List<Person> people = new ArrayList<>();


    /**
     * Add person objects to a list for displaying in table
     */
    public PeopleController() {
        Person newPerson1 = new Person(1, "Francisca", "", "Cassely", "15-05-94",
                "Female", "Thailand", "Thailand", "backpacker");
        Person newPerson2 = new Person(2, "Ezmeralda", "", "Goshawk", "03-09-76",
                "Female", "Venezuela", "Venezuela", "functional/business traveller");
        Person newPerson3 = new Person(3, "Leilah", "", "Ironmonger", "13-10-87",
                "Female", "Canada", "Canada", "groupies");
        Person newPerson4 = new Person(4, "Darrelle", "", "Dyos", "16-08-95",
                "Female", "Indonesia", "Indonesia", "thrillseeker");
        Person newPerson5 = new Person(5, "Fayre", "", "Sibthorp", "09-10-85",
                "Female", "Jordan", "Jordan", "thrillseeker");
        Person newPerson6 = new Person(6, "Ileana", "", "Hartus", "18-05-92",
                "Female", "Honduras", "Honduras", "groupies");
        Person newPerson7 = new Person(7, "Jackie", "", "Aird", "14-12-96",
                "Female", "Indonesia", "Indonesia", "frequent weekender");
        people.add(newPerson1);
        people.add(newPerson2);
        people.add(newPerson3);
        people.add(newPerson4);
        people.add(newPerson5);
        people.add(newPerson6);
        people.add(newPerson7);
    }
    /**
     * Handle default path requests, parses list of people to people.scala.html.
     * @return
     */
    public Result people() {
        System.out.println(people);
        return ok(views.html.people.render(people));
    }

}
