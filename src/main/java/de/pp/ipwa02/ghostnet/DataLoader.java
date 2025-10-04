package de.pp.ipwa02.ghostnet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initialdaten fuer Demo (nur Profil "dev"):
 * legt automatisch eine meldende Person und eine bergende Person an,
 * falls keine Personen mit jeweiliger Rolle vorhanden sind.
 * Patrick Praml, IPWA02-01
 */

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final PersonRepo personRepo;

    public DataLoader(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public void run(String... args) {
        // meldende Person vorhanden?
        List<Person> reporters = personRepo.findAllByRole(Role.REPORTER);
        if (reporters.isEmpty()) {
            Person r = new Person();
            r.setName("Meldende Demo");
            r.setPhone(null); // anonym moeglich
            r.setRole(Role.REPORTER);
            personRepo.save(r);
        }

        // bergende Person vorhanden?
        List<Person> salvors = personRepo.findAllByRole(Role.SALVOR);
        if (salvors.isEmpty()) {
            Person s = new Person();
            s.setName("Bergung Demo");
            s.setPhone("+49 999 1111");
            s.setRole(Role.SALVOR);
            personRepo.save(s);
        }
    }
}
