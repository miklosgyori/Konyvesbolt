package model;

import java.time.LocalDate;

/**
 * A postgres AB 'rendeles' tablajanak megfelelo osztaly.
 */
public class Rendeles {
    private int rendelesId;
    private long vasarloId;
    private LocalDate datum;
    private Vasarlo.Megye megye;
    private Statusz statusz;

    // az AB 'order_state' tipusanak megfelelo Enum
    public enum Statusz {
        leadva,
        elfogadva,
        kikuldve,
        teljesult,
        elutasitva;

        // a JAVA kompatibilis Enum neveket alakitja az AB-ben hasznaltakra: '_' csere '-'
        public static Statusz fromDbValue(String dbValue) {
            return Statusz.valueOf(dbValue);
        }

        public String toDbValue() {
            return this.name(); // PostgreSQL ENUM values match lowercase names
        }
    }

    // ID nelkuli konstruktor, INSERT-hez
    public Rendeles(long vasarloId, LocalDate datum, Vasarlo.Megye megye, Statusz statusz) {
        this.vasarloId = vasarloId;
        this.datum = datum;
        this.megye = megye;
        this.statusz = statusz;
    }

    // Konstruktor ID-val, UPDATE es QUERY celjara
    public Rendeles(int rendelesId, long vasarloId, LocalDate datum, Vasarlo.Megye megye, Statusz statusz) {
        this.rendelesId = rendelesId;
        this.vasarloId = vasarloId;
        this.datum = datum;
        this.megye = megye;
        this.statusz = statusz;
    }

    // Getterek es setterek, toString()
    public int getRendelesId() {
        return rendelesId;
    }

    public void setRendelesId(int rendelesId) {
        this.rendelesId = rendelesId;
    }

    public long getVasarloId() {
        return vasarloId;
    }

    public void setVasarloId(long vasarloId) {
        this.vasarloId = vasarloId;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Vasarlo.Megye getMegye() {
        return megye;
    }

    public void setMegye(Vasarlo.Megye megye) {
        this.megye = megye;
    }

    public Statusz getStatusz() {
        return statusz;
    }

    public void setStatusz(Statusz statusz) {
        this.statusz = statusz;
    }

    @Override
    public String toString() {
        return "Rendeles{" +
                "id=" + rendelesId +
                ", vasarloId=" + vasarloId +
                ", datum=" + datum +
                ", megye=" + megye +
                ", statusz=" + statusz +
                '}';
    }
}
