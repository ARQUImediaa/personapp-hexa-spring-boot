INSERT INTO
	`persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`)
VALUES
	(123456789,'Pepe','Perez','M',30),
	(987654321,'Pepito','Perez','M',null),
	(321654987,'Pepa','Juarez','F',30),
	(147258369,'Pepita','Juarez','F',10),
	(963852741,'Fede','Perez','M',18);

INSERT INTO
	`persona_db`.`profesion`(`id`,`nom`,`des`)
VALUES
	(1,'Ingeniero de Sistemas','Diseña y desarrolla sistemas de software'),
	(2,'Médico','Diagnostica y trata enfermedades'),
	(3,'Abogado','Asesora y representa en asuntos legales');

INSERT INTO
	`persona_db`.`telefono`(`num`,`oper`,`duenio`)
VALUES
	('3001234567','Claro',123456789),
	('3109876543','Movistar',321654987);

INSERT INTO
	`persona_db`.`estudios`(`id_prof`,`cc_per`,`fecha`,`univer`)
VALUES
	(1,123456789,'2015-12-10','Pontificia Universidad Javeriana'),
	(2,321654987,'2018-06-30','Universidad Nacional de Colombia');
