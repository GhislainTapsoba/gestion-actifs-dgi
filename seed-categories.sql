INSERT INTO categorie_materiel (id, libelle, description)
SELECT nextval('categorie_materiel_seq'), seed.libelle, seed.description
FROM (
	VALUES
		('Ordinateur de bureau', 'Postes fixes utilisés dans les bureaux'),
		('Ordinateur portable', 'Laptops pour agents mobiles ou cadres'),
		('Imprimante', 'Imprimantes et multifonctions'),
		('Serveur', 'Serveurs physiques de la Direction Informatique'),
		('Onduleur', 'Dispositifs de protection électrique (UPS)'),
		('Scanner', 'Scanners de documents'),
		('Vidéoprojecteur', 'Matériel pour salles de réunion'),
		('Équipement réseau', 'Switches, routeurs, points d''accès Wi-Fi'),
		('Téléphone IP', 'Postes téléphoniques VoIP'),
		('Photocopieur', 'Copieurs multifonctions'),
		('Écran', 'Moniteurs additionnels')
) AS seed(libelle, description)
WHERE NOT EXISTS (
	SELECT 1
	FROM categorie_materiel existing
	WHERE existing.libelle = seed.libelle
);
