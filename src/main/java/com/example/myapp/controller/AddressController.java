package com.example.myapp.controller;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import com.example.myapp.service.AddressService;
import com.example.myapp.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private AddressService addressService;

    public AddressController() {
    }

    @GetMapping("/showList")
    public String getAddressList(Model model) {
        List<Address> addresses = addressService.getAddresses();
        model.addAttribute("addresses", addresses);
        return "address-list";
    }

    @GetMapping("/addAddress")
    public String addAddress(Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        Address address = new Address();
        model.addAttribute("address", address);
        return "address-form";
    }

    @PostMapping("/saveAddress")
    public String saveAddress(Address address) {
        addressService.saveAddress(address);
        return "redirect:/address/showList";
    }

    @GetMapping("/updateAddress")
    public String updateAddress(@RequestParam("addressId") Integer id, Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        Address address = addressService.getAddress(id);
        model.addAttribute("address", address);
        return "address-form";
    }

    @GetMapping("/deleteAddress")
    public String deleteAddress(@RequestParam("addressId") Integer id) {
        addressService.deleteAddress(id);
        return "redirect:/address/showList";
    }
}