package de.pp.ipwa02.ghostnet.data;

import de.pp.ipwa02.ghostnet.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repo fuer Personen (REPORTER / SALVOR). Patrick Praml, IPWA02-01 */
public interface PersonRepository extends JpaRepository<Person, Long> {}
