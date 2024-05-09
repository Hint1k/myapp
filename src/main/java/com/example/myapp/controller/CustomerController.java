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