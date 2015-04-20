package com.darklunasea.dataengine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcEngine 
{
	private DriverManagerDataSource dataSource;
	private Connection conn;
	 
	public JdbcEngine() throws SQLException, IOException
	{
		String host = "";
		String port = "1521";
		String service = "";
		String user = "tester";
		String pw = "_test";
		
		dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		String url = String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, service);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pw);
		
		conn = dataSource.getConnection();
	}
	
	public void close() throws SQLException
	{
		if(conn != null)
		{
			conn.close();
		}
	}
    
    public ResultSet getByCusip(String cusip) throws SQLException 
    {
        String query = "SELECT * FROM BOND_ISSUE WHERE CUSIP = ?";    
                
        PreparedStatement ps = conn.prepareStatement(query);        
        ps.setString(1, cusip);        
        ResultSet rs = ps.executeQuery();                
        if(rs.next())
        {
            return rs;
        }
        else
        {
            System.out.println("No result found with cusip="+cusip);
            return null;
        }        
    }
}
