/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dto;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author alinc
 */
public class Location {

    int locationId;

    @NotBlank(message = "Name must not be empty")
    @Size(max = 45, message = "Description is too long.")
    String name;

    @Size(max = 45, message = "Description is too long.")
    String description;

    @NotBlank(message = "Address must not be empty")
    @Size(max = 45, message = "Address is too long.")
    String address;

    @DecimalMin(value = "-90.000000", inclusive = true, message = "Must not be smaller than -90")
    @Digits(integer = 8, fraction = 6)
    @DecimalMax(value = "90.000000", inclusive = true, message = "Must not be larger than 90")

    BigDecimal latitude;

    @DecimalMin(value = "-180.000000", inclusive = true, message = "Must not be smaller than -180")
    @Digits(integer = 9, fraction = 6)
    @DecimalMax(value = "180.000000", inclusive = true, message = "Must not be larger than 180")

    BigDecimal longitude;

    public Location() {

    }

    public Location(int locationId, String name, String description, String address, BigDecimal latitude, BigDecimal longitude) {
        this.locationId = locationId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.locationId;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.description);
        hash = 41 * hash + Objects.hashCode(this.address);
        hash = 41 * hash + Objects.hashCode(this.latitude);
        hash = 41 * hash + Objects.hashCode(this.longitude);
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
        final Location other = (Location) obj;
        if (this.locationId != other.locationId) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.latitude, other.latitude)) {
            return false;
        }
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        return true;
    }

}
