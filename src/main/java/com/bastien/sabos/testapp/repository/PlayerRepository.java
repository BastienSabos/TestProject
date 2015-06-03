package com.bastien.sabos.testapp.repository;

import com.bastien.sabos.testapp.domain.Player;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
public interface PlayerRepository extends JpaRepository<Player,Long> {

}
