# PF-18 — Peluquería de Mascotas

Proyecto académico de Programación II para administrar servicios y citas de estética de mascotas.

**Autor:** Walter Isaí Villagrán García  
**Carné:** completar con el carné real antes de entregar (no inventado).  
**Repositorio:** https://github.com/Walter1612/Proyecto-Final---Backend-

## Tecnologías
Java 17+, Spring Boot, Maven, Spring Web MVC, Spring Data JPA, Bean Validation, PostgreSQL, HTML, CSS y JavaScript.

## Arquitectura
- `controller`: endpoints HTTP y códigos de respuesta.
- `service`: reglas del negocio, conversiones a DTO y operaciones.
- `repository`: consultas con Spring Data JPA.
- `entity`: entidades relacionadas `Cita` N:1 `ServicioEstetica`.
- `dto`: solicitudes validadas y respuestas públicas.
- `exception`: errores de negocio y manejo centralizado.
- `src/main/resources/static`: interfaz web que consume la API.

## Preparar base de datos y ejecutar
1. Crear una base PostgreSQL denominada `peluqueria_mascotas`.
2. Ejecutar `database/peluqueria_mascotas.sql` en esa base. **Revisar duplicados activos antes de aplicar el índice único de horario** si la base ya contiene datos.
3. Opcional, solo para una base de práctica: ejecutar `database/datos_prueba.sql` (10 citas y 4 servicios **ficticios**; no utilizar en producción). El script de datos intenta no repetir filas activas con el mismo horario.
4. Configurar las variables `DB_PASSWORD` (obligatoria), `DB_USER` (predeterminado `postgres`) y opcionalmente `DB_URL` (predeterminado `jdbc:postgresql://localhost:5432/peluqueria_mascotas`). Nunca subir contraseñas a GitHub.
5. En Windows PowerShell: `$env:DB_PASSWORD='TU_CLAVE_LOCAL'` y `./mvnw.cmd spring-boot:run`. En Linux/macOS: `export DB_PASSWORD='TU_CLAVE_LOCAL'; ./mvnw spring-boot:run`.
6. Abrir `http://localhost:8080` para probar la interfaz. Requiere PostgreSQL en ejecución y Java instalado.
7. Ejecutar pruebas unitarias con `./mvnw.cmd test` (Windows) o `./mvnw test` (Linux/macOS). La prueba `contextLoads` requiere conexión a PostgreSQL.

## API REST

### Servicios
| Método | Ruta | Propósito |
|---|---|---|
| POST | `/api/servicios` | Crear servicio |
| GET | `/api/servicios` | Listar |
| GET | `/api/servicios/activos` | Filtrar activos |
| GET | `/api/servicios/{id}` | Consultar por ID |
| PUT | `/api/servicios/{id}` | Actualizar |
| DELETE | `/api/servicios/{id}` | Desactivar sin borrar histórico |

### Citas
| Método | Ruta | Propósito |
|---|---|---|
| POST | `/api/citas` | Registrar cita programada |
| GET | `/api/citas` | Listar |
| GET | `/api/citas/{id}` | Consultar por ID |
| GET | `/api/citas/estado/{estado}` | Filtrar por estado |
| GET | `/api/citas/fecha/{fecha}` | Filtrar por fecha ISO (AAAA-MM-DD) |
| PUT | `/api/citas/{id}` | Modificar cita programada |
| PATCH | `/api/citas/{id}/estado/{estado}` | Atender o cancelar |
| PATCH | `/api/citas/{id}/cancelar` | Cancelar |
| GET | `/api/citas/indicadores/servicios-mas-solicitados` | Conteo por servicio |
| GET | `/api/citas/indicadores/total-estimado/{fecha}` | Total estimado sin canceladas |

Ejemplo JSON para `POST /api/citas`:
```json
{"nombreMascota":"Luna","propietario":"Cliente de ejemplo","especie":"Perro","fechaHora":"2027-02-01T09:00:00","servicioId":1}
```
El `servicioId` debe existir y estar activo. Para servicios: `{ "nombre": "Baño", "descripcion": "Baño básico", "precio": 80.00, "duracionMinutos": 45, "activo": true }`.

## Reglas y errores
- Al crear una cita, su estado debe ser `PROGRAMADA`. No pueden existir dos citas activas en el mismo horario; un índice parcial de PostgreSQL refuerza la validación ante concurrencia.
- No se puede reservar con servicios desactivados.
- Solo una cita `PROGRAMADA` puede editarse, atenderse o cancelarse. Las citas `ATENDIDA` y `CANCELADA` son finales.
- `400`: datos inválidos, formato incorrecto o validaciones. `404`: ID inexistente. `409`: duplicados, horario ocupado, transición inválida o conflicto con restricciones.
- La interfaz permite registrar y editar servicios/citas, desactivar servicios, atender/cancelar citas, filtrar agenda y consultar indicadores.

## Evidencia para entrega
- Script de estructura: `database/peluqueria_mascotas.sql`.
- Datos ficticios: `database/datos_prueba.sql`.
- Colección de Postman: carpeta `postman/` (importar y ejecutar contra `http://localhost:8080`).
- Evidencias originales: carpeta `Evidencias/` (conservada).
- Guía de verificaciones por realizar: `Evidencias/GUIA_VALIDACION.md`.
- Pruebas unitarias: `src/test/java/`.

**Pendiente de ejecutar en equipo local:** pruebas reales con PostgreSQL, Postman y capturas nuevas para el documento Word. No se atribuye aprobación a comprobaciones no ejecutadas.
