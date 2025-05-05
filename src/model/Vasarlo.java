package model;

/**
 * A postgres AB 'vasarlo' tablajanak megfelelo osztaly.
 */
public class Vasarlo {
    private int vasarloId;
    private String nev;
    private String cim;
    private Megye megye;

    // az AB 'county' tipusanak megfelelo Enum
    public enum Megye {
        Baranya,
        Bacs_Kiskun,
        Bekes,
        Borsod_Abauj_Zemplen,
        Csongrad_Csanad,
        Fejer,
        Gyor_Moson_Sopron,
        Hajdu_Bihar,
        Heves,
        Jasz_Nagykun_Szolnok,
        Komarom_Esztergom,
        Nograd,
        Pest,
        Somogy,
        Szabolcs_Szatmar_Bereg,
        Tolna,
        Vas,
        Veszprem,
        Zala;

        // a JAVA kompatibilis Enum neveket alakitja az AB-ben hasznaltakra: '_' csere '-'
        public String toDbValue() {
            return this.name().replace('_', '-');
        }

        public static Megye fromDbValue(String dbValue) {
            return Megye.valueOf(dbValue.replace('-', '_'));
        }
    }

    // ID nelkuli konstruktor, INSERT-hez
    public Vasarlo(String nev, String cim, Megye megye) {
        this.nev = nev;
        this.cim = cim;
        this.megye = megye;
    }

    // Konstruktor ID-val, UPDATE es QUERY celjara
    public Vasarlo(int vasarloId, String nev, String cim, Megye megye) {
        this.vasarloId = vasarloId;
        this.nev = nev;
        this.cim = cim;
        this.megye = megye;
    }

    // Getterek es setterek, toString()
    public int getVasarloId() {
        return vasarloId;
    }

    public void setVasarloId(int vasarloId) {
        this.vasarloId = vasarloId;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public Megye getMegye() {
        return megye;
    }

    public void setMegye(Megye megye) {
        this.megye = megye;
    }

    @Override
    public String toString() {
        return "Vasarlo{" +
                "id=" + vasarloId +
                ", nev='" + nev + '\'' +
                ", cim='" + cim + '\'' +
                ", megye=" + megye +
                '}';
    }
}
