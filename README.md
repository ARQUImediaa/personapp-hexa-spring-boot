# PersonAPP: servicio web hexagonal (Laboratorio 2)

Servicio web con arquitectura hexagonal (puertos y adaptadores), Repository y Service, para el modelo de datos **persona, profesión, teléfono y estudio**. Persiste en **MariaDB** o **MongoDB** (se elige en cada petición) y se usa por **REST** (Swagger 3) o por **CLI** (menú de terminal).

Stack: JDK 11, Spring Boot 2.7.11, MariaDB 10.11, MongoDB 6, Swagger (springdoc).

Todo se ejecuta con **Docker**. No hace falta instalar Java, Maven ni bases de datos en el equipo.

## 1. Requisitos

- Docker con Docker Compose v2 (Docker Desktop en Windows, o Docker en Linux/WSL).
- Puertos libres en el host: `3000` (API), `3307` (MariaDB) y `27017` (MongoDB).

## 2. Puesta en marcha

Desde la raíz del repositorio:

```bash
docker compose up -d --build
```

Esto hace lo siguiente:

1. Levanta **MariaDB** en `localhost:3307`. Al primer arranque ejecuta `scripts/persona_ddl_maria.sql` (crea la base `persona_db`, el usuario y las tablas) y `scripts/persona_dml_maria.sql` (datos de ejemplo).
2. Levanta **MongoDB** en `localhost:27017`.
3. Ejecuta el servicio `mongo-init`, que aplica `scripts/persona_ddl_mongo.js` (usuario `persona_db`) y `scripts/persona_dml_mongo.js` (datos de ejemplo). Termina solo, con estado `Exited (0)`.
4. Compila la API dentro de un contenedor Maven con JDK 11 y la ejecuta en `localhost:3000`.

Para comprobar que todo está arriba:

```bash
docker compose ps
```

Los servicios `maria` y `mongo` deben estar `healthy` o `running`, `api-rest` `running`, y `mongo-init` `exited (0)`.

### Reiniciar con datos de ejemplo limpios

Los scripts de MariaDB solo corren cuando el volumen está vacío. Para volver a los datos de ejemplo:

```bash
docker compose down -v
docker compose up -d --build
```

`down -v` borra los datos. Con `docker compose down` (sin `-v`) los datos se conservan.

### Detener

```bash
docker compose down
```

## 3. Uso por REST (Swagger)

Abre **http://localhost:3000/swagger-ui.html** (redirige a `/swagger-ui/index.html`). La especificación OpenAPI está en **http://localhost:3000/api-docs**.

La base de datos se indica en la ruta con `MARIA` o `MONGO` (sin distinguir mayúsculas). Una base inválida responde `400`.

### Endpoints

| Recurso | Método y ruta | Descripción |
|---|---|---|
| Persona | `GET /api/v1/persona/{db}` | Lista todas las personas |
| | `GET /api/v1/persona/{db}/count` | Cuenta las personas |
| | `GET /api/v1/persona/{db}/{dni}` | Busca una persona |
| | `POST /api/v1/persona` | Crea una persona (la base va en el cuerpo) |
| | `PUT /api/v1/persona/{db}/{dni}` | Edita una persona |
| | `DELETE /api/v1/persona/{db}/{dni}` | Elimina una persona |
| Profesión | `GET /api/v1/profesion/{db}` | Lista las profesiones |
| | `GET /api/v1/profesion/{db}/count` | Cuenta las profesiones |
| | `GET /api/v1/profesion/{db}/{id}` | Busca una profesión |
| | `POST /api/v1/profesion/{db}` | Crea una profesión |
| | `PUT /api/v1/profesion/{db}/{id}` | Edita una profesión |
| | `DELETE /api/v1/profesion/{db}/{id}` | Elimina una profesión |
| Teléfono | `GET /api/v1/telefono/{db}` | Lista los teléfonos |
| | `GET /api/v1/telefono/{db}/count` | Cuenta los teléfonos |
| | `GET /api/v1/telefono/{db}/{numero}` | Busca un teléfono |
| | `POST /api/v1/telefono/{db}` | Crea un teléfono |
| | `PUT /api/v1/telefono/{db}/{numero}` | Edita un teléfono |
| | `DELETE /api/v1/telefono/{db}/{numero}` | Elimina un teléfono |
| Estudio | `GET /api/v1/estudio/{db}` | Lista los estudios |
| | `GET /api/v1/estudio/{db}/count` | Cuenta los estudios |
| | `GET /api/v1/estudio/{db}/{idPersona}/{idProfesion}` | Busca un estudio |
| | `POST /api/v1/estudio/{db}` | Crea un estudio |
| | `PUT /api/v1/estudio/{db}/{idPersona}/{idProfesion}` | Edita un estudio |
| | `DELETE /api/v1/estudio/{db}/{idPersona}/{idProfesion}` | Elimina un estudio |

Cuerpos de ejemplo:

```json
// POST /api/v1/persona
{ "dni": "700001", "firstName": "Ana", "lastName": "Prueba", "age": "30", "sex": "F", "database": "MARIA" }

// POST /api/v1/profesion/MARIA
{ "id": "100", "nombre": "Ingeniero", "descripcion": "Ingeniería de sistemas" }

// POST /api/v1/telefono/MARIA
{ "numero": "3001234567", "operador": "Claro", "dniDuenio": "700001" }

// POST /api/v1/estudio/MARIA
{ "idProfesion": "100", "dniPersona": "700001", "fechaGraduacion": "2020-06-15", "universidad": "Javeriana" }
```

En persona, `sex` acepta `M`, `F` u `O` (también `MALE` y `FEMALE`). En estudio, `fechaGraduacion` va como `yyyy-MM-dd` y puede omitirse.

### Códigos de respuesta

| Código | Cuándo |
|---|---|
| `200` | Operación correcta. Los borrados responden `true`. |
| `400` | Base de datos inválida o dato mal formado (número, fecha). |
| `404` | El registro no existe. |
| `409` | Se intenta crear un registro cuya llave ya existe. |

### Reglas de edición (PUT)

El PUT es **parcial**: los campos que no se envían conservan su valor actual. Por ejemplo, `PUT /api/v1/telefono/MARIA/3001234567` con `{"operador": "Tigo"}` cambia solo el operador.

### Borrado en cascada

- **Persona:** al borrarla se borran sus teléfonos y sus estudios.
- **Profesión:** al borrarla se borran sus estudios.

Esto aplica en MariaDB (por las relaciones de la entidad) y en MongoDB (lo hace el adaptador, porque Mongo no tiene llaves foráneas).

## 4. Uso por CLI

El CLI es un menú interactivo. Necesita que `maria` y `mongo` estén corriendo (lo están si ya hiciste `docker compose up -d`):

```bash
docker compose run --rm --build cli
```

El menú principal tiene un módulo por entidad (`1` persona, `2` profesión, `3` teléfono, `4` estudio, `0` salir). Cada módulo pregunta si usas MariaDB (`1`) o MongoDB (`2`), y luego ofrece:

- `1` listar, `2` buscar, `3` crear, `4` editar, `5` eliminar, `6` contar, `0` regresar.

Al crear, si la llave ya existe, el CLI avisa y no sobrescribe el registro. Las fechas de estudio van como `yyyy-MM-dd`.

Para salir de la sesión, regresa con `0` en cada menú hasta llegar al principal.

## 5. Datos de ejemplo

| Entidad | MariaDB (`persona_dml_maria.sql`) | MongoDB (`persona_dml_mongo.js`) |
|---|---|---|
| Persona | 5 personas (DNI `123456789`, `987654321`, `321654987`, `147258369`, `963852741`) | Las mismas 5 |
| Profesión | 3 (`1` Ingeniero de Sistemas, `2` Médico, `3` Abogado) | Las mismas 3 |
| Teléfono | 2 (`3001234567` de DNI `123456789`, `3109876543` de DNI `321654987`) | Los mismos 2 |
| Estudio | 2 (persona `123456789` en profesión `1`; persona `321654987` en profesión `2`) | Los mismos 2 |

El script de MongoDB vacía sus colecciones antes de cargar, así que se puede volver a ejecutar. El de MariaDB solo corre en el primer arranque del volumen.

## 6. Arquitectura

```
                 REST (Spring MVC)            CLI (menú de terminal)
                        │                              │
              rest-input-adapter              cli-input-adapter
              (controllers, mappers,          (menús, mappers,
               adaptadores de entrada)         adaptadores de entrada)
                        │                              │
                        └──────────────┬───────────────┘
                                       ▼
                         application (puertos in/out y casos de uso = Service)
                                       │
                 ┌─────────────────────┴─────────────────────┐
                 ▼                                           ▼
     maria-output-adapter                          mongo-output-adapter
     (Repository JPA, entidades,                   (Repository Mongo, documentos,
      mappers)                                      mappers)
                 │                                           │
                 ▼                                           ▼
             MariaDB 10.11                                MongoDB 6
                                 domain (Person, Phone, Profession, Study)
                                 common (anotaciones, excepciones, enums)
```

Módulos Maven (`pom.xml` raíz):

| Módulo | Contenido |
|---|---|
| `common` | Anotaciones (`@Adapter`, `@Port`, `@UseCase`, `@Mapper`), excepciones y `DatabaseOption` |
| `domain` | Modelo del dominio: `Person`, `Phone`, `Profession`, `Study`, `Gender` |
| `application` | Puertos de entrada y salida, y casos de uso (`PersonUseCase`, `ProfessionUseCase`, `PhoneUseCase`, `StudyUseCase`) |
| `maria-output-adapter` | Adaptadores de salida para MariaDB |
| `mongo-output-adapter` | Adaptadores de salida para MongoDB |
| `rest-input-adapter` | API REST: controllers, mappers, modelos de request y response, manejo de errores |
| `cli-input-adapter` | Aplicación de terminal: menús, adaptadores de entrada y mappers |

Archivos de despliegue:

| Archivo | Para qué sirve |
|---|---|
| `docker-compose.yml` | Define los servicios `maria`, `mongo`, `mongo-init`, `api-rest` y `cli` |
| `docker/rest.Dockerfile` | Compila la API con Maven (JDK 11) y la ejecuta con JRE 11 |
| `docker/cli.Dockerfile` | Compila el CLI y lo ejecuta con JRE 11 |
| `scripts/` | DDL y DML de MariaDB y MongoDB |

## 7. Comportamiento a tener en cuenta

- Las llaves se validan: crear algo con llave repetida responde `409`; editar o borrar algo inexistente responde `404`.
- Los IDs que vienen en la ruta mandan sobre los del cuerpo al editar.
- Las contraseñas del compose (`root` para los administradores de cada base y `persona_db` para la aplicación) son solo de desarrollo local.

## 8. Estructura del repositorio

```
.
├── README.md
├── docker-compose.yml
├── docker/
│   ├── rest.Dockerfile
│   └── cli.Dockerfile
├── scripts/
│   ├── persona_ddl_maria.sql
│   ├── persona_dml_maria.sql
│   ├── persona_ddl_mongo.js
│   └── persona_dml_mongo.js
├── common/
├── domain/
├── application/
├── maria-output-adapter/
├── mongo-output-adapter/
├── rest-input-adapter/
├── cli-input-adapter/
└── pom.xml
```
