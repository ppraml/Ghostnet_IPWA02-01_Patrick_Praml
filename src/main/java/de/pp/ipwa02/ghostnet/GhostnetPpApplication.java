package de.pp.ipwa02.ghostnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

/**
 * app start
 * Patrick Praml, IPWA02-01
 */

@SpringBootApplication
public class GhostnetPpApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhostnetPpApplication.class, args);
    }

    /**
     * mini-seed fuer demo: nur im Profil "dev"
     * legt 1 meldende Person, 1 Bergung und 1 REPORTED-Net an (nur wenn leer)
     */
    
    @Bean
    @Profile("dev")
    ApplicationRunner seed(PersonRepo personRepo, GhostNetRepo ghostNetRepo) {
        return args -> {
            if (personRepo.count() == 0) {
                Person reporter = new Person();
                reporter.setName("Meldende Demo");
                reporter.setPhone(null);
                reporter.setRole(Role.REPORTER);
                personRepo.save(reporter);

                Person salvor = new Person();
                salvor.setName("Bergung Demo");
                salvor.setPhone("+49 999 1111");
                salvor.setRole(Role.SALVOR);
                personRepo.save(salvor);
            }

            if (ghostNetRepo.count() == 0) {
                GhostNet g = new GhostNet();
                g.setLat(54.123456);
                g.setLon(8.654321);
                g.setSizeEstimate("mittel");
                g.setStatus(NetStatus.REPORTED);
                g.setReportedAt(LocalDateTime.now());
                personRepo.findFirstByRole(Role.REPORTER).ifPresent(g::setReportedBy);
                ghostNetRepo.save(g);
            }
        };
    }
}
