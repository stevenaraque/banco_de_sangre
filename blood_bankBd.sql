CREATE DATABASE  IF NOT EXISTS `blood_bank_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `blood_bank_db`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: blood_bank_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blood_inventory`
--

DROP TABLE IF EXISTS `blood_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actualizado_en` datetime(6) DEFAULT NULL,
  `cantidad_ml` int NOT NULL,
  `tipo_sangre` enum('AB_NEGATIVO','AB_POSITIVO','A_NEGATIVO','A_POSITIVO','B_NEGATIVO','B_POSITIVO','O_NEGATIVO','O_POSITIVO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `unidades_disponibles` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnugvtt8st10bmgidelnla1g60` (`tipo_sangre`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_inventory`
--

LOCK TABLES `blood_inventory` WRITE;
/*!40000 ALTER TABLE `blood_inventory` DISABLE KEYS */;
INSERT INTO `blood_inventory` VALUES (1,NULL,1200,'O_POSITIVO',3),(2,NULL,800,'O_NEGATIVO',2),(3,NULL,1500,'A_POSITIVO',4),(4,NULL,600,'A_NEGATIVO',1),(5,NULL,900,'B_POSITIVO',2),(6,NULL,400,'B_NEGATIVO',1),(7,NULL,350,'AB_POSITIVO',1),(8,NULL,200,'AB_NEGATIVO',1);
/*!40000 ALTER TABLE `blood_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consents`
--

DROP TABLE IF EXISTS `consents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `acepta_consentimiento` bit(1) NOT NULL,
  `creado_en` datetime(6) DEFAULT NULL,
  `donante_id` bigint NOT NULL,
  `firma_consentimiento` text COLLATE utf8mb4_unicode_ci,
  `version_documento` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consents`
--

LOCK TABLES `consents` WRITE;
/*!40000 ALTER TABLE `consents` DISABLE KEYS */;
INSERT INTO `consents` VALUES (1,_binary '',NULL,2,'uploads/firmas/firma_2_20260520_143000.png','v1.0'),(2,_binary '',NULL,3,'uploads/firmas/firma_3_20260522_101500.jpg','v1.0');
/*!40000 ALTER TABLE `consents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donations`
--

DROP TABLE IF EXISTS `donations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_ml` int NOT NULL,
  `codigo_donacion` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `creado_en` datetime(6) DEFAULT NULL,
  `fecha_donacion` date NOT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `donante_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhys34vxlhg1ff9mqt3te7bare` (`codigo_donacion`),
  KEY `FKjhlhvtu11b7goe5wf3lqr19i0` (`donante_id`),
  CONSTRAINT `FKjhlhvtu11b7goe5wf3lqr19i0` FOREIGN KEY (`donante_id`) REFERENCES `donors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donations`
--

LOCK TABLES `donations` WRITE;
/*!40000 ALTER TABLE `donations` DISABLE KEYS */;
INSERT INTO `donations` VALUES (1,450,'DON-A1B2C3D4',NULL,'2026-05-20','Donación regular, donante en buen estado.',2),(2,500,'DON-E5F6G7H8',NULL,'2026-05-22','Primera donación del donante, sin novedades.',3),(3,350,'DON-I9J0K1L2',NULL,'2026-05-22','Donación programada, completada exitosamente.',3);
/*!40000 ALTER TABLE `donations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donors`
--

DROP TABLE IF EXISTS `donors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `acepta_consentimiento` bit(1) NOT NULL,
  `actualizado_en` datetime(6) DEFAULT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creado_en` datetime(6) DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `documento` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `fecha_ultima_donacion` date DEFAULT NULL,
  `firma_consentimiento` text COLLATE utf8mb4_unicode_ci,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `peso` double NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo_sangre` enum('AB_NEGATIVO','AB_POSITIVO','A_NEGATIVO','A_POSITIVO','B_NEGATIVO','B_POSITIVO','O_NEGATIVO','O_POSITIVO') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlcaj1skkgfj38wk2djek5ih4x` (`documento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donors`
--

LOCK TABLES `donors` WRITE;
/*!40000 ALTER TABLE `donors` DISABLE KEYS */;
INSERT INTO `donors` VALUES (1,_binary '',NULL,'Martínez López','carlos.martinez@email.com',NULL,'Calle 10 # 5-20','100200300','1988-02-14',NULL,NULL,'Carlos Alberto',72,'3001112233','O_POSITIVO'),(2,_binary '',NULL,'Gómez Herrera','laura.gomez@email.com',NULL,'Carrera 50 # 30-15','200300400','1995-07-22','2026-05-20','uploads/firmas/firma_2_20260520_143000.png','Laura Daniela',58,'3104445566','A_NEGATIVO'),(3,_binary '',NULL,'Rojas Pinzón','andres.rojas@email.com',NULL,'Avenida 68 # 15-90','300400500','1990-11-05','2026-05-22','uploads/firmas/firma_3_20260522_101500.jpg','Andrés Felipe',80,'3207778899','B_POSITIVO'),(4,_binary '',NULL,'Castro Beltrán','diana.castro@email.com',NULL,'Diagonal 25 # 8-45','400500600','1982-03-18',NULL,NULL,'Diana Carolina',65,'3159990011','AB_POSITIVO');
/*!40000 ALTER TABLE `donors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'blood_bank_db'
--

--
-- Dumping routines for database 'blood_bank_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-25  6:30:43
