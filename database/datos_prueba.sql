-- DATOS FICTICIOS. Ejecutar después de peluqueria_mascotas.sql, en una BD de prueba.
-- Fechas futuras para permitir validación de @FutureOrPresent al editar.
INSERT INTO servicio_estetica(nombre, descripcion, precio, duracion_minutos, activo) VALUES
('Baño básico demo', 'Baño de mascota pequeña', 80, 45, true),
('Corte estético demo', 'Corte de pelaje', 120, 60, true),
('Limpieza de oídos demo', 'Limpieza preventiva', 45, 20, true),
('Corte de uñas demo', 'Cuidado de uñas', 35, 15, true)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO cita(nombre_mascota, propietario, especie, fecha_hora, estado, servicio_id)
SELECT d.mascota, d.propietario, d.especie, d.fecha_hora, 'PROGRAMADA', s.id
FROM (VALUES
('Luna','Cliente ficticio 01','Perro',TIMESTAMP '2027-01-10 08:00','Baño básico demo'),
('Max','Cliente ficticio 02','Perro',TIMESTAMP '2027-01-10 09:00','Corte estético demo'),
('Mía','Cliente ficticio 03','Gato',TIMESTAMP '2027-01-10 10:00','Limpieza de oídos demo'),
('Rocky','Cliente ficticio 04','Perro',TIMESTAMP '2027-01-10 11:00','Corte de uñas demo'),
('Nala','Cliente ficticio 05','Gato',TIMESTAMP '2027-01-11 08:00','Baño básico demo'),
('Toby','Cliente ficticio 06','Perro',TIMESTAMP '2027-01-11 09:00','Corte estético demo'),
('Kira','Cliente ficticio 07','Perro',TIMESTAMP '2027-01-11 10:00','Baño básico demo'),
('Coco','Cliente ficticio 08','Perro',TIMESTAMP '2027-01-11 11:00','Corte de uñas demo'),
('Simba','Cliente ficticio 09','Gato',TIMESTAMP '2027-01-12 09:00','Limpieza de oídos demo'),
('Bella','Cliente ficticio 10','Perro',TIMESTAMP '2027-01-12 10:00','Corte estético demo')
) AS d(mascota, propietario, especie, fecha_hora, servicio_nombre)
JOIN servicio_estetica s ON s.nombre = d.servicio_nombre
WHERE NOT EXISTS (SELECT 1 FROM cita c WHERE c.fecha_hora = d.fecha_hora AND c.estado <> 'CANCELADA');
