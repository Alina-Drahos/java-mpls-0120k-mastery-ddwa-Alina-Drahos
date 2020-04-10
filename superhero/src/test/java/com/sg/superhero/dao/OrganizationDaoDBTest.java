/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Organization;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author alinc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationDaoDBTest {

    @Autowired
    HeroDaoDB heroDao;

    @Autowired
    OrganizationDaoDB organizationDao;

    @Autowired
    LocationDaoDB locationDao;

    @Autowired
    SightingDaoDB sightingDao;

    @Autowired
    JdbcTemplate jdbc;

    public OrganizationDaoDBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Organization myOrganization = new Organization(0, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road");
        Organization org=organizationDao.addOrganization(myOrganization);
        Hero newHero1 = new Hero(0,"Garfield","Eating everything","Save_Lasagne");
        newHero1.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero1);
    }

    @After
    public void tearDown() {
        jdbc.update("Delete From members");
        jdbc.update("Delete From hero");
        jdbc.update("Delete From organization");
    }

    /**
     * Test of addOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetAndAddOrganization() {
        Organization myOrganization = new Organization(0, "EVIL_DOG", "Populate the world with evil housedogs", "1250 Super Secret Road");
        Organization newOrganization = organizationDao.addOrganization(myOrganization);
        assertEquals(myOrganization, newOrganization);
        int latestId = organizationDao.getAllOrganization().get(1).getOrganizationId();
        Organization anOrganization = organizationDao.getOrganization(latestId);
        assertEquals(latestId, anOrganization.getOrganizationId());
        assertTrue("EVIL_DOG".equals(anOrganization.getName()));
        assertEquals(newOrganization,anOrganization);
    }

    /**
     * Test of getOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetOrganization() {
        Organization allOrgs = organizationDao.getAllOrganization().get(0);
        int id = allOrgs.getOrganizationId();
        Organization org = organizationDao.getOrganization(allOrgs.getOrganizationId());
        assertEquals(org,allOrgs);
        assertEquals(org, new Organization(id, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road"));
        assertEquals(org.getAddress(), "1200 Super Secret Road");
    }

    /**
     * Test of updateOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testUpdateOrganization() {
        Organization myOrganization = new Organization(0, "EVIL_SQUIRREL", "Populate the world with evil nuts", "1250 Super Nutty Road");
        Organization orgToAdd = organizationDao.addOrganization(myOrganization);
        int id=orgToAdd.getOrganizationId();
        Organization updatedOrganization = organizationDao.updateOrganization(orgToAdd.getOrganizationId(), new Organization(id, "EVIL_CLOUD", "Populate the world with RAINSHOWERS", "1250 Super Rainy Road"));
        assertEquals(updatedOrganization, new Organization(orgToAdd.getOrganizationId(), "EVIL_CLOUD", "Populate the world with RAINSHOWERS", "1250 Super Rainy Road"));
    }

    /**
     * Test of deleteOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testDeleteOrganizationById() {
        Organization myOrganization = new Organization(0, "EVIL_SQUIRREL", "Populate the world with evil nuts", "1250 Super Nutty Road");
        Organization addedOrg = organizationDao.addOrganization(myOrganization);
        organizationDao.deleteOrganization(addedOrg.getOrganizationId());

        for (Organization org : organizationDao.getAllOrganization()) {
            assertNotEquals(org.getOrganizationId(), addedOrg.getOrganizationId());
        }
    }

    /**
     * Test of getAllOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetAllOrganization() {
        Organization myOrganization = new Organization(0, "EVIL_SQUIRREL", "Populate the world with evil nuts", "1250 Super Nutty Road");
        Organization addedOrg = organizationDao.addOrganization(myOrganization);
        List<Organization> allOrgs= organizationDao.getAllOrganization();
        assertEquals(2,allOrgs.size());   
    }

    /**
     * Test of getallOrganizationsForHero method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetallOrganizationsForHero() {
        Hero myHero=heroDao.getAllHeros().get(0);
        List<Organization>orgs=organizationDao.getallOrganizationsForHero(myHero);
        Objects.equals(organizationDao.getAllOrganization(),(orgs));
    }

}
