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
@RequestMapping("/api")
public class CourierController {

    @Autowired
    private CourierService courierService;

    public CourierController() {
    }

    // cutting off the spaces entered by user to avoid errors
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/couriers")
    public String getCourierList(Model model) {
        List<Courier> couriers = courierService.getCouriers();
        model.addAttribute("couriers", couriers);
        return "courier-list";
    }

    @GetMapping("/couriers/courier")
    public String addCourier(Model model) {
        Courier courier = new Courier();
        model.addAttribute("courier", courier);
        return "courier-form";
    }

    @PostMapping("/couriers")
    public String saveCourier(
            @Valid @ModelAttribute("courier") Courier courier,
            BindingResult result) {
        if (result.hasErrors()) {
            return "courier-form";
        }
        courierService.saveCourier(courier);
        return "redirect:/api/couriers";
    }

    @PutMapping("/couriers/{id}")
    public String updateCourier(@PathVariable("id") Integer id,
                                Model model) {
        Courier courier = courierService.getCourier(id);
        model.addAttribute("courier", courier);
        return "courier-form";
    }

    @DeleteMapping("/couriers/{id}")
    public String deleteCourier(@PathVariable("id") Integer id) {
        courierService.deleteCourier(id);
        return "redirect:/api/couriers";
    }
}