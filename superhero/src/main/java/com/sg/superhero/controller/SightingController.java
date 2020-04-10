package com.sg.superhero.controller;

import com.sg.superhero.dao.HeroDao;
import com.sg.superhero.dao.LocationDao;
import com.sg.superhero.dao.OrganizationDao;
import com.sg.superhero.dao.SightingDao;
import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Location;
import com.sg.superhero.dto.Sighting;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class SightingController {

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.allSightings();
        List<Location> locations = locationDao.allLocations();
        List<Hero> heros = heroDao.getAllHeros();
        model.addAttribute("sightings", sightings);
        model.addAttribute("locations", locations);
        model.addAttribute("heros", heros);
        model.addAttribute("sighting", new Sighting());
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(@Valid Sighting sighting, BindingResult result, HttpServletRequest request, Model model) {

        String locationId = request.getParameter("locationId");
        String[] heroIds = request.getParameterValues("heroId");
        String date = request.getParameter("date");
        sighting.setDate(LocalDateTime.parse(date));

        sighting.setLocation(locationDao.getLocation(Integer.parseInt(locationId)));

        List<Hero> heroes = new ArrayList<>();

        for (String heroId : heroIds) {
            heroes.add(heroDao.getHeroById(Integer.parseInt(heroId)));
        }
        
        sighting.setHeros(heroes);
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";

    }

    @GetMapping("detailSighting")
    public String sightingDetail(Integer id, Model model
    ) {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Hero> heroes = heroDao.getAllHeros();
        model.addAttribute("sighting", sighting);
        model.addAttribute("heroes", heroes);
        return "detailSighting";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id
    ) {
        sightingDao.deleteASighting(id);
        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model
    ) {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Location> locations = locationDao.allLocations();
        List<Hero> heros = heroDao.getAllHeros();
        model.addAttribute("sighting", sighting);
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heros);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(HttpServletRequest request
    ) {
        Sighting sighting = new Sighting();
        sighting.setSightingId(Integer.parseInt(request.getParameter("sightingId")));
        String locationId = request.getParameter("locationId");
        String[] heroIds = request.getParameterValues("heroId");
        String date = request.getParameter("date");
        sighting.setDate(LocalDateTime.parse(date));

        sighting.setLocation(locationDao.getLocation(Integer.parseInt(locationId)));

        List<Hero> heroes = new ArrayList<>();
        for (String heroId : heroIds) {
            heroes.add(heroDao.getHeroById(Integer.parseInt(heroId)));
        }

        sighting.setHeros(heroes);
        sightingDao.updateASighting(sighting.getSightingId(), sighting);
        return "redirect:/sightings";
    }

    @GetMapping("10LatestSightings")
    public String get10LatestSightings(Model model
    ) {
        List<Sighting> sightings = sightingDao.get10LatestSightings();

        model.addAttribute("sightings", sightings);
        return "10LatestSightings";
    }
    
    @GetMapping("contactUs")
    public String contactUs(){
        return "contactUs";
    }

}
