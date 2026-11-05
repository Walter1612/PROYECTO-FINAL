CREATE TABLE IF NOT EXISTS servicio_estetica (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    precio NUMERIC(10,2) NOT NULL CHECK (precio > 0),
    duracion_minutos INTEGER NOT NULL CHECK (duracion_minutos > 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS cita (
                                    id BIGSERIAL PRIMARY KEY,
                                    nombre_mascota VARCHAR(80) NOT NULL,
    propietario VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    estado VARCHAR(20) NOT NULL,
    servicio_id BIGINT NOT NULL,

    CONSTRAINT fk_cita_servicio
    FOREIGN KEY (servicio_id)
    REFERENCES servicio_estetica(id),

    CONSTRAINT chk_estado_cita
    CHECK (estado IN ('PROGRAMADA', 'ATENDIDA', 'CANCELADA'))
    );

CREATE INDEX IF NOT EXISTS idx_cita_fecha_hora
    ON cita(fecha_hora);

CREATE INDEX IF NOT EXISTS idx_cita_estado
    ON cita(estado);

CREATE INDEX IF NOT EXISTS idx_cita_servicio
    ON cita(servicio_id);
-- Evita que dos transacciones creen citas activas para el mismo instante.
-- Si hay duplicados históricos, depurarlos antes de crear este índice.
CREATE UNIQUE INDEX IF NOT EXISTS uq_cita_horario_activo
ON cita(fecha_hora) WHERE estado IN ('PROGRAMADA', 'ATENDIDA');
