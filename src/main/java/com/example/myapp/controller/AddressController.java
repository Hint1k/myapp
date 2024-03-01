package com.example.myapp.controller;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import com.example.myapp.service.AddressService;
import com.example.myapp.service.CourierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private AddressService addressService;

    public AddressController() {
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/addresses")
    public String getAddressList(Model model) {
        List<Address> addresses = addressService.getAddresses();
        model.addAttribute("addresses", addresses);
        return "address-list";
    }

    @GetMapping("/addresses/address")
    public String addAddress(Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        Address address = new Address();
        model.addAttribute("address", address);
        return "address-form";
    }

    @PostMapping("/addresses")
    public String saveAddress(
            @Valid @ModelAttribute("address") Address address,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            /* need two following lines of code to save courier choice
             in case of validation mistake in other field */
            List<Courier> couriers = courierService.getCouriers();
            model.addAttribute("couriers", couriers);
            return "address-form";
        }
        addressService.saveAddress(address);
        return "redirect:/api/addresses";
    }

    @PutMapping("/addresses/{id}")
    public String updateAddress(@PathVariable("id") Integer id,
                                Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        Address address = addressService.getAddress(id);
        model.addAttribute("address", address);
        return "address-form";
    }

    @DeleteMapping("/addresses/{id}")
    public String deleteAddress(@PathVariable("id") Integer id) {
        addressService.deleteAddress(id);
        return "redirect:/api/addresses";
    }
}