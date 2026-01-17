# Projekt-za-softversko
Projekt za softversko

(Klara: ) Upute za bazu podataka:
Ako nemate kreiranu tablicu 'kalendar' kreirajte ju ručno u MySQL (rwa.studenti.math.hr) ovako:
1. Klik na SQL (gore lijevo).
2. Unesete call.mysql.create_db('kalendar', 'username') (kreira se baza podataka imena 'username_kalendar')
3. Kliknete na 'go'.

Zatim
na lokaciji src/main/resources/config kreirati datoteku pod nazivom db.properties i kopirati:

db.host=rwa.studenti.math.hr
db.port=3306
db.name=username_kalendar
db.user=username
db.password=password

db.params=useSSL=true&serverTimezone=UTC

Za username unijeti svoj, za password unijeti svoj jmbag.
Ako ste uspješno unijeli svoje podatke bi vam pokretanjem programa trebale biti kreirane tablice u bazi.
db.properties stavljamo pod .gitignore da se ne sprema na git !
