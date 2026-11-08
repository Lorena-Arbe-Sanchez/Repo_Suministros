-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3307
-- Tiempo de generación: 08-11-2026 a las 15:47:34
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ad_suministros`
--
CREATE DATABASE IF NOT EXISTS `ad_suministros` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `ad_suministros`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pieza`
--

CREATE TABLE `pieza` (
  `cod` varchar(255) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `precio` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pieza`
--

INSERT INTO `pieza` (`cod`, `descripcion`, `nombre`, `precio`) VALUES
('P001', 'Tornillo de acero inoxidable de 5 cm', 'Tornillo M5', 0.15),
('P002', 'Tuerca hexagonal de acero galvanizado', 'Tuerca M5', 0.08),
('P003', 'Arandela plana de acero', 'Arandela M5', 0.05),
('P004', 'Barra de acero de 2 metros', 'Barra de acero', 12.5),
('P005', 'Plancha de aluminio de 1 metro cuadrado', 'Plancha aluminio', 35),
('P006', 'Cable eléctrico de cobre de 10 metros', 'Cable eléctrico', 8.75),
('P007', 'Motor eléctrico de 500 W', 'Motor eléctrico', 95),
('P008', 'Rodamiento industrial de alta resistencia', 'Rodamiento', 14.9),
('P009', 'Tubo de PVC de 3 metros', 'Tubo PVC', 6.25),
('P010', 'Pintura anticorrosiva para metal, bote de 5 litros', 'Pintura anticorrosiva', 28.5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `cod` varchar(255) NOT NULL,
  `apellido1` varchar(255) NOT NULL,
  `apellido2` varchar(255) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`cod`, `apellido1`, `apellido2`, `direccion`, `nombre`) VALUES
('PR001', 'García', 'López', 'Calle Mayor 12, Vitoria-Gasteiz', 'Comercial García'),
('PR002', 'Martínez', 'Sánchez', 'Avenida Gasteiz 25, Vitoria-Gasteiz', 'Suministros Martínez'),
('PR003', 'Fernández', 'Ruiz', 'Calle Dato 8, Vitoria-Gasteiz', 'Materiales Fernández'),
('PR004', 'Pérez', 'Gómez', 'Polígono Industrial 4, Bilbao', 'Distribuciones Pérez'),
('PR005', 'López', 'Díaz', 'Calle Navarra 15, Donostia', 'Componentes López');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyecto`
--

CREATE TABLE `proyecto` (
  `cod` varchar(255) NOT NULL,
  `ciudad` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `proyecto`
--

INSERT INTO `proyecto` (`cod`, `ciudad`, `nombre`) VALUES
('PRY001', 'Vitoria-Gasteiz', 'Construcción de nave industrial'),
('PRY002', 'Bilbao', 'Reforma de instalaciones eléctricas'),
('PRY003', 'Donostia', 'Ampliación de almacén'),
('PRY004', 'Pamplona', 'Fabricación de maquinaria'),
('PRY005', 'Burgos', 'Rehabilitación de edificio'),
('PRY006', 'Logroño', 'Instalación de sistema de climatización');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `suministro`
--

CREATE TABLE `suministro` (
  `id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `pieza_cod` varchar(255) NOT NULL,
  `proveedor_cod` varchar(255) NOT NULL,
  `proyecto_cod` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `suministro`
--

INSERT INTO `suministro` (`id`, `cantidad`, `fecha`, `pieza_cod`, `proveedor_cod`, `proyecto_cod`) VALUES
(1, 500, '2026-01-10', 'P001', 'PR001', 'PRY001'),
(2, 500, '2026-01-10', 'P002', 'PR001', 'PRY001'),
(3, 1000, '2026-01-12', 'P003', 'PR002', 'PRY001'),
(4, 20, '2026-01-15', 'P004', 'PR003', 'PRY002'),
(5, 10, '2026-01-18', 'P005', 'PR003', 'PRY002'),
(6, 100, '2026-01-20', 'P006', 'PR002', 'PRY002'),
(7, 5, '2026-02-01', 'P007', 'PR004', 'PRY004'),
(8, 12, '2026-02-03', 'P008', 'PR004', 'PRY004'),
(9, 30, '2026-02-05', 'P009', 'PR005', 'PRY003'),
(10, 8, '2026-02-08', 'P010', 'PR005', 'PRY003'),
(11, 300, '2026-02-12', 'P001', 'PR002', 'PRY005'),
(12, 300, '2026-02-12', 'P002', 'PR002', 'PRY005'),
(13, 15, '2026-02-15', 'P009', 'PR001', 'PRY006'),
(14, 3, '2026-02-18', 'P007', 'PR004', 'PRY006'),
(15, 50, '2026-02-20', 'P008', 'PR003', 'PRY004');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `pieza`
--
ALTER TABLE `pieza`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `proyecto`
--
ALTER TABLE `proyecto`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `suministro`
--
ALTER TABLE `suministro`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKs3iooa5ifvlxtc0d9u8nhfwvf` (`pieza_cod`),
  ADD KEY `FKm8d7b043fwcullsrj75tn2mig` (`proveedor_cod`),
  ADD KEY `FKdsti8dkydpbdi3evcrfwxx4ji` (`proyecto_cod`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `suministro`
--
ALTER TABLE `suministro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `suministro`
--
ALTER TABLE `suministro`
  ADD CONSTRAINT `FKdsti8dkydpbdi3evcrfwxx4ji` FOREIGN KEY (`proyecto_cod`) REFERENCES `proyecto` (`cod`),
  ADD CONSTRAINT `FKm8d7b043fwcullsrj75tn2mig` FOREIGN KEY (`proveedor_cod`) REFERENCES `proveedor` (`cod`),
  ADD CONSTRAINT `FKs3iooa5ifvlxtc0d9u8nhfwvf` FOREIGN KEY (`pieza_cod`) REFERENCES `pieza` (`cod`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
