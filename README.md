p# Könyvesbolt-kezelő alkalmazás (Java Swing + PostgreSQL)

Asztali Java alkalmazás, amely egy könyvesbolt könyveit, vásárlóit és rendeléseit kezeli. A grafikus felhasználói
felület Swing alapú, az adatok egy PostgreSQL adatbázisban tárolódnak, Docker konténerben futtatva.
Ezt az adatbázist a korábbi 'Adatbázis szerverek' kurzus beadandó keretében hoztam létre.

---

## Szerző

Név: Győri Miklós Dr, UTH2H2
Kurzus: Alkalmazás-fejlesztési technológiák

---
## Használt technológiák

- Java 18
- Swing (Java GUI)
- JDBC (adatbázis elérés)
- PostgreSQL (Docker konténerben) => A korábbi 'Adatbázis szerverek' kurzus beadandó keretében létrehozott AB.
- IntelliJ IDEA (Community Edition)

---

## Publikus git repo
https://github.com/miklosgyori/Konyvesbolt/commits/master/

---

## Telepítés és futtatás

1. Indítsd el a PostgreSQL konténert a projektben található Docker image alapján:

   ```bash
   docker load -i konyvesbolt-image.tar
   docker run -d --name pg -p 5488:5432 konyvesbolt-image
   ```

2. Ellenőrizd a JDBC kapcsolatot a `Database.java` fájlban:
   ```
   jdbc:postgresql://localhost:5488/konyvesbolt
   ```

3. Nyisd meg a projektet IntelliJ IDEA-ban.

4. Futtasd a `Main.java` fájlt az alkalmazás indításához.

---

## Konténer leállítása (opcionális)

Ha le szeretnéd állítani vagy törölni a konténert:

```bash
docker stop konyvesbolt-db
docker rm konyvesbolt-db
```

---

## Tesztadatok

Az adatbázis tartalmaz több ezer könyvet, vásárlót, rendelést, demonstrációs céllal.

A belépési adatok:
- Adatbazis nev: postgres
- Port: 5488:5432
- Felhasználó: `postgres`
- Jelszó: `postgres`

---

## Funkciók

- Könyvek kezelése (hozzáadás, módosítás, törlés, keresés, listázás)
- Vásárlók kezelése (hozzáadás, módosítás, törlés, keresés, listázás)
- Rendelések kezelése (hozzáadás, módosítás, törlés, keresés, listázás)
- Táblázatok oszlop szerint rendezhetők (fejlécre kattintással)
- Kilépés gomb megerősítéssel

---

## Fájlok a projektben

- `/src/` — Java forráskódok (model, view, test csomagok, Main.java)
- `/docker/konyvesbolt-image.tar` — elmentett Docker image a PostgreSQL adatbázishoz
- `README.md` — ez a fájl

---

## Ismert hibák / korlátok

- Nincs élő idegen kulcs ellenőrzés (pl. nem létező vásárlóra is létrehozható rendelés)
- Könyv-, vásárló- és rendelés-azonosítókat kézzel kell megadni a módosításhoz / törléshez
- Hibakezelés minimális (pl. adatbázis elérés hiba esetén)
- A GUI nem kezeli a tetel tablat, igy valódi tételek nem hozhatóak létre a GUI-n keresztül a rendelésekhez.
- TOVÁBBI KORLÁTOK a javadoc dokumentációban, az osztályoknál.
---


