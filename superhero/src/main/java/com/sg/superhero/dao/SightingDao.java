/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Location;
import com.sg.superhero.dto.Sighting;
import java.util.List;

/**
 *
 * @author alinc
 */
public interface SightingDao {

    Sighting addSighting(Sighting newSighting);

    Sighting getSightingById(int SightingId);

    Sighting updateASighting(int SightingId, Sighting updatedSighting);

    void deleteASighting(int SightingId);

    List<Sighting> allSightings();

    List<Sighting> getAllSightingsForSuperHero(Hero hero);
    
    List<Sighting> getAllSightingsForLocation(Location location);
    
    List<Sighting> get10LatestSightings();

}
