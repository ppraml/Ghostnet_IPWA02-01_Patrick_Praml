package de.pp.ipwa02.ghostnet;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Controller fuer Meldungen (Report)
 * zeigt Formular und legt neues Netz an
 * Patrick Praml, IPWA02-01
 */

@Controller
public class ReportController {

    private final GhostNetRepo ghostNetRepo;
    private final PersonRepo personRepo;

    public ReportController(GhostNetRepo ghostNetRepo, PersonRepo personRepo) {
        this.ghostNetRepo = ghostNetRepo;
        this.personRepo = personRepo;
    }

    @GetMapping("/report")
    public String showForm(Model model) {
        if (!model.containsAttribute("reportForm")) {
            model.addAttribute("reportForm", new ReportForm());
        }
        return "report";
    }

    @PostMapping("/report")
    public String submit(@ModelAttribute("reportForm") @Valid ReportForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.reportForm", br);
            ra.addFlashAttribute("reportForm", form);
            return "redirect:/report";
        }

        // Neues Netz anlegen
        GhostNet net = new GhostNet();
        net.setLat(form.getLat());
        net.setLon(form.getLon());
        net.setSizeEstimate(form.getSizeEstimate());
        net.setStatus(NetStatus.REPORTED);
        net.setReportedAt(LocalDateTime.now());
        net.setReporterName(form.getReporterName());
        net.setReporterPhone(form.getReporterPhone());

        // Optional: Reporter setzen
        personRepo.findFirstByRole(Role.REPORTER).ifPresent(net::setReportedBy);

        ghostNetRepo.save(net);

        // Benutzerrolle prüfen
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isGuest = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GUEST"));

        if (isGuest) {
            // Guest wird ausgeloggt und zur Login-Seite geleitet
            ra.addFlashAttribute("msg", "Danke für Ihre Meldung! Sie wurden abgemeldet.");
            return "redirect:/login?logout";
        }

        // Alle anderen -> normale Weiterleitung
        ra.addFlashAttribute("msg", "Netz erfolgreich gemeldet.");
        return "redirect:/nets";
    }
}
