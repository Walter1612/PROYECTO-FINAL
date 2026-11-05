# PF-18 — Sistema de Citas para Peluquería de Mascotas

Proyecto final de **Programación II** para gestionar servicios de estética para mascotas, registrar y administrar citas, consultar la agenda y visualizar indicadores. Incluye una **API REST en Spring Boot**, persistencia en **PostgreSQL** y una interfaz web desarrollada con **HTML, CSS y JavaScript**.

**Estudiante:** Walter Isaí Villagran Garcia  
**Canet:** 9941-21-8603
**Repositorio oficial de entrega:** https://github.com/Walter1612/PROYECTO-FINAL  
**Última validación documentada:** 5 de noviembre de 2026.

## Funcionalidades

- **Servicios de estética:** registrar, consultar, editar y desactivar servicios sin eliminar su historial.
- **Citas:** registrar, consultar, modificar, cancelar y marcar como atendidas.
- **Reglas de negocio:** impedir citas activas con la misma fecha y hora; comprobar que el servicio esté activo; evitar modificaciones de citas finalizadas.
- **Agenda:** consultar las citas por fecha.
- **Indicadores:** servicios más solicitados y total estimado por día, sin contabilizar citas canceladas.
- **Validación:** DTO con restricciones de campos y respuestas HTTP controladas ante errores.

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje de backend | Java (compilación configurada para Java 17) |
| Framework | Spring Boot 4.1.1, Spring Web MVC |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | PostgreSQL |
| Validación | Jakarta Bean Validation |
| Frontend | HTML, CSS y JavaScript |
| Compilación y pruebas | Maven Wrapper, JUnit y Mockito |
| Pruebas de API | Colección Postman incluida |

> En las comprobaciones locales del 5 de noviembre de 2026 se utilizó Java 26, Maven Wrapper y PostgreSQL 18.6. No es necesario instalar Maven globalmente si se utiliza `mvnw.cmd`.


## Instalación y ejecución local

### 1. Requisitos

- JDK 17 o superior compatible con el proyecto.
- PostgreSQL en ejecución.
- Git para clonar el repositorio.

### 2. Clonar el proyecto

```powershell
git clone https://github.com/Walter1612/PROYECTO-FINAL.git
cd PROYECTO-FINAL
```

### 3. Preparar PostgreSQL

1. Crear una base de datos denominada `peluqueria_mascotas`.
2. Ejecutar el archivo `database/peluqueria_mascotas.sql` sobre esa base.
3. **Opcional:** cargar `database/datos_prueba.sql` en una base destinada a pruebas. Incluye datos ficticios; no se recomienda ejecutarlo en una base con información real.

> Si ya existen citas en la base de datos, revisar los conflictos de horarios activos antes de ejecutar el índice único parcial del script SQL.

### 4. Configurar la conexión sin exponer credenciales

El archivo `src/main/resources/application.properties` debe usar una variable de entorno para la contraseña:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/peluqueria_mascotas
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

Si el usuario de PostgreSQL, la base o el puerto son diferentes, ajustar la configuración localmente. **Nunca escribir contraseñas reales en archivos que se suban a GitHub.**

En **Windows PowerShell**, configurar la contraseña de forma interactiva, sin dejarla escrita en el historial:

```powershell
$secure = Read-Host "Contraseña de PostgreSQL" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
```

La variable de entorno aplica únicamente a esa sesión de PowerShell. Hay que configurarla de nuevo al abrir otra terminal.

### 5. Iniciar la aplicación

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir **http://localhost:8080** en un navegador. Mantener abierta la terminal mientras se utiliza la aplicación.

En Linux o macOS, usar `./mvnw spring-boot:run` tras configurar `DB_PASSWORD` en la sesión correspondiente.

## API REST

URL base: `http://localhost:8080`

### Servicios

| Método | Endpoint | Acción |
|---|---|---|
| `POST` | `/api/servicios` | Registrar servicio |
| `GET` | `/api/servicios` | Listar servicios |
| `GET` | `/api/servicios/activos` | Consultar servicios activos |
| `GET` | `/api/servicios/{id}` | Consultar por ID |
| `PUT` | `/api/servicios/{id}` | Actualizar servicio |
| `DELETE` | `/api/servicios/{id}` | Desactivar servicio |

### Citas e indicadores

| Método | Endpoint | Acción |
|---|---|---|
| `POST` | `/api/citas` | Registrar cita |
| `GET` | `/api/citas` | Listar citas |
| `GET` | `/api/citas/{id}` | Consultar cita |
| `GET` | `/api/citas/estado/{estado}` | Filtrar por estado |
| `GET` | `/api/citas/fecha/{fecha}` | Consultar agenda por fecha ISO |
| `PUT` | `/api/citas/{id}` | Modificar cita programada |
| `PATCH` | `/api/citas/{id}/estado/{estado}` | Cambiar estado |
| `PATCH` | `/api/citas/{id}/cancelar` | Cancelar cita |
| `GET` | `/api/citas/indicadores/servicios-mas-solicitados` | Consultar servicios más solicitados |
| `GET` | `/api/citas/indicadores/total-estimado/{fecha}` | Consultar total estimado del día |

Ejemplo de solicitud para registrar una cita:

```json
{
  "nombreMascota": "Luna",
  "propietario": "Cliente de ejemplo",
  "especie": "Perro",
  "fechaHora": "2027-02-01T09:00:00",
  "servicioId": 1
}
```

El `servicioId` debe corresponder a un servicio existente y activo.

### Reglas de validación y respuestas HTTP

- **400 Bad Request:** datos inválidos, por ejemplo precio negativo o duración igual a cero.
- **404 Not Found:** recurso inexistente.
- **409 Conflict:** horario ocupado, duplicados o transiciones de estado no permitidas, según la regla violada.
- Las citas `ATENDIDA` y `CANCELADA` se tratan como estados finales: no se pueden editar desde el flujo normal.
- Las citas canceladas permanecen en el historial y dejan libre su horario.

Ejemplo de respuesta obtenida al probar el registro de un servicio con precio `-50` y duración `0`:

```json
{
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Los datos enviados no son válidos",
  "errores": {
    "precio": "El precio debe ser mayor que cero",
    "duracionMinutos": "La duración debe ser mayor que cero"
  }
}
```

## Pruebas y resultados

### Pruebas automatizadas

Ejecutar desde la raíz del proyecto, con PostgreSQL disponible y `DB_PASSWORD` configurada:

```powershell
.\mvnw.cmd clean test
```

**Resultado real registrado el 5 de noviembre de 2026:**

```text
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Distribución: `CitaServiceTest` (8), `ServicioEsteticaServiceTest` (2), y prueba de arranque de aplicación (1). El arranque de Spring Boot se verificó con conexión a PostgreSQL 18.6.

### Pruebas funcionales manuales realizadas

| Escenario | Resultado observado |
|---|---|
| Registrar cita y recuperarla en la lista | Correcto: cita creada y visible |
| Editar cita sin cambiar su ID | Correcto: ID 13 pasó de 10:00 a 12:00 |
| Intentar reservar el mismo horario activo | Rechazado con aviso de horario ocupado |
| Cancelar cita | Correcto: cita ID 13 quedó `CANCELADA` |
| Reutilizar el horario liberado | Correcto: nueva cita ID 14 quedó `PROGRAMADA` |
| Marcar cita como atendida | Correcto: ID 14 cambió a `ATENDIDA` |
| Consultar agenda e indicadores | Resultados visibles en interfaz |
| Enviar precio negativo y duración cero a la API | `HTTP 400` con errores por campo |

Las verificaciones se documentaron con capturas reales de la interfaz, de PowerShell y de las respuestas HTTP. **Estas pruebas se realizaron localmente; no implican despliegue público.** La colección Postman se incluye como recurso de pruebas, pero no se afirma que toda ella haya sido ejecutada en esta última validación.

## Evidencias y archivos de entrega

- `Evidencias/Evidencias.docx`: documento Word con capturas y explicaciones de las pruebas.
- `database/peluqueria_mascotas.sql`: creación y restricciones de las tablas.
- `database/datos_prueba.sql`: registros ficticios para pruebas locales.
- `postman/`: colección de API REST para importar en Postman.
- `src/test/java/`: pruebas automatizadas.

## Consideraciones de seguridad y alcance

- La conexión a PostgreSQL debe realizarse con credenciales locales no publicadas.
- No incluir carpetas de compilación como `target/` en futuros commits; se regeneran al compilar.
- El sistema es un proyecto académico ejecutado y validado **localmente**, no un servicio desplegado en producción.
- Las capturas y resultados documentados corresponden a las pruebas realizadas, no a resultados simulados.

---

**Proyecto Final PF-18 · Peluquería de Mascotas · Noviembre de 2026**
