package de.pp.ipwa02.ghostnet;

import jakarta.persistence.*;

/**
 * Entity fuer eine Person
 * Patrick Praml, IPWA02-01
 */

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    // name der person
    private String name;

    // telefon fuer rueckfragen (optional fuer reporter, noetig fuer bergende)
    private String phone;

    // rolle der person (REPORTER oder SALVOR)
    @Enumerated(EnumType.STRING)
    private Role role;

    // getter und setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
