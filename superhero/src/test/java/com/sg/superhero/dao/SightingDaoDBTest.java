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
import java.util.ArrayList;
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
public class SightingDaoDBTest {

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

    public SightingDaoDBTest() {
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
        Location firstLocation = new Location(0, "Hy-Vee Shakopee", "1451 Adams St S,Shakopee,MN", "Grocery Store",
                new BigDecimal("44.778492"), new BigDecimal("-93.538042"));
        locationDao.addLocation(firstLocation);
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30, 40, 50000));
        newSighting.setLocation(firstLocation);
        newSighting.setHeros(heroDao.getAllHeros());
        sightingDao.addSighting(newSighting);
    }

    @After
    public void tearDown() {
        jdbc.update("Delete From members");
        jdbc.update("Delete From heroSightings");
        jdbc.update("Delete From sighting");
        jdbc.update("Delete From hero");
        jdbc.update("Delete From organization");
        jdbc.update("Delete From location");
    }

    /**
     * Test of addSighting method, of class SightingDaoDB.
     */
    @Test
    public void testAddAndGetSighting() {
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2020, Month.MARCH, 12, 9, 30, 40, 50000));
        Location getLocation = locationDao.allLocations().get(0);
        List<Hero> heros = heroDao.getAllHeros();
        newSighting.setLocation(getLocation);
        newSighting.setHeros(heros);
        Sighting sigh = sightingDao.addSighting(newSighting);
        assertEquals(sigh, newSighting);
    }

    /**
     * Test of getSightingById method, of class SightingDaoDB.
     */
    @Test
    public void testGetSightingById() {
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2020, Month.MARCH, 12, 9, 30, 40, 0));
        Location getLocation = locationDao.allLocations().get(0);
        List<Hero> heros = heroDao.getAllHeros();
        newSighting.setLocation(getLocation);
        newSighting.setHeros(heros);
        Sighting mySigh = sightingDao.addSighting(newSighting);
        Sighting mySightingID = sightingDao.getSightingById(mySigh.getSightingId());
        assertEquals(mySightingID, mySigh);
    }

    /**
     * Test of updateASighting method, of class SightingDaoDB.
     */
    @Test
    public void testUpdateASighting() {
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2020, Month.MARCH, 12, 9, 30, 40, 0));
        Location getLocation = locationDao.allLocations().get(0);
        List<Hero> heros = heroDao.getAllHeros();
        newSighting.setLocation(getLocation);
        newSighting.setHeros(heros);
        Sighting mySigh = sightingDao.addSighting(newSighting);
        Sighting newSighting1 = new Sighting(mySigh.getSightingId(), LocalDateTime.of(2020, Month.JUNE, 12, 9, 30, 40, 0));
        Location getLocation1 = locationDao.allLocations().get(0);
        List<Hero> heros1 = heroDao.getAllHeros();
        newSighting1.setLocation(getLocation1);
        newSighting1.setHeros(heros1);
        Sighting updatedSighting = sightingDao.updateASighting(mySigh.getSightingId(), newSighting1);
        Sighting updatedSightingById = sightingDao.getSightingById(updatedSighting.getSightingId());
        assertEquals(updatedSighting, updatedSightingById);
    }

    /**
     * Test of deleteASighting method, of class SightingDaoDB.
     */
    @Test
    public void testDeleteASighting() {
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2020, Month.FEBRUARY, 19, 9, 30, 40, 0));
        Location getLocation = locationDao.allLocations().get(0);
        List<Hero> heros = heroDao.getAllHeros();
        newSighting.setLocation(getLocation);
        newSighting.setHeros(heros);
        Sighting mySigh = sightingDao.addSighting(newSighting);
        sightingDao.deleteASighting(mySigh.getSightingId());
        for (Sighting sighting : sightingDao.allSightings()) {
            assertNotEquals(sighting.getSightingId(), mySigh.getSightingId());
        }
    }

    /**
     * Test of allSightings method, of class SightingDaoDB.
     */
    @Test
    public void testAllSightings() {
        Sighting newSighting = new Sighting(0, LocalDateTime.of(2020, Month.FEBRUARY, 19, 9, 30, 40, 0));
        Location getLocation = locationDao.allLocations().get(0);
        List<Hero> heros = heroDao.getAllHeros();
        newSighting.setLocation(getLocation);
        newSighting.setHeros(heros);
        Sighting mySigh = sightingDao.addSighting(newSighting);
        List<Sighting> allSightings = sightingDao.allSightings();
        assertEquals(2, allSightings.size());
    }

    /**
     * Test of getAllSightingsForSuperHero method, of class SightingDaoDB.
     */
    @Test
    public void testGetAllSightingsForSuperHero() {
        Sighting newSighting1 = new Sighting(0, LocalDateTime.of(2020, Month.FEBRUARY, 19, 9, 30, 40, 0));
        Sighting newSighting2 = new Sighting(0, LocalDateTime.of(2020, Month.JANUARY, 22, 5, 30, 40, 0));
        Location getLocation = locationDao.allLocations().get(0);
        locationDao.addLocation(getLocation);

        Hero newHero1 = new Hero(0, "Garfield", "Eating everything", "Save_Lasagne");
        Organization myOrganization1 = new Organization(0, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road");
        organizationDao.addOrganization(myOrganization1);
        Organization myOrganization2 = new Organization(0, "EVIL_FLY", "Populate the world with FLIES", "1200 Super BUZZ Road");
        organizationDao.addOrganization(myOrganization2);
        newHero1.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero1);

        Hero newHero2 = new Hero(0, "Hamster", "Eating everything", "Save_Hey");
        organizationDao.addOrganization(myOrganization1);
        organizationDao.addOrganization(myOrganization2);
        newHero2.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero2);
        List<Hero> allHeros = new ArrayList<>();
        allHeros.add(newHero2);
        allHeros.add(newHero1);

        newSighting1.setHeros(allHeros);
        newSighting1.setLocation(getLocation);

        newSighting2.setHeros(allHeros);
        newSighting2.setLocation(getLocation);

        sightingDao.addSighting(newSighting2);
        sightingDao.addSighting(newSighting1);

        List<Sighting> sightingForHero = sightingDao.getAllSightingsForSuperHero(newHero1);
        assertEquals(2, sightingForHero.size());

        assertTrue(sightingForHero.contains(newSighting2));
        assertTrue(sightingForHero.contains(newSighting1));

    }

    /**
     * Test of getAllSightingsForLocation method, of class SightingDaoDB.
     */
    @Test
    public void testGetAllSightingsForLocation() {
        Sighting newSighting1 = new Sighting(0, LocalDateTime.of(2020, Month.FEBRUARY, 19, 9, 30, 40, 0));
        Sighting newSighting2 = new Sighting(0, LocalDateTime.of(2020, Month.JANUARY, 22, 5, 30, 40, 0));

        Location getLocation = locationDao.allLocations().get(0);
        locationDao.addLocation(getLocation);

        Hero newHero1 = new Hero(0, "Garfield", "Eating everything", "Save_Lasagne");
        Organization myOrganization1 = new Organization(0, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road");
        organizationDao.addOrganization(myOrganization1);
        Organization myOrganization2 = new Organization(0, "EVIL_FLY", "Populate the world with FLIES", "1200 Super BUZZ Road");
        organizationDao.addOrganization(myOrganization2);
        newHero1.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero1);

        Hero newHero2 = new Hero(0, "Hamster", "Eating everything", "Save_Hey");
        organizationDao.addOrganization(myOrganization1);
        organizationDao.addOrganization(myOrganization2);
        newHero2.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero2);

        List<Hero> allHeros = new ArrayList<>();
        allHeros.add(newHero2);
        allHeros.add(newHero1);

        newSighting1.setHeros(allHeros);
        newSighting1.setLocation(getLocation);

        newSighting2.setHeros(allHeros);
        newSighting2.setLocation(getLocation);

        sightingDao.addSighting(newSighting2);
        sightingDao.addSighting(newSighting1);
        
        List<Sighting> allSightings=sightingDao.getAllSightingsForLocation(getLocation);
        
        assertTrue(allSightings.contains(newSighting2));
        assertTrue(allSightings.contains(newSighting1));

    }
    
    @Test
    public void testGet10LatestSightings() {
        Location getLocation = locationDao.allLocations().get(0);
        locationDao.addLocation(getLocation);

        Hero newHero1 = new Hero(0, "Garfield", "Eating everything", "Save_Lasagne");
        Organization myOrganization1 = new Organization(0, "EVIL_CAT", "Populate the world with evil housecats", "1200 Super Secret Road");
        organizationDao.addOrganization(myOrganization1);
        Organization myOrganization2 = new Organization(0, "EVIL_FLY", "Populate the world with FLIES", "1200 Super BUZZ Road");
        organizationDao.addOrganization(myOrganization2);
        newHero1.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero1);

        Hero newHero2 = new Hero(0, "Hamster", "Eating everything", "Save_Hey");
        organizationDao.addOrganization(myOrganization1);
        organizationDao.addOrganization(myOrganization2);
        newHero2.setOrganizations(organizationDao.getAllOrganization());
        heroDao.addHero(newHero2);

        List<Hero> allHeros = new ArrayList<>();
        allHeros.add(newHero2);
        allHeros.add(newHero1);
        
        for(int i = 0; i < 11; i++) {
            Sighting newSighting = new Sighting(0, LocalDateTime.of(2019, Month.values()[i], 19, 9, 30, 40, 0));
            newSighting.setHeros(allHeros);
            newSighting.setLocation(getLocation);
            sightingDao.addSighting(newSighting);
        }

        
        
        List<Sighting> allSightings=sightingDao.get10LatestSightings();
        
        //verify there are 10 results
        assertEquals(allSightings.size(),10);
        
        //verify that the dates are in descending order
        for(int i=1;i<10;i++){
            if(allSightings.get(i).getDate().compareTo(allSightings.get(i-1).getDate())>=0){
                fail();
            }
        }

    }

}
