package de.pp.ipwa02.ghostnet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller fuer Geisternetze
 * Patrick Praml, IPWA02-01
 */

@Controller
@RequestMapping("/nets")
public class NetController {

    private final GhostNetRepo ghostNetRepo;
    private final PersonRepo personRepo;
    private final GhostNetService ghostNetService;

    public NetController(GhostNetRepo ghostNetRepo, PersonRepo personRepo, GhostNetService ghostNetService) {
        this.ghostNetRepo = ghostNetRepo;
        this.personRepo = personRepo;
        this.ghostNetService = ghostNetService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(name = "filter", required = false) String filter,
                       @RequestParam(name = "msg", required = false) String msg) {

        List<GhostNet> nets;
        if ("open".equalsIgnoreCase(filter)) {
            nets = ghostNetRepo.findOpen();
        } else {
            nets = ghostNetRepo.findAllByOrderByReportedAtDesc();
            filter = "all";
        }

        List<Person> people = personRepo.findAllByRole(Role.SALVOR);
        boolean hasSalvors = !people.isEmpty();

        long cntReported  = ghostNetRepo.countByStatus(NetStatus.REPORTED);
        long cntAssigned  = ghostNetRepo.countByStatus(NetStatus.ASSIGNED);
        long cntRecovered = ghostNetRepo.countByStatus(NetStatus.RECOVERED);

        model.addAttribute("nets", nets);
        model.addAttribute("people", people);
        model.addAttribute("hasSalvors", hasSalvors);
        model.addAttribute("filter", filter);
        model.addAttribute("cntReported", cntReported);
        model.addAttribute("cntAssigned", cntAssigned);
        model.addAttribute("cntRecovered", cntRecovered);

        if (msg != null) model.addAttribute("msg", msg);
        return "nets";
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") Long id,
                         @RequestParam(value = "salvorId", required = false) Long salvorId,
                         @RequestParam(value = "team", required = false) Integer team,
                         RedirectAttributes ra) {
        try {
            Long effectiveSalvorId = resolveSalvorId(salvorId, team);
            if (effectiveSalvorId == null) {
                ra.addFlashAttribute("msg", "Zuweisung nicht möglich: Keine bergende Person vorhanden/gewählt.");
                return "redirect:/nets";
            }
            ghostNetService.assign(id, effectiveSalvorId);
            Person p = personRepo.findById(effectiveSalvorId).orElse(null);
            ra.addFlashAttribute("msg", "Netz " + id + " wurde der Bergung zugewiesen" +
                    (p != null ? " (" + p.getName() + ")" : "") + ".");
        } catch (Exception e) {
            ra.addFlashAttribute("msg", e.getMessage());
        }
        return "redirect:/nets";
    }

    @PostMapping("/{id}/recover")
    public String recover(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ghostNetService.recover(id);
            ra.addFlashAttribute("msg", "Netz " + id + " wurde als geborgen markiert.");
        } catch (Exception e) {
            ra.addFlashAttribute("msg", e.getMessage());
        }
        return "redirect:/nets";
    }

    @PostMapping("/{id}/lost")
    public String lost(@PathVariable("id") Long id,
                       @RequestParam("name") String name,
                       @RequestParam("phone") String phone,
                       RedirectAttributes ra) {
        try {
            // Nur erlaubt, wenn bereits ZUGEWIESEN
            GhostNet net = ghostNetRepo.findById(id).orElseThrow();
            if (net.getStatus() != NetStatus.ASSIGNED) {
                ra.addFlashAttribute("msg", "Verschollen melden ist nur möglich, wenn das Netz bereits zugewiesen ist.");
                return "redirect:/nets";
            }
            ghostNetService.markLost(id, name, phone);
            ra.addFlashAttribute("msg", "Netz " + id + " wurde als verschollen gemeldet.");
        } catch (Exception e) {
            ra.addFlashAttribute("msg", e.getMessage());
        }
        return "redirect:/nets";
    }

    // helpers:

    private Long resolveSalvorId(Long salvorId, Integer team) {
        if (salvorId != null) return salvorId;

        if (team != null && (team == 1 || team == 2)) {
            String teamName = "Bergungsteam " + team;
            Optional<Person> existing = personRepo.findAllByRole(Role.SALVOR)
                    .stream().filter(p -> teamName.equals(p.getName())).findFirst();
            if (existing.isPresent()) return existing.get().getId();

            Person created = new Person();
            created.setName(teamName);
            created.setPhone(null);
            created.setRole(Role.SALVOR);
            personRepo.save(created);
            return created.getId();
        }
        return personRepo.findFirstByRole(Role.SALVOR).map(Person::getId).orElse(null);
    }
}
