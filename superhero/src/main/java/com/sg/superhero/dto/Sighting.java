/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author alinc
 */
public class Sighting {
    int sightingId;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss:SSS")
    LocalDateTime date;   
    Location location;
    
    List<Hero> heros=new ArrayList<>();
    
    public Sighting(){
        
    }

    public Sighting(int sightingId, LocalDateTime date) {
        this.sightingId = sightingId;
        this.date = date;
    }

    public int getSightingId() {
        return sightingId;
    }

    public void setSightingId(int sightingId) {
        this.sightingId = sightingId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Hero> getHeros() {
        return heros;
    }

    public void setHeros(List<Hero> heros) {
        this.heros = heros;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public String getSightingLocation(){
        return "https://www.bing.com/maps/embed?h=400&w=500&cp="+
            location.getLatitude()+"~"+location.getLongitude()+"&lvl=11&typ=d&sty=r&src=SHELL&FORM=MBEDV8";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.sightingId;
        hash = 43 * hash + Objects.hashCode(this.date);
        hash = 43 * hash + Objects.hashCode(this.location);
        hash = 43 * hash + Objects.hashCode(this.heros);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sighting other = (Sighting) obj;
        if (this.sightingId != other.sightingId) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.heros, other.heros)) {
            return false;
        }
        return true;
    }
    


    
    
}
