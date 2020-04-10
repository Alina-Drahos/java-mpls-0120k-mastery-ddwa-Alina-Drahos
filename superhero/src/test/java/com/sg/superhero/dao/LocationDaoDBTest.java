/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Location;
import java.math.BigDecimal;
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
public class LocationDaoDBTest {

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

    public LocationDaoDBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Location firstLocation = new Location(0, "Hy-Vee Shakopee", "1451 Adams St S,Shakopee,MN", "Grocery Store",
                new BigDecimal("44.778492"), new BigDecimal("-93.538042"));
        locationDao.addLocation(firstLocation);
    }

    @After
    public void tearDown() {
        jdbc.update("DELETE FROM location");
    }

    /**
     * Test of addLocation method, of class LocationDaoDB.
     */
    @Test
    public void testAndGetAddLocation() {
        Location secondLocation = new Location(0, "Cub Shakopee", " 1198 Vierling Dr E, Shakopee, MN", "Grocery Store",
                new BigDecimal("44.780937"), new BigDecimal("-93.502769"));
        Location createdLocation = locationDao.addLocation(secondLocation);
        assertEquals(createdLocation, secondLocation);
        int latestId = locationDao.allLocations().get(1).getLocationId();
        Location aLocation = locationDao.getLocation(latestId);
        assertEquals(latestId, aLocation.getLocationId());
        assertTrue("Cub Shakopee".equals(aLocation.getName()));
        assertEquals(secondLocation.getLatitude(), aLocation.getLatitude());

    }

    /**
     * Test of getLocation method, of class LocationDaoDB.
     */
    @Test
    public void testGetLocation() {
        Location newLocation = new Location(0, "Crave Eden Prairie", "8251 Flying Cloud Dr, Eden Prairie, MN", "Restaurant",
                new BigDecimal("44.854353"), new BigDecimal("-93.424831"));
        locationDao.addLocation(newLocation);

        int latestID = locationDao.allLocations().get(1).getLocationId();
        Location loc = locationDao.getLocation(latestID);
        assertEquals(loc.getLocationId(), latestID);
        assertEquals(newLocation,loc);

    }

    /**
     * Test of updateLocation method, of class LocationDaoDB.
     */
    @Test
    public void testUpdateLocation() {
        Location newLocation = new Location(0, "Crave Eden Prairie", "8251 Flying Cloud Dr, Eden Prairie, MN", "Restaurant",
                new BigDecimal("44.854353"), new BigDecimal("-93.424831"));
        Location myLocation = locationDao.addLocation(newLocation);
        int id = myLocation.getLocationId();
        Location updatedLocation = locationDao.updateLocation(id, new Location(id, "Henry Prairie", "Restaurant", "8956 Izzy Road Dr, Eden Prairie, MN",
                new BigDecimal("94.854358"), new BigDecimal("-93.424831")));
        Location myloc=locationDao.getLocation(id);
        assertEquals(updatedLocation.getAddress(), "8956 Izzy Road Dr, Eden Prairie, MN");
        assertEquals(updatedLocation, myloc);
    }

    /**
     * Test of deleteLocation method, of class LocationDaoDB.
     */
    @Test
    public void testDeleteLocationById() {
        Location newLocation = new Location(0, "Crave Eden Prairie", "8251 Flying Cloud Dr, Eden Prairie, MN", "Restaurant",
                new BigDecimal("44.854353"), new BigDecimal("-93.424831"));
        Location location = locationDao.addLocation(newLocation);
        locationDao.deleteLocation(location.getLocationId());
        for (Location loc:locationDao.allLocations()) {
           assertNotEquals(location.getLocationId(),loc.getLocationId());
        }
    }

    /**
     * Test of allLocations method, of class LocationDaoDB.
     */
    @Test
    public void testGetAllLocations() {
        Location newLocation = new Location(0, "Crave Eden Prairie", "8251 Flying Cloud Dr, Eden Prairie, MN", "Restaurant",
                new BigDecimal("44.854353"), new BigDecimal("-93.424831"));
        Location location = locationDao.addLocation(newLocation);
        List<Location> allLocations= locationDao.allLocations();
        assertEquals(2,allLocations.size());
    }

}
