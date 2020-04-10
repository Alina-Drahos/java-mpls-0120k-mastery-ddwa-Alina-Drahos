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

/**
 *
 * @author alinc
 */
@Repository
public class OrganizationDaoDB implements OrganizationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO organization(name,description,address)"
                + "VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_ORGANIZATION,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, organization.getName());
            statement.setString(2, organization.getDescription());
            statement.setString(3, organization.getAddress());
            return statement;
        }, keyHolder);
        organization.setOrganizationId(keyHolder.getKey().intValue());
        return organization;
    }

    @Override
    public Organization getOrganization(int organizationId) {
        try {
            final String SELECT_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE organizationId = ?";
            return jdbc.queryForObject(SELECT_ORGANIZATION_BY_ID, new OrganizationMapper(), organizationId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Organization updateOrganization(int organizationId, Organization organizationToUpdate) {
        final String UPDATE_ORGANIZATION = "UPDATE organization "
                + "SET name=?,description=?, address=? WHERE organizationId=?";
        jdbc.update(UPDATE_ORGANIZATION,
                organizationToUpdate.getName(),
                organizationToUpdate.getDescription(),
                organizationToUpdate.getAddress(),
                organizationId);

        return organizationToUpdate;
    }

    @Override
    public void deleteOrganization(int OrganizationId) {
        final String DELETE_Members = "DELETE FROM members "
                + "WHERE organizationId = ?";
        jdbc.update(DELETE_Members, OrganizationId);

        final String DELETE_HERO = "DELETE FROM Organization WHERE OrganizationId = ?";
        jdbc.update(DELETE_HERO, OrganizationId);

    }

    @Override
    public List<Organization> getAllOrganization() {
        final String SELECT_ALL_ORGANIZATIONS = "SELECT * FROM organization";
        return jdbc.query(SELECT_ALL_ORGANIZATIONS, new OrganizationMapper());
    }

    @Override
    public List<Organization> getallOrganizationsForHero(Hero hero) {
        final String SELECT_ORGANIZATIONS_FOR_HERO = "SELECT * FROM organization o "
                + "JOIN members m on o.organizationId= m.organizationId where heroId=?";
        List<Organization> organizations = jdbc.query(SELECT_ORGANIZATIONS_FOR_HERO,
                new OrganizationMapper(), hero.getHeroId());
        
        return organizations;

    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization org = new Organization(rs.getInt("organizationId"), rs.getString("name"), rs.getString("Description"),
                    rs.getString("address"));
            return org;
        }
    }

}
