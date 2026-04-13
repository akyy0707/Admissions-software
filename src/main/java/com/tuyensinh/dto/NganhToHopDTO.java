package com.tuyensinh.dto;

import javax.persistence.*;

@Entity
@Table(name = "xt_nganh_tohop")
public class NganhToHopDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "manganh", length = 45, nullable = false)
    private String maNganh;

    @Column(name = "matohop", length = 45, nullable = false)
    private String maToHop;

    @Column(name = "th_mon1", length = 10) private String thMon1;
    @Column(name = "hsmon1") private Byte hsMon1; 

    @Column(name = "th_mon2", length = 10) private String thMon2;
    @Column(name = "hsmon2") private Byte hsMon2;

    @Column(name = "th_mon3", length = 10) private String thMon3;
    @Column(name = "hsmon3") private Byte hsMon3;

    @Column(name = "tb_keys", length = 45, unique = true) 
    private String tbKeys; 

    @Column(name = "N1") private Boolean n1;
    @Column(name = "TO") private Boolean to;
    @Column(name = "LI") private Boolean li;
    @Column(name = "HO") private Boolean ho;
    @Column(name = "SI") private Boolean si;
    @Column(name = "VA") private Boolean va;
    @Column(name = "SU") private Boolean su;
    @Column(name = "DI") private Boolean di;
    @Column(name = "TI") private Boolean ti;
    @Column(name = "KHAC") private Boolean khac;
    @Column(name = "KTPL") private Boolean ktpl;

    @Column(name = "dolech", precision = 6, scale = 2)
    private Double doLech;

    public NganhToHopDTO() {}

    // --- GETTER & SETTER ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaNganh() { return maNganh; }
    public void setMaNganh(String maNganh) { this.maNganh = maNganh; }

    public String getMaToHop() { return maToHop; }
    public void setMaToHop(String maToHop) { this.maToHop = maToHop; }

    public String getThMon1() { return thMon1; }
    public void setThMon1(String thMon1) { this.thMon1 = thMon1; }

    public Byte getHsMon1() { return hsMon1; }
    public void setHsMon1(Byte hsMon1) { this.hsMon1 = hsMon1; }

    public String getThMon2() { return thMon2; }
    public void setThMon2(String thMon2) { this.thMon2 = thMon2; }

    public Byte getHsMon2() { return hsMon2; }
    public void setHsMon2(Byte hsMon2) { this.hsMon2 = hsMon2; }

    public String getThMon3() { return thMon3; }
    public void setThMon3(String thMon3) { this.thMon3 = thMon3; }

    public Byte getHsMon3() { return hsMon3; }
    public void setHsMon3(Byte hsMon3) { this.hsMon3 = hsMon3; }

    public String getTbKeys() { return tbKeys; }
    public void setTbKeys(String tbKeys) { this.tbKeys = tbKeys; }

    public Boolean getN1() { return n1; }
    public void setN1(Boolean n1) { this.n1 = n1; }

    public Boolean getTo() { return to; }
    public void setTo(Boolean to) { this.to = to; }

    public Boolean getLi() { return li; }
    public void setLi(Boolean li) { this.li = li; }

    public Boolean getHo() { return ho; }
    public void setHo(Boolean ho) { this.ho = ho; }

    public Boolean getSi() { return si; }
    public void setSi(Boolean si) { this.si = si; }

    public Boolean getVa() { return va; }
    public void setVa(Boolean va) { this.va = va; }

    public Boolean getSu() { return su; }
    public void setSu(Boolean su) { this.su = su; }

    public Boolean getDi() { return di; }
    public void setDi(Boolean di) { this.di = di; }

    public Boolean getTi() { return ti; }
    public void setTi(Boolean ti) { this.ti = ti; }

    public Boolean getKhac() { return khac; }
    public void setKhac(Boolean khac) { this.khac = khac; }

    public Boolean getKtpl() { return ktpl; }
    public void setKtpl(Boolean ktpl) { this.ktpl = ktpl; }

    public Double getDoLech() { return doLech; }
    public void setDoLech(Double doLech) { this.doLech = doLech; }
}