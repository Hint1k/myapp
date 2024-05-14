package com.example.myapp.controller;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Customer;
import com.example.myapp.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // cutting off the spaces entered by user to avoid errors
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/customers/customer-form")
    public String showRegistrationForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer-form";
    }

    @PostMapping("/customers")
    public String saveCustomer(
            @Valid @ModelAttribute("customer") Customer customer,
            BindingResult result) {
        if (result.hasErrors()) {
            return "customer-form";
        }

        List<Customer> customers = customerService.getCustomers();
        Map<String, String> names = new LinkedHashMap<>();
        List<String> emails = new ArrayList<>();
        List<String> usernames = new ArrayList<>();

        for (Customer oldCustomer : customers) {
            emails.add(oldCustomer.getEmail());
            usernames.add(oldCustomer.getUsername());
            names.put(oldCustomer.getFirstName(),
                    oldCustomer.getLastName());
        }

        if (names.entrySet().stream().anyMatch(entry ->
                entry.getKey().equals(customer.getFirstName()) &&
                        entry.getValue().equals(customer.getLastName()))) {
            String message = "This customer already exists in the database";
            result.rejectValue("firstName", "customer.exists", message);
            result.rejectValue("lastName", "customer.exists", message);
            return "customer-form";
        }

        if (emails.contains(customer.getEmail())) {
            result.rejectValue("email", "email.exists",
                    "This email already exists in the database");
            return "customer-form";
        }

        if (usernames.contains(customer.getUsername())) {
            result.rejectValue("username", "username.exists",
                    "This username already exists in the database");
            return "customer-form";
        }

        customerService.saveCustomer(customer);
        return "redirect:/registration-successful";
    }

    @GetMapping("/customers/order") //order here used as noun
    public String showOrderFormPage(Model model) {
        Address address = new Address();
        model.addAttribute("address", address);
        return "order-form";
    }
}