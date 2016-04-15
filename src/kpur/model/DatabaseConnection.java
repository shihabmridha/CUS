package kpur.model;

import java.sql.*;

public class DatabaseConnection
{
	private Connection c = null;
	private Statement query = null;

	public DatabaseConnection()
	{
	    try
	    {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:main.db");
	    }
	    catch ( Exception e )
	    {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}

	public void puts(String sql)
	{
		try
		{
	      query = c.createStatement();
	      query.executeUpdate(sql);
	    }
		catch ( Exception e )
		{
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		System.out.println("Query OK!");
	}

	public boolean hasTable()
	{
		DatabaseMetaData meta;
		try
		{
			meta = c.getMetaData();
			ResultSet res = meta.getTables(null, null, "shareholder", new String[] {"TABLE"});
			if (res.next())
			{
				return true;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public void createTable()
	{
		puts("create table shareholder (date datetime default current_timestamp,"
				+ "user_id INTEGER PRIMARY KEY autoincrement,"
				+ "name text not null,"
				+ "guardian text not null,"
				+ "mobile text not null,"
				+ "village text not null,"
				+ "ps text not null,"
				+ "nomini text not null,"
				+ "relation text not null);");

		puts("create table shareholder_data (trans_id INTEGER PRIMARY KEY autoincrement,"
				+ "date text not null,"
				+ "user_id INTEGER not null,"
				+ "deposit INTEGER DEFAULT 0,"
				+ "profit INTEGER DEFAULT 0,"
				+ "withdraw INTEGER DEFAULT 0,"
				+ "balance INTEGER DEFAULT 0);");

		puts("create table centers(center_id integer primary key autoincrement,"
				+ "center_name text not null,"
				+ "fs text not null)");

		puts("create table weekly_user(user_id integer primary key autoincrement,"
				+ "center_id int,"
				+ "serial_no int not null,"
				+ "name text not null,"
				+ "husband text not null,"
				+ "bf_balance double not null,"
				+ "investment_amount double not null);");

		puts("create table weekly_saving(trans_id integer primary key autoincrement,"
				+ "user_id int not null,"
				+ "date text not null,"
				+ "bf_balance double not null,"
				+ "weekly_deposit double not null,"
				+ "rebet double not null,"
				+ "monthly_collection double not null,"
				+ "saving_return double not null,"
				+ "total_balance double not null);");

		puts("create table weekly_invest(trans_id integer primary key autoincrement,"
				+ "user_id int not null,"
				+ "date text not null,"
				+ "investment_amount double not null,"
				+ "instolment_amount double not null,"
				+ "project_no int not null,"
				+ "od_last_month double not null,"
				+ "investment_cbm double not null,"
				+ "last_month_outstanding double not null,"
				+ "disbuds_current_month double not null,"
				+ "total_balance_inv double not null,"
				+ "weekly_instolment double not null,"
				+ "total_collection double not null,"
				+ "total_outstanding double not null);");
	}

	public Connection connect()
	{
		return c;
	}
	public Statement query()
	{
		return query;
	}
	public void setQuery(Statement query)
	{
		this.query = query;
	}
	public Statement getQuery()
	{
		return query;
	}
}