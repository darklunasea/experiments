package com.nxiao.service.dataengine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.nxiao.service.configuration.ConnectionConfig;

public class JdbcEngine 
{
	public enum DB_SCHEMA
	{
		SEC_MASTER,
		SEC_AQADM
	}
	
	protected DriverManagerDataSource dataSource;
	protected Connection conn;	
	 
	public JdbcEngine(DB_SCHEMA schema) throws Exception
	{
		dataSource = getDataSource(schema);		
		conn = dataSource.getConnection();
	}

	protected DriverManagerDataSource getDataSource(DB_SCHEMA schema) throws IOException 
	{
		//Override this method if your datasource is different
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		String host = ConnectionConfig.getAsString("OracleDb_Host");
		String port = ConnectionConfig.getAsString("OracleDb_Port");
		String service = ConnectionConfig.getAsString("OracleDb_Service");
		String user = ConnectionConfig.getAsString("OracleDb_Schema_" + schema.toString());
		String pw = ConnectionConfig.getAsString("OracleDb_Password_" + schema.toString());
		
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		String url = String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, service);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pw);
		
		return dataSource;
	}
	
	public void close() throws SQLException
	{
		if(conn != null)
		{
			conn.close();
		}
	}
	
	public Connection getConnection()
	{
		return conn;
	}
}
