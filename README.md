# Ghost Net Fishing – Shea Sephard  
Programm für die Fallstudie im Modul IPWA02-01  
Patrick Praml – Matrikelnummer IU14080751  

---

## 1. Projektbeschreibung

Das Projekt „Ghost Net Fishing – Shea Sephard“ wurde im Rahmen des IU-Moduls *IPWA02-01 PROGRAMMIERUNG VON INDUSTRIELLEN INFORMATIONSSYSTEMEN MIT JAVA EE* entwickelt.  
Ziel der Anwendung ist die Erfassung, Verwaltung und Nachverfolgung sogenannter *Geisternetze* – herrenlose oder verlorene Fischernetze in Gewässern.  

Die Anwendung erlaubt das Melden neuer Funde, die Zuweisung an Bergungsteams und die abschließende Markierung als geborgen oder verschollen. Sie wurde mit dem Framework **Spring Boot** unter Verwendung von **Thymeleaf**, **Spring Security** und einer **H2-In-Memory-Datenbank** umgesetzt.

---

## 2. Technische Umsetzung

| Komponente | Technologie |
|-------------|-------------|
| Programmiersprache | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Build-Tool | Maven (Wrapper integriert) |
| View-Schicht | Thymeleaf-Templates |
| Datenbank | H2 (In-Memory) |
| Frontend-Erweiterung | Leaflet.js (Kartenansicht) |
| Sicherheitsmechanismus | Spring Security mit Rollensteuerung |

Die Anwendung ist vollständig eigenständig lauffähig und benötigt keine externe Datenbank.  
Beim Start werden über die Klasse *DataLoader* Standardbenutzer und Testdaten angelegt.

---

## 3. Aufbau und Architektur

Das Projekt folgt dem MVC-Muster:

- **Model**: Datenklassen *GhostNet*, *Person* sowie die Enumerationen *Role* und *NetStatus*.  
- **View**: HTML-Templates mit Thymeleaf für Startseite, Meldung, Liste, Login.  
- **Controller**: *HomeController*, *ReportController* und *NetController* steuern die Navigation und Geschäftslogik.  
- **Service**: *GhostNetService* kapselt Statuswechsel und Prüflogik.  
- **Repositorys**: *GhostNetRepo* und *PersonRepo* ermöglichen CRUD-Zugriffe über Spring Data JPA.

Die Anwendung verwendet eine einfache In-Memory-Datenhaltung, die bei jedem Start neu initialisiert wird.

---

## 4. Rollen und Berechtigungen

| Rolle | Benutzername | Passwort | Zugriffsrechte |
|-------|---------------|-----------|----------------|
| **Admin** | admin | admin | Vollzugriff auf alle Funktionen und H2-Konsole |
| **Reporter** | reporter | reporter | Netze melden und anzeigen |
| **Berger (Salvor)** | berger | berger | Zuweisung und Bergung von Netzen |
| **Guest** | guest | guest | Nur Meldungen möglich, automatische Abmeldung nach Meldung |

Die Zugriffsrechte werden über die Klasse *SecurityConfig* und Spring Security geregelt.  
Jede Route ist einer oder mehreren Rollen explizit zugeordnet.

---

## 5. Funktionsweise

1. **Login**  
   Nach dem Start der Anwendung erfolgt eine Anmeldung über die Login-Maske.  
   Gäste werden nach dem Login direkt auf das Meldeformular weitergeleitet, alle anderen Rollen gelangen auf die Startseite.

2. **Startseite**  
   Die Startseite zeigt eine interaktive Karte mit allen gemeldeten Geisternetzen.  
   Als Grundlage dient Leaflet mit OpenStreetMap-Kacheln.

3. **Meldung eines Netzes**  
   Über das Formular können Koordinaten, eine Größenschätzung sowie optional Name und Telefonnummer des Meldenden eingegeben werden.  
   Eingaben werden über *Jakarta Validation* überprüft.

4. **Zuweisung und Bergung**  
   Admins und Berger können gemeldete Netze einem Bergungsteam zuweisen (Team 1 / Team 2).  
   Nach erfolgreicher Bergung wird der Status auf *RECOVERED* gesetzt.

5. **Verschollen-Meldung**  
   Nur zugewiesene Netze können als verschollen markiert werden.  
   Für offene Meldungen ist dies gesperrt.

6. **Logout**  
   Nach dem Logout erfolgt eine Rückleitung zur Login-Maske.  
   Gäste werden nach einer Meldung automatisch abgemeldet.

---

## 6. Datenhaltung und H2-Zugriff

Die Daten werden zur Laufzeit im Speicher gehalten.  
Zur optionalen Kontrolle kann die H2-Konsole über  
http://localhost:8080/h2-console 
geöffnet werden.

**Verbindungsdaten:**

| Feld | Wert |
|------|------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Benutzer | `sa` |
| Passwort | *(leer)* |

---

## 7. Start der Anwendung

Voraussetzungen:  
- Java 17  
- Maven 3 (Wrapper ist integriert)

Startbefehl unter Windows:

```bash
.\mvnw.cmd -U clean package
.\mvnw.cmd spring-boot:run

Webanwendung starten:
http://localhost:8080/