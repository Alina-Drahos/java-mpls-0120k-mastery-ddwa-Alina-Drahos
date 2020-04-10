/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import com.sg.superhero.dto.Organization;
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
public class HeroDaoDB implements HeroDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Hero addHero(Hero newHero) {
        final String INSERT_HERO = "INSERT INTO hero(name,description,superPower)"
                + "VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_HERO,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newHero.getName());
            statement.setString(2, newHero.getDescription());
            statement.setString(3, newHero.getSuperPower());
            return statement;
        }, keyHolder);
        newHero.setHeroId(keyHolder.getKey().intValue());
        insertOrganizationHero(newHero);
        return newHero;
    }

    private void insertOrganizationHero(Hero newHero) {
        
            final String INSERT_MEMBERS = "INSERT INTO members"
                    + "(heroId,organizationId) VALUES(?,?)";
            for (Organization organization : newHero.getOrganizations()) {
                jdbc.update(INSERT_MEMBERS, newHero.getHeroId(), organization.getOrganizationId());
            }
    }

    @Override
    public Hero getHeroById(int id) {
        try {
            final String SELECT_HERO_BY_ID = "SELECT * FROM hero WHERE heroid = ?";
            return jdbc.queryForObject(SELECT_HERO_BY_ID, new HeroMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Hero updateHeroInformation(int heroID, Hero updatedHero) {
        final String UPDATE_HERO = "UPDATE hero SET name = ?, description = ?, superPower=? "
                + "WHERE heroid = ?";
        jdbc.update(UPDATE_HERO,
                updatedHero.getName(),
                updatedHero.getDescription(),
                updatedHero.getSuperPower(),
                heroID);
        insertOrganizationHero(updatedHero);
        return updatedHero;
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {
        final String DELETE_SIGHTING_HERO = "DELETE FROM herosightings "
                + "WHERE heroId = ?";
        jdbc.update(DELETE_SIGHTING_HERO, id);

        final String DELETE_MEMBERS_HERO = "DELETE FROM members "
                + "WHERE heroId = ?";
        jdbc.update(DELETE_MEMBERS_HERO, id);

        final String DELETE_HERO = "DELETE FROM hero WHERE heroId = ?";
        jdbc.update(DELETE_HERO, id);
    }

    @Override
    public List<Hero> getAllHeros() {
        final String SELECT_ALL_HEROS = "SELECT * FROM hero";
        return jdbc.query(SELECT_ALL_HEROS, new HeroMapper());
    }

    public static final class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int index) throws SQLException {
            Hero hero = new Hero(rs.getInt("heroId"), rs.getString("name"),
                    rs.getString("description"), rs.getString("superPower"));
            return hero;
        }
    }

}
