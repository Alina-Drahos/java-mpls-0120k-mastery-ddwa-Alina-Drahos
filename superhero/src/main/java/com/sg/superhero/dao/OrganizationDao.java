/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Organization;
import java.util.List;

/**
 *
 * @author alinc
 */
public interface OrganizationDao {

    Organization addOrganization(Organization organization);

    Organization getOrganization(int organizationId);
    
    Organization updateOrganization(int organizationId, Organization organizationToUpdate);
    
    void deleteOrganization(int OrganizationId);
    
    List<Organization> getAllOrganization();
    
    List<Organization> getallOrganizationsForHero(Hero hero);

}
