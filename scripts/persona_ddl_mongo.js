use admin

// Re-ejecutable: solo crea el usuario si no existe.
// No se borra y recrea: la API ya tiene sesiones abiertas con él.
if (db.getUser("persona_db") === null) {
  db.createUser({
    user: "persona_db",
    pwd: "persona_db",
    roles: [
      { role: "read", db: "persona_db" },
      { role: "readWrite", db: "persona_db" },
      { role: "dbAdmin", db: "persona_db" }
    ],
    mechanisms: ["SCRAM-SHA-1","SCRAM-SHA-256"]
  })
}
