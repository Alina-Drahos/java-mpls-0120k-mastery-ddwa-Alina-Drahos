package com.sg.superhero.controller;

import com.sg.superhero.dao.HeroDao;
import com.sg.superhero.dao.LocationDao;
import com.sg.superhero.dao.OrganizationDao;
import com.sg.superhero.dao.SightingDao;
import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Organization;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
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
public class HeroController {

    Set<ConstraintViolation<Hero>> violations = new HashSet<>();

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;

    @GetMapping("heroes")
    public String displayHeros(Model model) {
        List<Hero> heroes = heroDao.getAllHeros();
        List<Organization> organizations = organizationDao.getAllOrganization();
        model.addAttribute("heroes", heroes);
        model.addAttribute("organizations", organizations);
        model.addAttribute("hero", new Hero());
        return "heroes";
    }

    @PostMapping("addHero")
    public String addHero(@Valid Hero hero,BindingResult result, HttpServletRequest request, Model model ) {
        String[] organizationIds = request.getParameterValues("organizationId");

        List<Organization> organizations = new ArrayList<>();
        if (organizationIds != null) {
            for (String orgnizationId : organizationIds) {
                organizations.add(organizationDao.getOrganization(Integer.parseInt(orgnizationId)));
            }
        } else {
            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
        }

        if (result.hasErrors()) {
            model.addAttribute("heroes", heroDao.getAllHeros());
            model.addAttribute("organizations", organizationDao.getAllOrganization());
            return "heroes";
        }

        hero.setOrganizations(organizations);

        heroDao.addHero(hero);

        return "redirect:/heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        heroDao.deleteHeroById(id);

        return "redirect:/heroes";
    }

    @GetMapping("editHero")
    public String editHero(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroDao.getHeroById(id);
        hero.setOrganizations(organizationDao.getallOrganizationsForHero(hero));
        List<Organization> organizations = organizationDao.getAllOrganization();

        model.addAttribute("hero", hero);
        model.addAttribute("organizations", organizations);
        return "editHero";
    }

    @PostMapping("editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model) {
        String[] organizationIds = request.getParameterValues("organizationId");

        List<Organization> organizations = new ArrayList<>();

        if (organizationIds !=null) {
            for (String organizationId : organizationIds) {
                organizations.add(organizationDao.getOrganization(Integer.parseInt(organizationId)));
            }
        }else{
            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
        }
        hero.setOrganizations(organizations);

        if (result.hasErrors()) {
            model.addAttribute("organizations", organizationDao.getAllOrganization());
            model.addAttribute("hero", hero);
            return "editHero";
        }
        heroDao.updateHeroInformation(hero.getHeroId(), hero);

        return "redirect:/heroes";
    }
}
