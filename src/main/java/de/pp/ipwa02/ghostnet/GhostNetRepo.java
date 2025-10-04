package de.pp.ipwa02.ghostnet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Repo fuer Geisternetze.
 * Patrick Praml, IPWA02-01
 */

public interface GhostNetRepo extends JpaRepository<GhostNet, Long> {

    // offene netze = noch nicht recovered und kein salvor gesetzt
    @Query("""
           select g
             from GhostNet g
            where g.status <> de.pp.ipwa02.ghostnet.NetStatus.RECOVERED
              and g.salvor is null
            order by g.reportedAt desc
           """)
    List<GhostNet> findOpen();

    // alle nets, neueste zuerst (fuer uebersicht)
    List<GhostNet> findAllByOrderByReportedAtDesc();

    // nach status gefiltert (z.b. alle REPORTED)
    List<GhostNet> findAllByStatusOrderByReportedAtDesc(NetStatus status);

    // nicht-ein status (z.b. alles ausser RECOVERED)
    List<GhostNet> findAllByStatusNotOrderByReportedAtDesc(NetStatus status);

    // zaehler pro status (fuer badges in der liste)
    long countByStatus(NetStatus status);
}
