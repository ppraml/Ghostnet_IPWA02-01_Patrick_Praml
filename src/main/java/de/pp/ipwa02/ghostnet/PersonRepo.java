package de.pp.ipwa02.ghostnet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repo fuer Personen (Reporter/Salvor).
 * Patrick Praml, IPWA02-01
 */

public interface PersonRepo extends JpaRepository<Person, Long> {

    // alle personen nach rolle
    List<Person> findAllByRole(Role role);

    // optional: erste person mit rolle
    Optional<Person> findFirstByRole(Role role);
}
