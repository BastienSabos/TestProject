package com.bastien.sabos.testapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bastien.sabos.testapp.domain.Player;
import com.bastien.sabos.testapp.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    @Inject
    private PlayerRepository playerRepository;

    /**
     * POST  /players -> Create a new player.
     */
    @RequestMapping(value = "/players",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to save Player : {}", player);
        if (player.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new player cannot already have an ID").build();
        }
        playerRepository.save(player);
        return ResponseEntity.created(new URI("/api/players/" + player.getId())).build();
    }

    /**
     * PUT  /players -> Updates an existing player.
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to update Player : {}", player);
        if (player.getId() == null) {
            return create(player);
        }
        playerRepository.save(player);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /players -> get all the players.
     */
    @RequestMapping(value = "/players",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Player> getAll() {
        log.debug("REST request to get all Players");
        return playerRepository.findAll();
    }

    /**
     * GET  /players/:id -> get the "id" player.
     */
    @RequestMapping(value = "/players/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> get(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        return Optional.ofNullable(playerRepository.findOne(id))
            .map(player -> new ResponseEntity<>(
                player,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /players/:id -> delete the "id" player.
     */
    @RequestMapping(value = "/players/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerRepository.delete(id);
    }
}
