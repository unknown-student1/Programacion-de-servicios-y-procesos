-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-01-2019 a las 18:20:32
-- Versión del servidor: 10.1.28-MariaDB
-- Versión de PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `provincias`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `provincias`
--

CREATE TABLE `provincias` (
  `id` int(11) NOT NULL,
  `cod` char(2) NOT NULL DEFAULT '00' COMMENT 'Código de la provincia de dos digitos',
  `nombre` varchar(50) NOT NULL DEFAULT '' COMMENT 'Nombre de la provincia'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Provincias de españa; 99 para seleccionar a Nacional';

--
-- Volcado de datos para la tabla `provincias`
--

INSERT INTO `provincias` (`id`, `cod`, `nombre`) VALUES
(1, '01', 'Alava'),
(2, '02', 'Albacete'),
(3, '03', 'Alicante'),
(4, '04', 'Almera'),
(5, '05', 'Avila'),
(6, '06', 'Badajoz'),
(7, '07', 'Balears (Illes)'),
(8, '08', 'Barcelona'),
(9, '09', 'Burgos'),
(10, '10', 'Cáceres'),
(11, '11', 'Cádiz'),
(12, '12', 'Castellón'),
(13, '13', 'Ciudad Real'),
(14, '14', 'Córdoba'),
(15, '15', 'Coruña (A)'),
(16, '16', 'Cuenca'),
(17, '17', 'Girona'),
(18, '18', 'Granada'),
(19, '19', 'Guadalajara'),
(20, '20', 'Guipzcoa'),
(21, '21', 'Huelva'),
(22, '22', 'Huesca'),
(23, '23', 'Jaén'),
(24, '24', 'León'),
(25, '25', 'Lleida'),
(26, '26', 'Rioja (La)'),
(27, '27', 'Lugo'),
(28, '28', 'Madrid'),
(29, '29', 'Málaga'),
(30, '30', 'Murcia'),
(31, '31', 'Navarra'),
(32, '32', 'Ourense'),
(33, '33', 'Asturias'),
(34, '34', 'Palencia'),
(35, '35', 'Palmas (Las)'),
(36, '36', 'Pontevedra'),
(37, '37', 'Salamanca'),
(38, '38', 'Santa Cruz de Tenerife'),
(39, '39', 'Cantabria'),
(40, '40', 'Segovia'),
(41, '41', 'Sevilla'),
(42, '42', 'Soria'),
(43, '43', 'Tarragona'),
(44, '44', 'Teruel'),
(45, '45', 'Toledo'),
(46, '46', 'Valencia'),
(47, '47', 'Valladolid'),
(48, '48', 'Vizcaya'),
(49, '49', 'Zamora'),
(50, '50', 'Zaragoza'),
(51, '51', 'Ceuta'),
(52, '52', 'Melilla');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `provincias`
--
ALTER TABLE `provincias`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `provincias`
--
ALTER TABLE `provincias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
