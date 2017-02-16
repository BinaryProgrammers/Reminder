package edu.cpp.cs580.controller;

import edu.cpp.cs580.manager.BillManager;
import edu.cpp.cs580.manager.UsersManager;
import edu.cpp.cs580.service.EmailService;
import edu.cpp.cs580.util.Bill;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@RestController
public class WebController {


    @Autowired
    UsersManager usersManager;

    @Autowired
    BillManager billManager;

    @Autowired
    EmailService service;

    private static final Logger Logger = LoggerFactory.getLogger(WebController.class);

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

        try {
            String code = service.registerUser(rName, rEmail, rProvider, rNumber);

            //save to the database make a new entry
            Users users = new Users(rName, rEmail, rPassword, rProvider, rNumber, code, false);
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
        if (!usersList.isEmpty()) {
            Users users = usersList.get(0);
            users.setVerified(true);
            usersManager.save(users);
            return users.getName() + " (" + users.getEmail() + ")";
        }
        return "invalid";
    }

    //Bill Info
    @RequestMapping(value = "/bill/{name}", method = RequestMethod.GET)
    String saveBill(@PathVariable("name") String name,
                    @RequestParam("amount") String amount,
                    @RequestParam("duedate") String duedate) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
            Date date = format.parse(duedate);

            Bill bill = new Bill(name, amount, date, false);
            billManager.save(bill);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/bill")
    ModelAndView getAllBills() {
        ArrayList<Bill> bills = (ArrayList<Bill>) billManager.findAll();
        ModelAndView modelAndView = new ModelAndView("bill");
        modelAndView.addObject("bills", bills);
        return modelAndView;
    }

    //Change paid status of bill
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.POST)
    String updateBill(@PathVariable("id") String id) {
        ArrayList<Bill> bills = (ArrayList<Bill>) billManager.findById(Integer.parseInt(id));
        if(!bills.isEmpty()) {
            Bill bill = bills.get(0);
            bill.setStatus(true);
            billManager.save(bill);
            return "success";
        }
        return "error";
    }

    //delete bill
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.DELETE)
    String deleteBill(@PathVariable("id") String id) {
        try {
            billManager.delete(Integer.parseInt(id));
        } catch (Exception e) {
            return "error";
        }
        return "success";
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

	 
	
