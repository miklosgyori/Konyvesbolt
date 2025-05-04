package model;

import java.time.LocalDate;

/**
 * A postgres AB 'konyv' tablajanak megfelelo osztaly.
 */
public class Konyv {

    private int konyvId;           // konyv_id, SERIAL, PRIMARY KEY
    private String szerzok;        // szerzok, TEXT, NULL
    private String cim;            // cim, TEXT, NOT NULL
    private String kiado;          // kiado, TEXT, NULL
    private LocalDate kiadasEve;   // kiadas_eve, DATE, NULL
    private int egysegar;          // egysegar, INT, NOT NULL
    private short keszlet;         // keszlet, SMALLINT, NOT NULL

    /**
     * Konstruktor, konyvID nelkul, INSERT celjara. (ID-t az AB generalja.)
     * @param szerzok
     * @param cim
     * @param kiado
     * @param kiadasEve
     * @param egysegar
     * @param keszlet
     */
    public Konyv(String szerzok, String cim, String kiado, LocalDate kiadasEve, int egysegar, short keszlet) {
        this.szerzok = szerzok;
        this.cim = cim;
        this.kiado = kiado;
        this.kiadasEve = kiadasEve;
        this.egysegar = egysegar;
        this.keszlet = keszlet;
    }

    /**
     * Konstruktor, konyvID-val, UPDATE es QUERY celjara, feltelezi, hogy a KonyvID mar generalodott az INSERT soran.
     * @param konyvId
     * @param szerzok
     * @param cim
     * @param kiado
     * @param kiadasEve
     * @param egysegar
     * @param keszlet
     */
    public Konyv(int konyvId, String szerzok, String cim, String kiado, LocalDate kiadasEve, int egysegar, short keszlet) {
        this.konyvId = konyvId;
        this.szerzok = szerzok;
        this.cim = cim;
        this.kiado = kiado;
        this.kiadasEve = kiadasEve;
        this.egysegar = egysegar;
        this.keszlet = keszlet;
    }

    // Getterek, setterek, toString
    public int getKonyvId() {
        return konyvId;
    }

    public void setKonyvId(int konyvId) {
        this.konyvId = konyvId;
    }

    public String getSzerzok() {
        return szerzok;
    }

    public void setSzerzok(String szerzok) {
        this.szerzok = szerzok;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getKiado() {
        return kiado;
    }

    public void setKiado(String kiado) {
        this.kiado = kiado;
    }

    public LocalDate getKiadasEve() {
        return kiadasEve;
    }

    public void setKiadasEve(LocalDate kiadasEve) {
        this.kiadasEve = kiadasEve;
    }

    public int getEgysegar() {
        return egysegar;
    }

    public void setEgysegar(int egysegar) {
        this.egysegar = egysegar;
    }

    public short getKeszlet() {
        return keszlet;
    }

    public void setKeszlet(short keszlet) {
        this.keszlet = keszlet;
    }

    @Override
    public String toString() {
        return "Konyv{" +
                "id= " + konyvId +
                "; szerzok= '" + szerzok + '\'' +
                "; cim= '" + cim + '\'' +
                "; kiado= '" + kiado + '\'' +
                "; kiadasEve= " + kiadasEve +
                "; egysegar= " + egysegar +
                "; keszlet= " + keszlet +
                '}';
    }
}
