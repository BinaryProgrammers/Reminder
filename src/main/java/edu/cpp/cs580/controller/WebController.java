package edu.cpp.cs580.controller;

import edu.cpp.cs580.manager.UsersManager;
import edu.cpp.cs580.service.EmailService;
import edu.cpp.cs580.util.Users;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;

<<<<<<< HEAD
import java.net.SocketPermission;
import java.util.ArrayList;
=======
import edu.cpp.cs580.App;
import edu.cpp.cs580.data.GpsProduct;
import edu.cpp.cs580.data.User;
import edu.cpp.cs580.data.provider.GpsProductManager;
import edu.cpp.cs580.data.provider.UserManager;


/**
 * This is the controller used by Spring framework.
 * <p>
 * The basic function of this controller is to map
 * each HTTP API Path to the correspondent method.
 *
 */
>>>>>>> refs/remotes/csupomona-cs580/master

@RestController
public class WebController {

<<<<<<< HEAD
    @Autowired
    UsersManager usersManager;

    @Autowired
    EmailService service;

    private static final Logger Logger = LoggerFactory.getLogger(WebController.class);

    @RequestMapping(value = "/databasetesting", method = RequestMethod.GET)
    ArrayList<Users> databaseTesting() {
        //ArrayList<Users> userss1 = (ArrayList<Users>) usersManager.findByName("Hardeep Singh");
        ArrayList<Users> userss = (ArrayList<Users>) usersManager.findAll();
        return userss;
    }


    @RequestMapping(value = "/valid/{uName}", method = RequestMethod.GET)
    String validateInput(@PathVariable("uName") String uName) {
        if (uName.contains("@")) {
            return "Valid";
        } else {
            return "Invalid";
        }
    }

    @RequestMapping(value = "/encrypt/{password}", method = RequestMethod.GET)
    String encrypt(@PathVariable("password") String password) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        return encryptedPassword;
    }

    @RequestMapping(value = "/sendsms")
    ModelAndView loadSendEmail() {
        ModelAndView modelAndView = new ModelAndView("sms");
        return modelAndView;
    }

    @RequestMapping(value = "/processSMS/{number}", method = RequestMethod.GET)
    String sendEmail(@PathVariable("number") String number,
                     @RequestParam("provider") String provider,
                     @RequestParam("subject") String subject,
                     @RequestParam("message") String message) {

        //Use service class to send email
        try {
            service.sendSMS(number, provider, subject, message);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    //Registration
    @RequestMapping(value = "/registration")
    ModelAndView loadRegistration() {
        ModelAndView modelAndView = new ModelAndView("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/processRegistration/{rName}", method = RequestMethod.GET)
    String register(@PathVariable("rName") String rName,
                    @RequestParam("rEmail") String rEmail,
                    @RequestParam("rPassword") String rPassword,
                    @RequestParam("rProvider") String rProvider,
                    @RequestParam("rNumber") String rNumber) {

        String code;
        Users users;
        try {
            code = service.registerUser(rName, rEmail, rProvider, rNumber);

            //save to the database make a new entry
            users = new Users(rName, rEmail, rPassword, rProvider, rNumber, code, false);
            usersManager.save(users);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    //load validation
    @RequestMapping(value = "/verificationCode")
    ModelAndView verificationCode() {
        ModelAndView modelAndView = new ModelAndView("verificationCode");
        return modelAndView;
    }

    //Verify Code
    @RequestMapping(value = "/validateCode/{vCode}", method = RequestMethod.GET)
    String validateCode(@PathVariable("vCode") String vCode) {
        ArrayList<Users> usersList = (ArrayList<Users>) usersManager.findByVcode(vCode);
        if(!usersList.isEmpty()) {
            Users users = usersList.get(0);
            users.setVerified(true);
            usersManager.save(users);
            return users.getName() + " (" + users.getEmail() + ")";
        }
        return "invalid";
    }
    
    @RequestMapping(value = "/log/{logString}", method = RequestMethod.GET)
    String logger(@PathVariable("logString") String logString) {
        Logger.debug(logString);
        return "Successfully Logged " + logString;
    }

    @RequestMapping(value = "/login")
    ModelAndView logIn() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @RequestMapping(value = "/success")
    ModelAndView success() {
        ModelAndView modelAndView = new ModelAndView("success");
        return modelAndView;
    }


    @RequestMapping(value = "/logOutSuccess")
    ModelAndView logOutSuccessMV() {
        ModelAndView modelAndView = new ModelAndView("logOutSuccess");
        return modelAndView;
    }

    @RequestMapping(value = "/forgotPassword")
    ModelAndView forgotPasswordMV() {
        ModelAndView modelAndView = new ModelAndView("forgotPassword");
        return modelAndView;
    }
}

	 
	
=======
	/**
	 * When the class instance is annotated with
	 * {@link Autowired}, it will be looking for the actual
	 * instance from the defined beans.
	 * <p>
	 * In our project, all the beans are defined in
	 * the {@link App} class.
	 */
	@Autowired
	private UserManager userManager;
	@Autowired
	private GpsProductManager gpsManager;

	/**
	 * This is a simple example of how the HTTP API works.
	 * It returns a String "OK" in the HTTP response.
	 * To try it, run the web application locally,
	 * in your web browser, type the link:
	 * 	http://localhost:8080/cs580/ping
	 */
	@RequestMapping(value = "/cs580/healthcheck", method = RequestMethod.GET)
	String healthCheck() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return "OK-CS580 in Class Demo";
	}

	@RequestMapping(value = "/cs580/gpslist", method = RequestMethod.GET)
	List<GpsProduct> listGps() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return gpsManager.listGps();
	}

	/**
	 * This is a simple example of how to use a data manager
	 * to retrieve the data and return it as an HTTP response.
	 *
	 * <p>
	 * Note, when it returns from the Spring, it will be
	 * automatically converted to JSON format.
	 * <p>
	 * Try it in your web browser:
	 * 	http://localhost:8080/cs580/user/user101
	 */
	@RequestMapping(value = "/cs580/user/{userId}", method = RequestMethod.GET)
	User getUser(@PathVariable("userId") String userId) {
		User user = userManager.getUser(userId);
		return user;
	}

	/**
	 * This is an example of sending an HTTP POST request to
	 * update a user's information (or create the user if not
	 * exists before).
	 *
	 * You can test this with a HTTP client by sending
	 *  http://localhost:8080/cs580/user/user101
	 *  	name=John major=CS
	 *
	 * Note, the URL will not work directly in browser, because
	 * it is not a GET request. You need to use a tool such as
	 * curl.
	 *
	 * @param id
	 * @param name
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/cs580/user/{userId}", method = RequestMethod.POST)
	User updateUser(
			@PathVariable("userId") String id,
			@RequestParam("name") String name,
			@RequestParam(value = "major", required = false) String major) {
		User user = new User();
		user.setId(id);
		user.setMajor(major);
		user.setName(name);
		userManager.updateUser(user);
		return user;
	}

	/**
	 * This API deletes the user. It uses HTTP DELETE method.
	 *
	 * @param userId
	 */
	@RequestMapping(value = "/cs580/user/{userId}", method = RequestMethod.DELETE)
	void deleteUser(
			@PathVariable("userId") String userId) {
		userManager.deleteUser(userId);
	}

	/**
	 * This API lists all the users in the current database.
	 *
	 * @return
	 */
	@RequestMapping(value = "/cs580/users/list", method = RequestMethod.GET)
	List<User> listAllUsers() {
		return userManager.listAllUsers();
	}

	/*********** Web UI Test Utility **********/
	/**
	 * This method provide a simple web UI for you to test the different
	 * functionalities used in this web service.
	 */
	@RequestMapping(value = "/cs580/home", method = RequestMethod.GET)
	ModelAndView getUserHomepage() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("users", listAllUsers());
		return modelAndView;
	}

}
>>>>>>> refs/remotes/csupomona-cs580/master
