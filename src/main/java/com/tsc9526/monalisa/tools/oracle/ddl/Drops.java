/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.oracle.ddl;

/**
 * @see <a href="https://stackoverflow.com/questions/1799128/oracle-if-table-exists">Oracle: If Table Exists</a>
 * @author zzg.zhou(11039850@qq.com)
 */
public class Drops {
	
	public static String dropTableIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n   EXECUTE IMMEDIATE 'DROP TABLE ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n   WHEN OTHERS THEN"
			+ "\r\n      IF SQLCODE != -942 THEN"
			+ "\r\n         RAISE;"
			+ "\r\n      END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropSequenceIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP SEQUENCE ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -2289 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropViewIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP VIEW ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -942 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropTriggerIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP TRIGGER ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -4080 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropIndexIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP INDEX ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -1418 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropColumnIfExists(String tableName,String columnName) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'ALTER TABLE ' || '" +(tableName)+ "' "
			+ "\r\n  						|| ' DROP COLUMN ' || '" +(columnName)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -904 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropDatabaseLinkIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP DATABASE LINK ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -2024 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropMaterializedViewIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP MATERIALIZED VIEW ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -12003 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropTypeIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP TYPE ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -4043 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	 
	public static String dropConstaintIfExists(String tableName , String constraintName) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'ALTER TABLE ' || '" +(tableName)+ "'"
			+ "\r\n    					|| ' DROP CONSTRAINT ' || '" +(constraintName)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -2443 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropScheduleIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  DBMS_SCHEDULER.drop_job('" +(name)+ "');"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -27475 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	public static String dropUserSchemaIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP USER ' || '" +(name)+ "';"
			+ "\r\n  -- you may or may not want to add CASCADE"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -1918 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropPackageIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP PACKAGE ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -4043 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropFunctionIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP FUNCTION ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -4043 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	 
	
	public static String dropProcedureIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP PROCEDURE ' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -4043 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
	
	
	public static String dropTablespaceIfExists(String name) {
		return ""+/**~!{*/""
			+ "BEGIN"
			+ "\r\n  EXECUTE IMMEDIATE 'DROP TABLESPACE' || '" +(name)+ "';"
			+ "\r\nEXCEPTION"
			+ "\r\n  WHEN OTHERS THEN"
			+ "\r\n    IF SQLCODE != -959 THEN"
			+ "\r\n      RAISE;"
			+ "\r\n    END IF;"
			+ "\r\nEND;"
		+ "\r\n"/**}*/;
	}
}
