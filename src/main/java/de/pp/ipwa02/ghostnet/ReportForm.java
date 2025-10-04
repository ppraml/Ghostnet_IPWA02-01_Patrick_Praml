package de.pp.ipwa02.ghostnet;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Formular fuer Meldung eines Geisternetzes
 * Patrick Praml, IPWA02-01
 */

public class ReportForm {

    // lat/lon muessen gesetzt sein und in sinnvollen grenzen liegen
    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double lat;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double lon;

    // groessen-schaetzung (frei als text)
    @NotBlank
    private String sizeEstimate;

    // optionale felder (im template vorhanden)
    @Size(max = 120)
    private String reporterName;

    @Size(max = 40)
    private String reporterPhone;

    // getter/setter
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public String getSizeEstimate() { return sizeEstimate; }
    public void setSizeEstimate(String sizeEstimate) { this.sizeEstimate = sizeEstimate; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }
}
