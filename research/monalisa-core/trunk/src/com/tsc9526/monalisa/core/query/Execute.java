package com.tsc9526.monalisa.core.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Execute<X>{
	public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException;
	public X execute(PreparedStatement pst)throws SQLException;
}
