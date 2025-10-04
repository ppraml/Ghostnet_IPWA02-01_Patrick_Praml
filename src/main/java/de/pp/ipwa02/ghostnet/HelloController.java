package de.pp.ipwa02.ghostnet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * simple hello seite
 * Patrick Praml, IPWA02-01
 */

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        // laedt die view datei src/main/resources/templates/index.html
        return "index";
    }
}
