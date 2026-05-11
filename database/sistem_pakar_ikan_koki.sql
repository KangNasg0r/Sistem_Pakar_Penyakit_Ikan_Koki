-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 11, 2026 at 06:19 PM
-- Server version: 8.0.30
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistem_pakar_ikan_koki`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `nama_lengkap` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `password`, `nama_lengkap`) VALUES
('A01', '123', 'Administrator'),
('A02', '123', 'Ali');

-- --------------------------------------------------------

--
-- Table structure for table `diagnosa`
--

CREATE TABLE `diagnosa` (
  `id_diagnosa` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `nama_ikan` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_diagnosa` date NOT NULL,
  `kode_penyakit` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hasil_diagnosa` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `solusi` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diagnosa`
--

INSERT INTO `diagnosa` (`id_diagnosa`, `nama_ikan`, `tanggal_diagnosa`, `kode_penyakit`, `hasil_diagnosa`, `solusi`) VALUES
('D001', 'Juna', '2026-05-10', 'P01', 'White Spot', 'Pisahkan ikan yang sakit, naikkan suhu air secara bertahap, dan gunakan obat antiparasit atau garam ikan.'),
('D002', 'Rony', '2026-05-10', 'P02', 'Jamur / Saprolegniasis', 'Gunakan methylene blue atau obat antijamur serta lakukan pergantian air secara rutin.'),
('D003', 'Swan', '2026-05-11', 'P11', 'Lernaea / Cacing Jangkar', 'Cabut cacing secara hati-hati dan gunakan antiseptik serta obat antiparasit.');

-- --------------------------------------------------------

--
-- Table structure for table `diagnosa_detail`
--

CREATE TABLE `diagnosa_detail` (
  `id_detail_diagnosa` int NOT NULL,
  `id_diagnosa` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `kode_gejala` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diagnosa_detail`
--

INSERT INTO `diagnosa_detail` (`id_detail_diagnosa`, `id_diagnosa`, `kode_gejala`) VALUES
(32, 'D001', 'G01'),
(33, 'D001', 'G02'),
(34, 'D001', 'G03'),
(35, 'D002', 'G03'),
(36, 'D002', 'G04'),
(37, 'D002', 'G05'),
(38, 'D002', 'G09'),
(42, 'D003', 'G08'),
(43, 'D003', 'G09'),
(44, 'D003', 'G11'),
(45, 'D003', 'G13'),
(46, 'D003', 'G14');

-- --------------------------------------------------------

--
-- Table structure for table `gejala`
--

CREATE TABLE `gejala` (
  `kode_gejala` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `nama_gejala` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gejala`
--

INSERT INTO `gejala` (`kode_gejala`, `nama_gejala`) VALUES
('G01', 'Bintik putih pada tubuh'),
('G02', 'Ikan menggosok tubuh'),
('G03', 'Nafsu makan menurun'),
('G04', 'Sirip rusak / robek'),
('G05', 'Tubuh berjamur seperti kapas'),
('G06', 'Perut membengkak'),
('G07', 'Sisik mengembang'),
('G08', 'Luka merah pada tubuh'),
('G09', 'Ikan lemas'),
('G10', 'Insang pucat'),
('G11', 'Ikan sering ke permukaan'),
('G12', 'Mata menonjol'),
('G13', 'Tubuh berlendir berlebihan'),
('G14', 'Ada parasit menempel'),
('G15', 'Gerakan tidak seimbang'),
('G16', 'Warna tubuh kusam'),
('G17', 'Sirip menutup'),
('G18', 'Bercak kuning/emas'),
('G19', 'Pendarahan pada tubuh'),
('G20', 'Produksi lendir meningkat');

-- --------------------------------------------------------

--
-- Table structure for table `hasil_kemungkinan_diagnosa`
--

CREATE TABLE `hasil_kemungkinan_diagnosa` (
  `id_kemungkinan` int NOT NULL,
  `id_diagnosa` varchar(11) NOT NULL,
  `kode_penyakit` varchar(11) NOT NULL,
  `nama_penyakit` varchar(255) NOT NULL,
  `solusi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `hasil_kemungkinan_diagnosa`
--

INSERT INTO `hasil_kemungkinan_diagnosa` (`id_kemungkinan`, `id_diagnosa`, `kode_penyakit`, `nama_penyakit`, `solusi`) VALUES
(10, 'D001', 'P01', 'White Spot', 'Pisahkan ikan yang sakit, naikkan suhu air secara bertahap, dan gunakan obat antiparasit atau garam ikan.'),
(11, 'D002', 'P02', 'Jamur / Saprolegniasis', 'Gunakan methylene blue atau obat antijamur serta lakukan pergantian air secara rutin.'),
(13, 'D003', 'P11', 'Lernaea / Cacing Jangkar', 'Cabut cacing secara hati-hati dan gunakan antiseptik serta obat antiparasit.');

-- --------------------------------------------------------

--
-- Table structure for table `penyakit`
--

CREATE TABLE `penyakit` (
  `kode_penyakit` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `nama_penyakit` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `deskripsi` text COLLATE utf8mb4_general_ci NOT NULL,
  `solusi` text COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penyakit`
--

INSERT INTO `penyakit` (`kode_penyakit`, `nama_penyakit`, `deskripsi`, `solusi`) VALUES
('P01', 'White Spot', 'Penyakit yang disebabkan oleh parasit Ichthyophthirius multifiliis yang ditandai munculnya bintik-bintik putih pada tubuh dan sirip ikan.', 'Pisahkan ikan yang sakit, naikkan suhu air secara bertahap, dan gunakan obat antiparasit atau garam ikan.'),
('P02', 'Jamur / Saprolegniasis', 'Infeksi jamur pada tubuh ikan yang biasanya muncul seperti kapas putih akibat kualitas air buruk atau luka pada tubuh ikan.', 'Gunakan methylene blue atau obat antijamur serta lakukan pergantian air secara rutin.'),
('P03', 'Busuk Sirip / Fin Rot', 'Penyakit akibat infeksi bakteri yang menyebabkan sirip ikan rusak, robek, dan membusuk.', 'Bersihkan akuarium, gunakan antibiotik khusus ikan, dan lakukan karantina.'),
('P04', 'Dropsy', 'Penyakit yang menyebabkan perut ikan membengkak dan sisik terlihat mengembang akibat infeksi bakteri atau gangguan organ dalam.', 'Kurangi pemberian pakan, gunakan antibiotik, dan lakukan perawatan intensif pada ikan.'),
('P05', 'Kutu Ikan / Argulus', 'Penyakit akibat serangan parasit berbentuk kutu yang menempel pada tubuh ikan dan menghisap darah ikan.', 'Lepaskan kutu secara perlahan dan gunakan obat antiparasit.'),
('P06', 'Cacing Insang / Dactylogyrus', 'Penyakit parasit yang menyerang bagian insang sehingga ikan kesulitan bernapas.', 'Tingkatkan aerasi air dan gunakan obat antiparasit khusus cacing insang.'),
('P07', 'Cacing Kulit / Gyrodactylus', 'Penyakit akibat parasit yang menyerang kulit ikan sehingga tubuh menghasilkan lendir berlebih.', 'Rendam ikan dengan garam ikan dan jaga kualitas air tetap bersih.'),
('P08', 'Velvet / Karat', 'Penyakit parasit yang menyebabkan tubuh ikan terlihat seperti berdebu emas atau kecokelatan.', 'Kurangi pencahayaan dan gunakan obat antiparasit seperti copper sulfate.'),
('P09', 'Aeromonas', 'Penyakit bakteri yang menyebabkan luka merah, pendarahan, dan tubuh ikan menjadi lemas.', 'Gunakan antibiotik dan lakukan pergantian air secara rutin.'),
('P10', 'Columnaris', 'Penyakit bakteri yang menyerang kulit dan sirip ikan sehingga tubuh tampak kusam dan berlendir.', 'Lakukan karantina dan gunakan antibiotik serta perbaiki kualitas air.'),
('P11', 'Lernaea / Cacing Jangkar', 'Penyakit akibat parasit berbentuk cacing yang menempel pada tubuh ikan dan menyebabkan luka.', 'Cabut cacing secara hati-hati dan gunakan antiseptik serta obat antiparasit.'),
('P12', 'Swim Bladder Disorder', 'Gangguan pada kantung renang ikan yang menyebabkan ikan sulit berenang seimbang.', 'Kurangi pemberian pakan dan beri makanan lunak seperti kacang polong rebus.'),
('P13', 'Popeye', 'Penyakit yang menyebabkan mata ikan menonjol akibat infeksi bakteri atau kualitas air buruk.', 'Bersihkan air akuarium dan gunakan antibiotik sesuai dosis.'),
('P14', 'Stress / Kualitas Air Buruk', 'Kondisi stres akibat perubahan suhu, pH, atau kualitas air yang buruk sehingga ikan menjadi lemah.', 'Stabilkan kualitas air dan lakukan pergantian air secara berkala.'),
('P15', 'Trichodiniasis', 'Penyakit akibat parasit Trichodina yang menyebabkan tubuh ikan berlendir dan lemas.', 'Gunakan garam ikan atau formalin sesuai dosis dan bersihkan akuarium secara rutin.');

-- --------------------------------------------------------

--
-- Table structure for table `rule`
--

CREATE TABLE `rule` (
  `kode_rule` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `kode_penyakit` varchar(11) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rule`
--

INSERT INTO `rule` (`kode_rule`, `kode_penyakit`) VALUES
('R01', 'P01'),
('R02', 'P02'),
('R03', 'P03'),
('R04', 'P04'),
('R05', 'P05'),
('R06', 'P06'),
('R07', 'P07'),
('R08', 'P08'),
('R09', 'P09'),
('R10', 'P10'),
('R11', 'P11'),
('R12', 'P12'),
('R13', 'P13'),
('R14', 'P14'),
('R15', 'P15');

-- --------------------------------------------------------

--
-- Table structure for table `rule_detail`
--

CREATE TABLE `rule_detail` (
  `id_detail` int NOT NULL,
  `kode_rule` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `kode_gejala` varchar(11) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rule_detail`
--

INSERT INTO `rule_detail` (`id_detail`, `kode_rule`, `kode_gejala`) VALUES
(10, 'R01', 'G01'),
(11, 'R01', 'G02'),
(12, 'R01', 'G03'),
(13, 'R02', 'G05'),
(14, 'R02', 'G03'),
(15, 'R02', 'G09'),
(16, 'R03', 'G04'),
(17, 'R03', 'G08'),
(18, 'R03', 'G03'),
(19, 'R04', 'G06'),
(20, 'R04', 'G07'),
(21, 'R04', 'G09'),
(22, 'R05', 'G14'),
(23, 'R05', 'G02'),
(24, 'R05', 'G09'),
(25, 'R06', 'G10'),
(26, 'R06', 'G11'),
(27, 'R06', 'G03'),
(28, 'R07', 'G02'),
(29, 'R07', 'G13'),
(30, 'R07', 'G09'),
(31, 'R08', 'G18'),
(32, 'R08', 'G16'),
(33, 'R08', 'G02'),
(34, 'R09', 'G08'),
(35, 'R09', 'G19'),
(36, 'R09', 'G09'),
(37, 'R10', 'G04'),
(38, 'R10', 'G16'),
(39, 'R10', 'G13'),
(40, 'R11', 'G14'),
(41, 'R11', 'G08'),
(42, 'R11', 'G09'),
(43, 'R12', 'G15'),
(44, 'R12', 'G11'),
(45, 'R12', 'G03'),
(46, 'R13', 'G12'),
(47, 'R13', 'G08'),
(48, 'R13', 'G09'),
(49, 'R14', 'G16'),
(50, 'R14', 'G17'),
(51, 'R14', 'G03'),
(52, 'R15', 'G13'),
(53, 'R15', 'G20'),
(54, 'R15', 'G09');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `diagnosa`
--
ALTER TABLE `diagnosa`
  ADD PRIMARY KEY (`id_diagnosa`);

--
-- Indexes for table `diagnosa_detail`
--
ALTER TABLE `diagnosa_detail`
  ADD PRIMARY KEY (`id_detail_diagnosa`),
  ADD KEY `id_gejala` (`kode_gejala`),
  ADD KEY `id_diagnosa` (`id_diagnosa`);

--
-- Indexes for table `gejala`
--
ALTER TABLE `gejala`
  ADD PRIMARY KEY (`kode_gejala`);

--
-- Indexes for table `hasil_kemungkinan_diagnosa`
--
ALTER TABLE `hasil_kemungkinan_diagnosa`
  ADD PRIMARY KEY (`id_kemungkinan`);

--
-- Indexes for table `penyakit`
--
ALTER TABLE `penyakit`
  ADD PRIMARY KEY (`kode_penyakit`);

--
-- Indexes for table `rule`
--
ALTER TABLE `rule`
  ADD PRIMARY KEY (`kode_rule`),
  ADD KEY `kode_penyakit` (`kode_penyakit`);

--
-- Indexes for table `rule_detail`
--
ALTER TABLE `rule_detail`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `kode_rule` (`kode_rule`),
  ADD KEY `kode_gejala` (`kode_gejala`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `diagnosa_detail`
--
ALTER TABLE `diagnosa_detail`
  MODIFY `id_detail_diagnosa` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `hasil_kemungkinan_diagnosa`
--
ALTER TABLE `hasil_kemungkinan_diagnosa`
  MODIFY `id_kemungkinan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `rule_detail`
--
ALTER TABLE `rule_detail`
  MODIFY `id_detail` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
