/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Location;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author alinc
 */
@Repository
public class LocationDaoDB implements LocationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Location addLocation(Location newLocation) {
        final String INSERT_LOCATION = "INSERT INTO location(name,description,address,latitude,longetitude)"
                + "VALUES(?,?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_LOCATION,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newLocation.getName());
            statement.setString(2, newLocation.getDescription());
            statement.setString(3, newLocation.getAddress());
            statement.setBigDecimal(4, newLocation.getLatitude());
            statement.setBigDecimal(5, newLocation.getLongitude());

            return statement;
        }, keyHolder);
        newLocation.setLocationId(keyHolder.getKey().intValue());
        return newLocation;
    }

    @Override
    public Location getLocation(int locationId) {
        try {
            final String SELECT_LOCATION_BY_ID = "SELECT * FROM location WHERE locationId = ?";
            return jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), locationId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Location updateLocation(int locationId, Location oldLocation) {
        final String UPDATE_LOCATION = "UPDATE location "
                + "SET name=?, description=?, address=?,latitude=?,longetitude=? WHERE locationId=?;";
        jdbc.update(UPDATE_LOCATION,
                oldLocation.getName(),
                oldLocation.getDescription(),
                oldLocation.getAddress(),
                oldLocation.getLatitude(),
                oldLocation.getLongitude(),
                locationId);

        return oldLocation;
    }

    @Override
    @Transactional
    public void deleteLocation(int locationId) {
        final String DELETE_SIGHTING = "DELETE FROM sighting "
                + "WHERE locationId = ?";
        jdbc.update(DELETE_SIGHTING, locationId);

        final String DELETE_LOCATION = "DELETE FROM location WHERE locationId = ?";
        jdbc.update(DELETE_LOCATION, locationId);
    }

    @Override
    public List<Location> allLocations() {
        final String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
        return jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location loc = new Location(rs.getInt("locationId"), rs.getString("name"), rs.getString("description"),
                    rs.getString("address"), rs.getBigDecimal("latitude"), rs.getBigDecimal("longetitude"));
            return loc;
        }
    }

}
