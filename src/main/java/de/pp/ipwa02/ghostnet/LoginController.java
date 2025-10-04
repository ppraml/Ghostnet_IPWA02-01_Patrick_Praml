package de.pp.ipwa02.ghostnet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Liefert die Login-Seite (Custom Page statt Default).
 * Patrick Praml, IPWA02-01
 */

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // laedt templates/login.html
    }
}
