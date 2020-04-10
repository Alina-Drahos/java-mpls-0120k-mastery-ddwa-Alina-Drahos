/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Location;
import java.util.List;

/**
 *
 * @author alinc
 */
public interface LocationDao {

    Location addLocation(Location newLocation);

    Location getLocation(int locationId);

    Location updateLocation(int locationId, Location oldLocation);

    void deleteLocation(int locationId);

    List<Location> allLocations();
    
    
    
}
