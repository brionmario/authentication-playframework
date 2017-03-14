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
     * Renders a variable message page that can be used to display messages and feedback to the user.
     *
     * @param message The massage to be passed to the user
     * @param heading The greeting or salutation
     * @param title The title of the page
     * @return Returns the message.scala.html page with the params
     */
    public Result messagePage(String title , String heading , String message){

        return ok(views.html.message.render(title,heading,message));
    }

    /**
     * Method to add a new user to the databse
     * @return Returns a success message
     */
    public Result addUser() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        //string to store the success message to the user
        String success = "Your account was created successfully";


        //add the values posted by the form and create a new user.
        //P.S : data has to be passed according to the order of parameters taken by the model constructor.
        userDB.add(new User(requestData.get("firstName"), requestData.get("lastName"),
                requestData.get("email"), requestData.get("password")));

        //return ok("Account was created successfully.");
        return redirect(routes.AuthenticationController.messagePage("Success" , "Congratulations!" , success));
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
        String fName;
        String lName;
        String fullName = null;

        User loggeUuser = null;
        boolean isUserFound = false;

        //custom error message to be shown to the user
        String errorMessage = "We couldn't locate your account. Please try again.";

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

        //return ok("Sorry! We couldn't locate your account. Please try again.");
        return redirect(routes.AuthenticationController.messagePage("Error" , "Sorry!" ,errorMessage));

    }


    public Result getUsers() {
        return ok(toJson(userDB).toString());
    }
}
