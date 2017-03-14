package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.ArrayList;

import static play.libs.Json.toJson;

/**
 * This Class does the authentication for a small sign in and sign up application using playfamework 2
 * as the back end.
 *
 * @author  Brion Mario
 * @version 1.0
 * @since   2017-03-03
 */

public class AuthenticationController extends Controller {

    static ArrayList<User> userDB = new ArrayList<>();
    //Helper to create HTML forms
    private final FormFactory formFactory;


    @Inject
    public AuthenticationController(FormFactory formFactory) {

        this.formFactory = formFactory;

    }

    /**
     * Renders the login page when the route file calls this method
     * @return Returns the login.scala.html page
     */
    public Result index() {

        return ok(views.html.login.render());
    }

    /**
     * Renders the signup page when the route file calls this method
     * @return Returns the signup.scala.html page
     */
    public Result signup() {

        return ok(views.html.signup.render());
    }


    /**
     * Renders the home page when the route file calls this method
     * @param fullName Full name ( First Name & Last Name ) of the user is passed
     *                 from the getUser() method  via this parameter
     * @return Returns the signup.scala.html page and the name of the user logged in
     */
    public Result homePage(String fullName) {

        return ok(views.html.index.render(fullName));

    }

    /**
     * Method to add a new user to the databse
     * @return Returns a success message
     */
    public Result addUser() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        //add the values posted by the form and create a new user.
        //P.S : data has to be passed according to the order of parameters taken by the model constructor.
        userDB.add(new User(requestData.get("firstName"), requestData.get("lastName"),
                requestData.get("email"), requestData.get("password")));

        return ok("User Added Successfully");
    }


    /**
     * This method checks if the user already has an account and if he/she does , It will redirect the user to the
     * Home page
     * @return If the user is found Returns the home page, and if not will display an error.
     */
    public Result getUser() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        //store the values posted by the login form in Strings.
        String email = requestData.get("email");
        String password = requestData.get("password");

        //string to store the name of the user to be displayed in the welcome page.
        String fName = null;
        String lName = null;
        String fullName = null;

        User loggeUuser = null;
        boolean isUserFound = false;

        //iterating through the user database
        for (User user : userDB) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                loggeUuser = user;
                fName = user.getFirstName();
                lName = user.getLastName();
                fullName = fName + " " + lName;
                isUserFound = true;
            }
        }

        if (isUserFound) {
            
            return redirect(routes.AuthenticationController.homePage(fullName));

        }
        return ok("No such user password combination found");
    }


    public Result getUsers() {
        return ok(toJson(userDB).toString());
    }
}
