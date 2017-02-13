/***CREATE TABLE: test_logyyyymm_ :: test_logyyyymm_201603***/
CREATE TABLE IF NOT EXISTS `#{table}`(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `log_time` datetime NOT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT 'the name',
  `enum_int_a` int(11) DEFAULT NULL COMMENT 'enum field a.  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) DEFAULT NULL COMMENT 'enum field string. #enum{{ TRUE, FALSE}}',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8


