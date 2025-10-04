package de.pp.ipwa02.ghostnet;

/**
 * Rollen fuer Personen (Reporter/Bergung/Admin/Gast)
 * Hinweis: Code-Rollennamen bleiben Englisch; UI zeigt deutsche Begriffe.
 * Patrick Praml, IPWA02-01
 */

public enum Role {
    REPORTER,  // meldende Person (kann Netze melden)
    SALVOR,    // Bergung (zuweisen & als geborgen markieren)
    ADMIN,     // Admin (darf alles)
    GUEST      // Gast (lesen + melden)
}
