use persona_db

// Re-ejecutable: vacía las colecciones antes de cargar los datos de ejemplo
db.persona.drop()
db.profesion.drop()
db.telefono.drop()
db.estudios.drop()

db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false })

db.profesion.insertMany([
	{ "_id": NumberInt(1), "nom": "Ingeniero de Sistemas", "des": "Diseña y desarrolla sistemas de software" },
	{ "_id": NumberInt(2), "nom": "Médico", "des": "Diagnostica y trata enfermedades" },
	{ "_id": NumberInt(3), "nom": "Abogado", "des": "Asesora y representa en asuntos legales" }
], { ordered: false })

db.telefono.insertMany([
	{ "_id": "3001234567", "oper": "Claro", "primaryDuenio": NumberInt(123456789) },
	{ "_id": "3109876543", "oper": "Movistar", "primaryDuenio": NumberInt(321654987) }
], { ordered: false })

// El _id de un estudio es "idPersona-idProfesion" (ver EstudiosMapperMongo.validateId)
db.estudios.insertMany([
	{ "_id": "123456789-1", "primaryPersona": NumberInt(123456789), "primaryProfesion": NumberInt(1),
	  "fecha": new Date("2015-12-10"), "univer": "Pontificia Universidad Javeriana" },
	{ "_id": "321654987-2", "primaryPersona": NumberInt(321654987), "primaryProfesion": NumberInt(2),
	  "fecha": new Date("2018-06-30"), "univer": "Universidad Nacional de Colombia" }
], { ordered: false })
