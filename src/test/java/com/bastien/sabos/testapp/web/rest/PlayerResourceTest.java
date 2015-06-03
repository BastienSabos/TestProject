package com.bastien.sabos.testapp.web.rest;

import com.bastien.sabos.testapp.Application;
import com.bastien.sabos.testapp.domain.Player;
import com.bastien.sabos.testapp.repository.PlayerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlayerResource REST controller.
 *
 * @see PlayerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlayerResourceTest {


    private static final Long DEFAULT_IDENTIFIER = 1L;
    private static final Long UPDATED_IDENTIFIER = 2L;

    
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private PlayerRepository playerRepository;

    private MockMvc restPlayerMockMvc;

    private Player player;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlayerResource playerResource = new PlayerResource();
        ReflectionTestUtils.setField(playerResource, "playerRepository", playerRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource).build();
    }

    @Before
    public void initTest() {
        player = new Player();
        player.setIdentifier(DEFAULT_IDENTIFIER);
        player.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        // Validate the database is empty
        assertThat(playerRepository.findAll()).hasSize(0);

        // Create the Player
        restPlayerMockMvc.perform(post("/api/players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(player)))
                .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(1);
        Player testPlayer = players.iterator().next();
        assertThat(testPlayer.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testPlayer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the players
        restPlayerMockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(player.getId().intValue()))
                .andExpect(jsonPath("$.[0].identifier").value(DEFAULT_IDENTIFIER.toString()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Update the player
        player.setIdentifier(UPDATED_IDENTIFIER);
        player.setName(UPDATED_NAME);
        restPlayerMockMvc.perform(put("/api/players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(player)))
                .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(1);
        Player testPlayer = players.iterator().next();
        assertThat(testPlayer.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testPlayer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(0);
    }
}
