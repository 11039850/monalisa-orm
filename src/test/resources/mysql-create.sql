CREATE TABLE `test_table_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `name` varchar(128) NOT NULL default 'N0001' COMMENT 'the name',
  `title` varchar(128) NULL  COMMENT 'the title',
  `enum_int_a` int(11) NOT NULL default 0 COMMENT 'enum fields A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) NOT NULL default 'TRUE' COMMENT '#enum{{ TRUE, FALSE}}',
  `ts_a` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(64) NULL,
  `update_time` datetime NULL,
  `update_by` varchar(64) NULL,
  PRIMARY KEY (`id`),
  KEY `ix_name_title`(`name`,`title`),
  UNIQUE KEY `ux_name_time`(`name`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `test_table_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `name` varchar(128) NOT NULL default 'N0001' COMMENT 'the name',
  `title` varchar(128) NULL  COMMENT 'the title',
  `enum_int_a` int(11) NOT NULL default 0 COMMENT 'enum fields A  #enum{{V0,V1}}',
  `enum_string_a` varchar(64) NOT NULL default 'TRUE' COMMENT '#enum{{ TRUE, FALSE}}',
  `array_int` varchar(256)  NULL COMMENT 'array of int. #array{int}',
  `array_string` varchar(256)  NULL COMMENT 'array of string. #array{}',
  `json` varchar(1024)  NULL COMMENT 'Json object. #json{}',
  `obj` varchar(1024)  NULL COMMENT 'Json object with given class.  #json{test.com.tsc9526.monalisa.orm.data.ColumnData}',
  `ts_a` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(64) NULL,
  `update_time` datetime NULL,
  `update_by` varchar(64) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `test_record` (
	`record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
	`name` varchar(128) NOT NULL default 'N0001' COMMENT 'the name',
	`title` varchar(128) NULL  COMMENT 'the title',
	`ts_a` datetime  NULL COMMENT 'date time field',
	`create_time` datetime NOT NULL,
	`create_by` varchar(64) NULL,
	`update_time` datetime NULL,
	`update_by` varchar(64) NULL,
	PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	

CREATE TABLE `test_logyyyymm_201601` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `log_time` datetime NOT NULL,
  `name` varchar(128)  NULL COMMENT 'the name',
  `enum_int_a` int(11)  NULL COMMENT 'enum field a.  #enum{{V0,V1}}',
  `enum_string_a` varchar(64)  NULL COMMENT 'enum field string. #enum{{ TRUE, FALSE}}',
  `create_time` datetime  NULL,
  `create_by` varchar(64)  NULL,
  `update_time` datetime  NULL,
  `update_by` varchar(64)  NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET=utf8;


INSERT INTO test_record(record_id,`name`,`title`,ts_a,create_time)VALUES(1,"hello","record",now(),now());