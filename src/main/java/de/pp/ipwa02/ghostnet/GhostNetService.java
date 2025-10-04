package de.pp.ipwa02.ghostnet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * Service fuer Geisternetze
 * enthaelt Logik fuer Statuswechsel
 * Patrick Praml, IPWA02-01
 */

@Service
public class GhostNetService {

    private final GhostNetRepo ghostNetRepo;
    private final PersonRepo personRepo;

    // konstruktor injection
    public GhostNetService(GhostNetRepo ghostNetRepo, PersonRepo personRepo) {
        this.ghostNetRepo = ghostNetRepo;
        this.personRepo = personRepo;
    }

    // netz assignen (claimen) -> REPORTED -> ASSIGNED
    @Transactional
    public void assign(Long id, Long salvorId) {
        GhostNet net = ghostNetRepo.findById(id).orElseThrow();
        if (net.getStatus() != NetStatus.REPORTED) {
            throw new IllegalStateException("Netz ist nicht im Status 'gemeldet' (REPORTED).");
        }
        Person salvor = personRepo.findById(salvorId).orElseThrow();
        if (salvor.getRole() != Role.SALVOR) {
            throw new IllegalArgumentException("Ausgewaehlte Person gehoert nicht zur Bergung.");
        }
        net.setStatus(NetStatus.ASSIGNED);
        net.setSalvor(salvor);
        net.setClaimedAt(LocalDateTime.now());
        ghostNetRepo.save(net);
    }

    // netz als recovered markieren -> ASSIGNED -> RECOVERED
    @Transactional
    public void recover(Long id) {
        GhostNet net = ghostNetRepo.findById(id).orElseThrow();
        if (net.getStatus() != NetStatus.ASSIGNED) {
            throw new IllegalStateException("Netz ist nicht 'zugewiesen' (ASSIGNED).");
        }
        net.setStatus(NetStatus.RECOVERED);
        net.setRecoveredAt(LocalDateTime.now());
        ghostNetRepo.save(net);
    }

    // netz als verschollen markieren -> REPORTED/ASSIGNED -> LOST
    @Transactional
    public void markLost(Long id, String name, String phone) {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Name und Telefon sind fuer 'verschollen' erforderlich.");
        }
        GhostNet net = ghostNetRepo.findById(id).orElseThrow();
        if (net.getStatus() == NetStatus.RECOVERED || net.getStatus() == NetStatus.LOST) {
            throw new IllegalStateException("Netz kann nicht als 'verschollen' markiert werden (Status: " + net.getStatus() + ").");
        }
        net.setStatus(NetStatus.LOST);
        net.setLostAt(LocalDateTime.now());
        net.setLostByName(name.trim());
        net.setLostByPhone(phone.trim());
        ghostNetRepo.save(net);
    }
}
