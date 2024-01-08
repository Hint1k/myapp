package com.example.myapp.controller;

import com.example.myapp.entity.Courier;
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
@RequestMapping("/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

    public CourierController() {
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showList")
    public String getCourierList(Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        return "courier-list";
    }

    @GetMapping("/addCourier")
    public String addCourier(Model model) {
        Courier courier = new Courier();
        model.addAttribute("courier", courier);
        return "courier-form";
    }

    @PostMapping("/saveCourier")
    public String saveCourier(
            @Valid @ModelAttribute("courier") Courier courier,
            BindingResult result) {
        if (result.hasErrors()) {
            return "courier-form";
        }
        courierService.saveCourier(courier);
        return "redirect:/courier/showList";
    }

    @GetMapping("/updateCourier")
    public String updateCourier(@RequestParam("courierId") Integer id,
                                Model model) {
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