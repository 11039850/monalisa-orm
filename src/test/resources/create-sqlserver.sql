 
CREATE TABLE dbo.test_table_1
	(
	id int NOT NULL IDENTITY (1, 1),
	name varchar(128) NOT NULL,
	title varchar(128) NULL,
	enum_int_a int NOT NULL,
	enum_string_a varchar(64) NULL,
	ts_a datetime NOT NULL,
	create_time datetime NOT NULL,
	create_by varchar(64) NULL,
	update_time datetime NULL,
	update_by varchar(64) NULL
	)  ON [PRIMARY]
 
