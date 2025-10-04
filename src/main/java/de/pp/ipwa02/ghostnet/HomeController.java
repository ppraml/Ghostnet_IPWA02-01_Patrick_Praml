package de.pp.ipwa02.ghostnet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Startseite mit Karte (ohne Liste)
 * Patrick Praml, IPWA02-01
 */

@Controller
public class HomeController {

    private final GhostNetRepo ghostNetRepo;

    public HomeController(GhostNetRepo ghostNetRepo) {
        this.ghostNetRepo = ghostNetRepo;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<GhostNet> nets = ghostNetRepo.findAllByOrderByReportedAtDesc();
        model.addAttribute("nets", nets);
        return "home"; // laedt templates/home.html
    }
}
