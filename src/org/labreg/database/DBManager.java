package org.labreg.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Voqus
 */
public final class DBManager
{

	public static Connection connect()
	{
		try
		{
			// JDBC driver connection
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:" + new File("src/org/labreg/database/LabDatabase.sqlite"));
			return con;
		} catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
			return null;
		} // try
	} // connect
} // DBManager