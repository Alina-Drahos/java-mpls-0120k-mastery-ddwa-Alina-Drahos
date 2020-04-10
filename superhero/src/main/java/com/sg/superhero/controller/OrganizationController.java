package com.sg.superhero.controller;

import com.sg.superhero.dao.HeroDao;
import com.sg.superhero.dao.LocationDao;
import com.sg.superhero.dao.OrganizationDao;
import com.sg.superhero.dao.SightingDao;
import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Organization;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
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
public class OrganizationController {

    //Set<ConstraintViolation<Organization>> violations = new HashSet<>();

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    SightingDao sightingDao;

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationDao.getAllOrganization();
        model.addAttribute("organizations", organizations);
        model.addAttribute("organization", new Organization());
        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(@Valid Organization organization, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("organizations", organizationDao.getAllOrganization());
            return "organizations";
        }
        organizationDao.addOrganization(organization);
        return "redirect:/organizations";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        organizationDao.deleteOrganization(id);
        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrganization(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationDao.getOrganization(id);
        model.addAttribute("organization", organization);
        
        return "editOrganization";
    }

    @PostMapping("editOrganization")
    public String performEditOrganization(@Valid Organization organization, BindingResult result,HttpServletRequest request,Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (result.hasErrors()) {
            model.addAttribute("organizations", organizationDao.getAllOrganization());
            return "editOrganization";
        }
        organizationDao.updateOrganization(organization.getOrganizationId(), organization);
        
        

        return "redirect:/organizations";
    }
}
