/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Location;
import com.sg.superhero.dto.Organization;
import com.sg.superhero.dto.Sighting;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author alinc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeroDaoDBTest {
    
    @Autowired
    HeroDaoDB heroDao;
    
    @Autowired
    OrganizationDaoDB organizationDao;
    
    @Autowired
    LocationDaoDB locationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    JdbcTemplate jdbc;
    
    public HeroDaoDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Hero newHero1 = new Hero(0, "Garfield", "Eating everything", "Save_Lasagne");
        Organization myOrganization1 = new Organization(0, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road");
        organizationDao.addOrganization(myOrganization1);
        Organization myOrganization2 = new Organization(0, "EVIL_FLY", "Populate the world with FLIES", "1200 Super BUZZ Road");
        organizationDao.addOrganization(myOrganization2);
        newHero1.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero1);
    }
    
    @After
    public void tearDown() {
        jdbc.update("Delete From members");
        jdbc.update("Delete from heroSightings");
        jdbc.update("Delete from sighting");
        jdbc.update("Delete From hero");
        jdbc.update("Delete From organization");
        jdbc.update("Delete from location");
    }

    /**
     * Test of addHero method, of class HeroDaoDB.
     */
    @Test
    public void testAddAndGetHero() {
        Hero hero2 = new Hero(0, "Atticus_Cat", "Eating everything", "Protect_Tuna");
        List<Organization> allMyOrgs = organizationDao.getAllOrganization();
        hero2.setOrganizations(allMyOrgs);
        Hero aHero = heroDao.addHero(hero2);
        assertEquals(aHero, new Hero(aHero.getHeroId(), "Atticus_Cat", "Eating everything", "Protect_Tuna"));
        
        assertTrue(organizationDao.getAllOrganization().equals(aHero.getOrganizations()));
    }

    /**
     * Test of getHeroById method, of class HeroDaoDB.
     */
    @Test
    public void testGetHeroById() {
        Hero myHero = new Hero(0, "Gopher", "Digging up everything", "Save_DIRT");
        myHero.setOrganizations(organizationDao.getAllOrganization());
        Hero addedHero = heroDao.addHero(myHero);
        int id = addedHero.getHeroId();
        Hero getMyHero = heroDao.getHeroById(id);
        assertEquals(id, getMyHero.getHeroId());
        assertEquals(getMyHero, addedHero);
        
    }

    /**
     * Test of updateHeroInformation method, of class HeroDaoDB.
     */
    @Test
    public void testUpdateHeroInformation() {
        Hero myHero = new Hero(0, "Gopher", "Digging up everything", "Save_DIRT");
        myHero.setOrganizations(organizationDao.getAllOrganization());
        Hero addedHero = heroDao.addHero(myHero);
        addedHero.setName("Garfunkel");
        Hero updatedHero = heroDao.updateHeroInformation(addedHero.getHeroId(), addedHero);
        Hero getUpdatedHero = heroDao.getHeroById(updatedHero.getHeroId());
        assertEquals(getUpdatedHero, updatedHero);
        assertEquals(updatedHero.getHeroId(), addedHero.getHeroId());
        assertTrue(updatedHero.equals(addedHero));
    }

    /**
     * Test of deleteHeroById method, of class HeroDaoDB.
     */
    @Test
    public void testDeleteHeroById() {
        Hero myHero = new Hero(0, "Gopher", "Digging up everything", "Save_DIRT");
        myHero.setOrganizations(organizationDao.getAllOrganization());
        Hero addedHero = heroDao.addHero(myHero);
        Location firstLocation = new Location(0, "Hy-Vee Shakopee", "1451 Adams St S,Shakopee,MN", "Grocery Store",
                new BigDecimal("44.778492"), new BigDecimal("-93.538042"));
        locationDao.addLocation(firstLocation);
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30, 40, 50000));
        newSighting.setLocation(firstLocation);
        newSighting.setHeros(heroDao.getAllHeros());
        sightingDao.addSighting(newSighting);
        heroDao.deleteHeroById(addedHero.getHeroId());
        for (Hero hero : heroDao.getAllHeros()) {
            assertNotEquals(hero.getHeroId(), addedHero.getHeroId());
        }
        
    }

    /**
     * Test of getAllHeros method, of class HeroDaoDB.
     */
    @Test
    public void testGetAllHeros() {
        Hero myHero = new Hero(0, "Gopher", "Digging up everything", "Save_DIRT");
        myHero.setOrganizations(organizationDao.getAllOrganization());
        Hero addedHero = heroDao.addHero(myHero);
        assertEquals(2, heroDao.getAllHeros().size());
    }
    
}
