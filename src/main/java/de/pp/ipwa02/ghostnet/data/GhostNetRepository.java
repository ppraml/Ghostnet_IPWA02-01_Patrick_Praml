package de.pp.ipwa02.ghostnet.data;

import de.pp.ipwa02.ghostnet.GhostNet;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repo fuer Geisternetze. Patrick Praml, IPWA02-01 */
public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {}

