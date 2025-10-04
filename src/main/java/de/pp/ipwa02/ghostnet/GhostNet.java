package de.pp.ipwa02.ghostnet;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity fuer ein Geisternetz
 * Patrick Praml, IPWA02-01
 */

@Entity
public class GhostNet {

    @Id
    @GeneratedValue
    private Long id;

    // koordinaten
    private double lat;
    private double lon;

    // wie gross das netz ist (nur schaetzung)
    private String sizeEstimate;

    // status vom netz (REPORTED, ASSIGNED, RECOVERED, LOST)
    @Enumerated(EnumType.STRING)
    private NetStatus status = NetStatus.REPORTED;

    // wann gemeldet
    private LocalDateTime reportedAt;

    // optional: kontakt der meldenden person (anonym moeglich)
    private String reporterName;
    private String reporterPhone;

    // wer hat gemeldet (falls registrierte Person genutzt wird)
    @ManyToOne
    private Person reportedBy;

    // wer ist fuer bergung zustaendig (falls schon assigned)
    @ManyToOne
    private Person salvor;

    // zeitpunkt wann geclaimt wurde (assigned)
    private LocalDateTime claimedAt;

    // zeitpunkt wann recovered wurde
    private LocalDateTime recoveredAt;

    // LOST (verschollen): nur nicht anonym moeglich laut Aufgabenstellung
    private LocalDateTime lostAt;
    private String lostByName;
    private String lostByPhone;

    // getter und setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getSizeEstimate() { return sizeEstimate; }
    public void setSizeEstimate(String sizeEstimate) { this.sizeEstimate = sizeEstimate; }

    public NetStatus getStatus() { return status; }
    public void setStatus(NetStatus status) { this.status = status; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }

    public Person getReportedBy() { return reportedBy; }
    public void setReportedBy(Person reportedBy) { this.reportedBy = reportedBy; }

    public Person getSalvor() { return salvor; }
    public void setSalvor(Person salvor) { this.salvor = salvor; }

    public LocalDateTime getClaimedAt() { return claimedAt; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }

    public LocalDateTime getRecoveredAt() { return recoveredAt; }
    public void setRecoveredAt(LocalDateTime recoveredAt) { this.recoveredAt = recoveredAt; }

    public LocalDateTime getLostAt() { return lostAt; }
    public void setLostAt(LocalDateTime lostAt) { this.lostAt = lostAt; }

    public String getLostByName() { return lostByName; }
    public void setLostByName(String lostByName) { this.lostByName = lostByName; }

    public String getLostByPhone() { return lostByPhone; }
    public void setLostByPhone(String lostByPhone) { this.lostByPhone = lostByPhone; }
}
