/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dao.HeroDaoDB.HeroMapper;
import com.sg.superhero.dao.LocationDaoDB.LocationMapper;
import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Location;
import com.sg.superhero.dto.Sighting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public class SightingDaoDB implements SightingDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Sighting addSighting(Sighting newSighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(date,locationId) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_SIGHTING,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(newSighting.getDate()));
            statement.setInt(2, newSighting.getLocation().getLocationId());
            return statement;
        }, keyHolder);
        newSighting.setSightingId(keyHolder.getKey().intValue());
        insertSightingHero(newSighting);
        return newSighting;
    }

    private void insertSightingHero(Sighting newSighting) {
        final String INSERT_HERO_SIGHTINGS = "INSERT INTO heroSightings"
                + "(heroId,sightingId)VALUES(?,?);";
        for (Hero hero : newSighting.getHeros()) {
            jdbc.update(INSERT_HERO_SIGHTINGS, hero.getHeroId(), newSighting.getSightingId());
        }
    }

    @Override
    @Transactional
    public Sighting getSightingById(int sightingId) {
        try {
            final String SELECT_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE sightingId=?;";
            Sighting sighting = jdbc.queryForObject(SELECT_SIGHTING_BY_ID, new SightingMapper(), sightingId);
            sighting.setLocation(getLocationForSighting(sighting));
            sighting.setHeros(getHerosForSighting(sighting));
            return sighting;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    private Location getLocationForSighting(Sighting sighting) {
        final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* FROM location l"
                + " JOIN sighting s ON l.locationId= s.locationId WHERE s.sightingId=?;";
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationMapper(),
                sighting.getSightingId());
    }
    

    private List<Hero> getHerosForSighting(Sighting sighting) {
        final String SELECT_HEROS_FOR_SIGHTING = "SELECT h.* FROM hero h "
                + "JOIN heroSightings he ON h.heroId = he.heroId WHERE he.sightingId = ?";
        return jdbc.query(SELECT_HEROS_FOR_SIGHTING, new HeroMapper(),
                sighting.getSightingId());
    }

    private void addLocationAndHeroToMeeting(List<Sighting> sightings) {
        for (Sighting sighting : sightings) {
            sighting.setLocation(getLocationForSighting(sighting));
            sighting.setHeros(getHerosForSighting(sighting));
        }
    }

    @Override
    public Sighting updateASighting(int sightingId, Sighting updatedSighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting "
                + "SET date=?,locationId=? WHERE sightingId=?";
        jdbc.update(UPDATE_SIGHTING,
                Timestamp.valueOf(updatedSighting.getDate().withNano(0)),
                updatedSighting.getLocation().getLocationId(),
                sightingId);
        final String DELETE_HERO_SIGHTING = "DELETE FROM heroSightings "
                + "WHERE sightingId=?;";
        jdbc.update(DELETE_HERO_SIGHTING, sightingId);
        insertSightingHero(updatedSighting);
        return updatedSighting;
    }

    @Override
    public void deleteASighting(int sightingId) {
        final String DELETE_HERO_SIGHTINGS = "DELETE FROM heroSightings "
                + "WHERE sightingId = ?";
        jdbc.update(DELETE_HERO_SIGHTINGS, sightingId);

        final String DELETE_SIGHTINGS = "DELETE FROM sighting WHERE sightingId = ?;";
        jdbc.update(DELETE_SIGHTINGS, sightingId);
    }

    @Override
    public List<Sighting> allSightings() {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sighting>sightings=jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper());
        addLocationAndHeroToMeeting(sightings);
        return sightings;
    }

    @Override
    public List<Sighting> getAllSightingsForSuperHero(Hero hero) {
        final String SELECT_SIGHTINGS_FOR_HERO = "SELECT * FROM sighting s "
                + "JOIN heroSightings hs ON s.sightingId = hs.sightingId WHERE hs.heroId= ?";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTINGS_FOR_HERO,
                new SightingMapper(), hero.getHeroId());
        addLocationAndHeroToMeeting(sightings);
        return sightings;
    }

    @Override
    public List<Sighting> getAllSightingsForLocation(Location location) {
       final String SELECT_SIGHTINGS_FOR_LOCATION = "SELECT * FROM sighting "
                +"WHERE locationId= ?";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTINGS_FOR_LOCATION,
                new SightingMapper(), location.getLocationId());
        addLocationAndHeroToMeeting(sightings);
        return sightings;
    }

    @Override
    public List<Sighting> get10LatestSightings() {
        final String SELECT_10_LATEST_SIGHTINGS="SELECT * FROM sighting "
                + "ORDER BY date DESC LIMIT 10";
        List<Sighting> sightings=jdbc.query(SELECT_10_LATEST_SIGHTINGS,
                new SightingMapper());
        addLocationAndHeroToMeeting(sightings);
        return sightings;
    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sigh = new Sighting(rs.getInt("sightingId"),
                    rs.getTimestamp("Date").toLocalDateTime().withNano(0));
            return sigh;
        }
    }

}
