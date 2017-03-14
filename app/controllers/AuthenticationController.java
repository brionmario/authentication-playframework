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

    public Result index() {

        return ok(views.html.login.render());
    }

    public Result signup() {

        return ok(views.html.signup.render());
    }

    public Result homePage(String fullname) {

        return ok(views.html.index.render(fullname));

    }

    public Result addUser() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        //add the values posted by the form and create a new user.
        //P.S : data has to be passed according to the order of parameters taken by the model constructor.
        userDB.add(new User(requestData.get("firstName"), requestData.get("lastName"), requestData.get("email"), requestData.get("password")));
        return ok("User Added Successfully");
    }


    public Result getUser() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        //store the values posted by the login form in Strings.
        String email = requestData.get("email");
        String password = requestData.get("password");

        //string to store the name of the user to be displayed in the welcome page.
        String fName = null;
        String lName = null;
        String fullName = null;

        User user = null;
        boolean flag = false;

        for (User u : userDB) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                user = u;
                fName = u.getFirstName();
                lName = u.getLastName();
                fullName = fName + " " + lName;
                flag = true;
            }
        }

        if (flag) {
            return redirect(routes.AuthenticationController.homePage(fullName));
            //return ok(user.getName() + " is logged in with ID " + user.getMailID());
        }
        return ok("No such user password combination found");
    }


    public Result getUsers() {
        return ok(toJson(userDB).toString());
    }
}
