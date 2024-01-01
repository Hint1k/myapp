package com.example.myapp.controller;

import com.example.myapp.entity.Courier;
import com.example.myapp.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courier")
public class CourierController {

    private CourierService courierService;

    public CourierController() {}

    @Autowired
    public CourierController(CourierService courierService){
        this.courierService = courierService;
    }

    @GetMapping("/showList")
    public String getCourierList(Model model){
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers",couriers);
        return "courier-list";
    }

    @GetMapping("/addCourier")
    public String addCourier(Model model) {
        Courier courier = new Courier();
        model.addAttribute("courier", courier);
        return "courier-form";
    }

    @PostMapping("/saveCourier")
    public String saveCourier(@ModelAttribute("courier") Courier courier) {
        courierService.saveCourier(courier);
        return "redirect:/courier/showList";
    }

    @GetMapping("/updateCourier")
    public String updateCourier(@RequestParam("courierId") Integer id, Model model) {
        Courier courier = courierService.getCourier(id);
        model.addAttribute("courier", courier);
        return "courier-form";
    }

    @GetMapping("/deleteCourier")
    public String deleteCourier(@RequestParam("courierId") Integer id) {
        courierService.deleteCourier(id);
        return "redirect:/courier/showList";
    }
}