CREATE TABLE `test_table_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `name` varchar(128) NOT NULL default 'N0001' COMMENT '名称',
  `title` varchar(128) NULL  COMMENT '标题',
  `enum_int_a` int(11) NOT NULL default 0 COMMENT '枚举字段A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) NOT NULL default 'TRUE' COMMENT '#enum{{ TRUE, FALSE}}',
  `ts_a` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(64) NULL,
  `update_time` datetime NULL,
  `update_by` varchar(64) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `test_table_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `name` varchar(128) NOT NULL default 'N0001' COMMENT '名称',
  `title` varchar(128) NULL  COMMENT '标题',
  `enum_int_a` int(11) NOT NULL default 0 COMMENT '枚举字段A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) NOT NULL default 'TRUE' COMMENT '#enum{{ TRUE, FALSE}}',
  `array_int` varchar(256)  NULL COMMENT '整形数组 #array{int}',
  `array_string` varchar(256)  NULL COMMENT '字符串数组 #array{}',
  `json` varchar(1024)  NULL COMMENT 'Json #json{}',
  `obj` varchar(1024)  NULL COMMENT 'Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}',
  `ts_a` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(64) NULL,
  `update_time` datetime NULL,
  `update_by` varchar(64) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `test_logyyyymm_201601` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `log_time` datetime NOT NULL,
  `name` varchar(128)  NULL COMMENT '名称',
  `enum_int_a` int(11)  NULL COMMENT '枚举字段A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64)  NULL COMMENT '#enum{{ TRUE, FALSE}}',
  `create_time` datetime  NULL,
  `create_by` varchar(64)  NULL,
  `update_time` datetime  NULL,
  `update_by` varchar(64)  NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;