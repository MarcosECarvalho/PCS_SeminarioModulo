-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.67-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema sca
--

CREATE DATABASE IF NOT EXISTS sca;
USE sca;

--
-- Definition of table `ac_categoria`
--

DROP TABLE IF EXISTS `ac_categoria`;
CREATE TABLE `ac_categoria` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ac_categoria`
--

/*!40000 ALTER TABLE `ac_categoria` DISABLE KEYS */;
INSERT INTO `ac_categoria` (`id`,`nome`) VALUES 
 (4,'ENSINO'),
 (1,'PESQUISA'),
 (2,'EXTENSÃO');
/*!40000 ALTER TABLE `ac_categoria` ENABLE KEYS */;


--
-- Definition of table `ac_item`
--

DROP TABLE IF EXISTS `ac_item`;
CREATE TABLE `ac_item` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(200) NOT NULL,
  `fk_ac_categoria` int(10) unsigned NOT NULL,
  `ativo` tinyint(3) unsigned NOT NULL,
  `carga_horaria_maxima` int(10) unsigned NOT NULL,
  `carga_horaria_minima` int(10) unsigned NOT NULL,
  `fk_ac_regulamento` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ac_item`
--

/*!40000 ALTER TABLE `ac_item` DISABLE KEYS */;
INSERT INTO `ac_item` (`id`,`nome`,`fk_ac_categoria`,`ativo`,`carga_horaria_maxima`,`carga_horaria_minima`,`fk_ac_regulamento`) VALUES 
 (11,'Disciplinas Não Previstas',0,1,120,0,1),
 (12,'Monitoria',0,1,100,0,1),
 (13,'Iniciação Científica',1,1,120,0,1),
 (14,'Publicações',1,1,120,0,1),
 (15,'Participação em Projetos de Pesquisa',1,1,100,0,1),
 (16,'Assistência a Monografias, Teses e Dissertações',1,1,40,0,1),
 (17,'Organização e/ou Colaboração em Eventos e Atividades Institucionais',2,1,80,0,1),
 (18,'Seminários, Conferências, Palestras, Oficinas e Visitas Técnicas',2,1,60,10,1),
 (19,'Participação em Projetos de Extensão',2,1,100,0,1),
 (20,'Presença em Bancas de Projeto Final de Curso',2,1,20,6,1);
/*!40000 ALTER TABLE `ac_item` ENABLE KEYS */;


--
-- Definition of table `ac_regencia`
--

DROP TABLE IF EXISTS `ac_regencia`;
CREATE TABLE `ac_regencia` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fk_sie_versao_curso` int(10) unsigned NOT NULL,
  `fk_ac_regulamento` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ac_regencia`
--

/*!40000 ALTER TABLE `ac_regencia` DISABLE KEYS */;
INSERT INTO `ac_regencia` (`id`,`fk_sie_versao_curso`,`fk_ac_regulamento`) VALUES 
 (24,47721219,1),
 (23,47721281,1);
/*!40000 ALTER TABLE `ac_regencia` ENABLE KEYS */;


--
-- Definition of table `ac_regulamento`
--

DROP TABLE IF EXISTS `ac_regulamento`;
CREATE TABLE `ac_regulamento` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(45) NOT NULL,
  `horas` int(10) unsigned NOT NULL,
  `min_pesquisa` int(10) unsigned NOT NULL,
  `max_pesquisa` int(10) unsigned NOT NULL,
  `min_ensino` int(10) unsigned NOT NULL,
  `max_ensino` int(10) unsigned NOT NULL,
  `min_extensao` int(10) unsigned NOT NULL,
  `max_extensao` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ac_regulamento`
--

/*!40000 ALTER TABLE `ac_regulamento` DISABLE KEYS */;
INSERT INTO `ac_regulamento` (`id`,`nome`,`horas`,`min_pesquisa`,`max_pesquisa`,`min_ensino`,`max_ensino`,`min_extensao`,`max_extensao`) VALUES 
 (1,'Regulamento BCC',230,10,150,0,150,20,150);
/*!40000 ALTER TABLE `ac_regulamento` ENABLE KEYS */;


--
-- Definition of table `admin_coordenacao`
--

DROP TABLE IF EXISTS `admin_coordenacao`;
CREATE TABLE `admin_coordenacao` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fk_xls_prof` int(10) NOT NULL,
  `fk_xls_curso` int(10) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `index_unique_prof_curso` (`fk_xls_prof`,`fk_xls_curso`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin_coordenacao`
--

/*!40000 ALTER TABLE `admin_coordenacao` DISABLE KEYS */;
INSERT INTO `admin_coordenacao` (`id`,`fk_xls_prof`,`fk_xls_curso`) VALUES 
 (7,1604711,65601),
 (16,1445829,85843),
 (11,1604711,85843);
/*!40000 ALTER TABLE `admin_coordenacao` ENABLE KEYS */;


--
-- Definition of table `admin_coordenacao_ac`
--

DROP TABLE IF EXISTS `admin_coordenacao_ac`;
CREATE TABLE `admin_coordenacao_ac` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fk_xls_prof` int(10) NOT NULL,
  `fk_xls_curso` int(10) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `index_unique_prof_curso` (`fk_xls_prof`,`fk_xls_curso`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin_coordenacao_ac`
--

/*!40000 ALTER TABLE `admin_coordenacao_ac` DISABLE KEYS */;
INSERT INTO `admin_coordenacao_ac` (`id`,`fk_xls_prof`,`fk_xls_curso`) VALUES 
 (2,1604711,65601),
 (6,1604711,85843);
/*!40000 ALTER TABLE `admin_coordenacao_ac` ENABLE KEYS */;


--
-- Definition of table `eval_avaliacao`
--

DROP TABLE IF EXISTS `eval_avaliacao`;
CREATE TABLE `eval_avaliacao` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8657568 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_avaliacao`
--

/*!40000 ALTER TABLE `eval_avaliacao` DISABLE KEYS */;
INSERT INTO `eval_avaliacao` (`id`,`nome`) VALUES 
 (2,'REGULAR'),
 (0,'PESSIMO'),
 (3,'BOM'),
 (4,'OTIMO'),
 (1,'RUIM');
/*!40000 ALTER TABLE `eval_avaliacao` ENABLE KEYS */;


--
-- Definition of table `eval_categoria`
--

DROP TABLE IF EXISTS `eval_categoria`;
CREATE TABLE `eval_categoria` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_categoria`
--

/*!40000 ALTER TABLE `eval_categoria` DISABLE KEYS */;
INSERT INTO `eval_categoria` (`id`,`nome`) VALUES 
 (4,'INFRAESTRUTURA'),
 (1,'DISCIPLINA'),
 (2,'DOCENCIA');
/*!40000 ALTER TABLE `eval_categoria` ENABLE KEYS */;


--
-- Definition of table `eval_questao`
--

DROP TABLE IF EXISTS `eval_questao`;
CREATE TABLE `eval_questao` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `enunciado` varchar(800) NOT NULL,
  `fk_questao_categoria` int(10) unsigned NOT NULL,
  `ordem` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=106 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_questao`
--

/*!40000 ALTER TABLE `eval_questao` DISABLE KEYS */;
INSERT INTO `eval_questao` (`id`,`enunciado`,`fk_questao_categoria`,`ordem`) VALUES 
 (97,'De forma geral, a apresentação do programa e dos objetivos dessa disciplina ocorreu de maneira...',1,4),
 (98,'De forma geral, a atualização da bibliografia utilizada e a adequação aos tópicos do programa dessa disciplina ocorreu de maneira...',1,1),
 (99,' De forma geral, o esclarecimento prévio sobre os critérios utilizados para a avaliação ocorreu de maneira...',1,2),
 (100,' De forma geral, o cumprimento do conteúdo programático ocorreu de maneira...',1,3),
 (105,'As práticas pedagógicas promovem a contextualização. De forma geral, a relação da teoria com a prática nessa disciplina ocorreu de maneira...',1,8),
 (102,'De forma geral, o planejamento/organização das aulas pelo professor ocorreu de maneira...',1,5),
 (103,'De forma geral, a assiduidade do professor ocorreu de forma...',1,6),
 (104,' De forma geral, a pontualidade do professor pode ser avaliada como...',1,7);
/*!40000 ALTER TABLE `eval_questao` ENABLE KEYS */;


--
-- Definition of table `eval_questao_categoria`
--

DROP TABLE IF EXISTS `eval_questao_categoria`;
CREATE TABLE `eval_questao_categoria` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_questao_categoria`
--

/*!40000 ALTER TABLE `eval_questao_categoria` DISABLE KEYS */;
INSERT INTO `eval_questao_categoria` (`id`,`nome`) VALUES 
 (4,'INFRAESTRUTURA'),
 (1,'DISCIPLINA'),
 (2,'DOCENCIA');
/*!40000 ALTER TABLE `eval_questao_categoria` ENABLE KEYS */;


--
-- Definition of table `eval_questionario`
--

DROP TABLE IF EXISTS `eval_questionario`;
CREATE TABLE `eval_questionario` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `data` date NOT NULL,
  `fk_sie_aluno` int(11) NOT NULL,
  `fk_sie_turma` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_questionario`
--

/*!40000 ALTER TABLE `eval_questionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `eval_questionario` ENABLE KEYS */;


--
-- Definition of table `eval_resposta`
--

DROP TABLE IF EXISTS `eval_resposta`;
CREATE TABLE `eval_resposta` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fk_eval_avaliacao` int(10) unsigned NOT NULL,
  `fk_eval_questao` int(10) unsigned NOT NULL,
  `fk_eval_questionario` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eval_resposta`
--

/*!40000 ALTER TABLE `eval_resposta` DISABLE KEYS */;
/*!40000 ALTER TABLE `eval_resposta` ENABLE KEYS */;


--
-- Definition of table `mtr_turma_monitoria`
--

DROP TABLE IF EXISTS `mtr_turma_monitoria`;
CREATE TABLE `mtr_turma_monitoria` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `local` varchar(45) NOT NULL,
  `disciplina` varchar(100) NOT NULL,
  `dia_da_semana` int(10) unsigned NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fim` time NOT NULL,
  `fk_sie_aluno` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mtr_turma_monitoria`
--

/*!40000 ALTER TABLE `mtr_turma_monitoria` DISABLE KEYS */;
INSERT INTO `mtr_turma_monitoria` (`id`,`local`,`disciplina`,`dia_da_semana`,`hora_inicio`,`hora_fim`,`fk_sie_aluno`) VALUES 
 (1,'LAB03','Algoritmos',1,'10:00:00','12:00:00',1422871948),
 (2,'LAB02','EDA',1,'15:00:00','18:30:00',1422633620);
/*!40000 ALTER TABLE `mtr_turma_monitoria` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
