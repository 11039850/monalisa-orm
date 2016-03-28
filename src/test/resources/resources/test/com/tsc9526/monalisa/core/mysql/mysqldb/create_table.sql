/***CREATE TABLE: test_logyyyymm_ :: test_logyyyymm_201603***/
CREATE TABLE IF NOT EXISTS `#{table}`(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `log_time` datetime NOT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `enum_int_a` int(11) DEFAULT NULL COMMENT '枚举字段A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) DEFAULT NULL COMMENT '#enum{{ TRUE, FALSE}}',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8


