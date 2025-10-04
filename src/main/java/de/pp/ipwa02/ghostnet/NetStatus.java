package de.pp.ipwa02.ghostnet;

/**
 * Status fuer Geisternetze
 * Patrick Praml, IPWA02-01
 */

public enum NetStatus {
    REPORTED,   // wurde gemeldet
    ASSIGNED,   // wurde einem Salvor zugeteilt
    RECOVERED,  // wurde geborgen
    LOST        // am gemeldeten Standort nicht auffindbar (verschollen)
}
