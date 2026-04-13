package com.tuyensinh.dto;

import javax.persistence.*;

@Entity
@Table(name = "xt_tohop_monthi")
public class ToHopDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtohop")
    private int idToHop;

    @Column(name = "matohop", length = 45, unique = true, nullable = false)
    private String maToHop;

    @Column(name = "mon1", length = 10, nullable = false)
    private String mon1;

    @Column(name = "mon2", length = 10, nullable = false)
    private String mon2;

    @Column(name = "mon3", length = 10, nullable = false)
    private String mon3;

    @Column(name = "tentohop", length = 100)
    private String tenToHop;

    public ToHopDTO() {}


    public int getIdToHop() { return idToHop; }
    public void setIdToHop(int idToHop) { this.idToHop = idToHop; }

    public String getMaToHop() { return maToHop; }
    public void setMaToHop(String maToHop) { this.maToHop = maToHop; }

    public String getMon1() { return mon1; }
    public void setMon1(String mon1) { this.mon1 = mon1; }

    public String getMon2() { return mon2; }
    public void setMon2(String mon2) { this.mon2 = mon2; }

    public String getMon3() { return mon3; }
    public void setMon3(String mon3) { this.mon3 = mon3; }

    public String getTenToHop() { return tenToHop; }
    public void setTenToHop(String tenToHop) { this.tenToHop = tenToHop; }
}