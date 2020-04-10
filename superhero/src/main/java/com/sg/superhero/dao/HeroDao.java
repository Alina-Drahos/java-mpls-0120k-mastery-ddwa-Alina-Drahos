/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superhero.dao;

import com.sg.superhero.dto.Hero;
import java.util.List;

/**
 *
 * @author alinc
 */
public interface HeroDao {

    Hero addHero(Hero newHero);

    Hero getHeroById(int id);

    Hero updateHeroInformation(int heroID, Hero updatedHero);

    void deleteHeroById(int id);

    List<Hero> getAllHeros();

}
