package com.sg.superhero.controller;

import com.sg.superhero.dao.HeroDao;
import com.sg.superhero.dao.LocationDao;
import com.sg.superhero.dao.OrganizationDao;
import com.sg.superhero.dao.SightingDao;
import com.sg.superhero.dto.Location;
import com.sg.superhero.dto.Organization;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alinc
 */
@Controller
public class LocationController {

    @Autowired
    LocationDao locationDao;

    @Autowired
    HeroDao heroDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.allLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("location", new Location());
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(@Valid Location location, BindingResult result, HttpServletRequest request, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("locations", locationDao.allLocations());
            return "locations";
        }
        locationDao.addLocation(location);

        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        locationDao.deleteLocation(id);
        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocation(id);

        model.addAttribute("location", location);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, BindingResult result, HttpServletRequest request, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("locations", locationDao.allLocations());
            return "editlocation";
        }
        locationDao.updateLocation(location.getLocationId(), location);

        return "redirect:/locations";
    }

}
