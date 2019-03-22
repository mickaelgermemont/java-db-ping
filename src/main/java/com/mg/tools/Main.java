package com.mg.tools;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.ServiceLoader;

public class Main {

	public static void main(String[] args) {

		if(args.length != 3) {
			System.err.println("need 3 arguments: url user password\n\t\"jdbc:postgresql://localhost:5432/dbName\" \"username\" \"password\"");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String pwd = args[2];
		
		System.out.print("parameters");
		System.out.print("\t");
		System.out.print("url=");
		System.out.print(url);
		System.out.print(";");
		System.out.print("user=");
		System.out.print(user);
		System.out.print(";");
		System.out.print("password=");
		System.out.print(pwd);
		System.out.println();

		// Ensure loading JDBC drivers
		System.out.println("ServiceLoader list:" );
		ServiceLoader.load(Driver.class).forEach(d -> {
			System.out.print("\t");
			System.out.print(d);
			System.out.println();
		});

		//String url = "jdbc:postgresql://localhost:6432/mickaelgermemont";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pwd);
		props.setProperty("ssl", "true");
		
//		for (String arg : args) {
//			System.out.print("cli-arg=");
//			System.out.print(arg);
//			System.out.println();
//		}

		try {
			System.out.println("Trying to connect");

			try (final Connection connection = DriverManager.getConnection(url, user, pwd)) {
				System.out.println("\tEstablished db connection successfully. ");
				
				try (final ResultSet rs = connection.getMetaData().getCatalogs()){
					System.out.print(connection.getMetaData().getCatalogTerm());
					System.out.print(" list:");
					System.out.println();
					while(rs.next()) {
						final String catalogName = rs.getString(1);
						System.out.print("\t");
						System.out.print(catalogName);
						System.out.println();
					}
				}
				
				try (final ResultSet rs = connection.getMetaData().getSchemas()){
					System.out.print(connection.getMetaData().getSchemaTerm());
					System.out.print(" list:");
					System.out.println();
					while(rs.next()) {
						final String schemaName = rs.getString(1);
						final String catalogName = rs.getString(2);
						System.out.print("\t");
						System.out.print(connection.getMetaData().getSchemaTerm());
						System.out.print("=");
						System.out.print(schemaName);
						System.out.print(";");
						System.out.print(connection.getMetaData().getCatalogTerm());
						System.out.print("=");
						System.out.print(catalogName);
						System.out.println();
						
						final String tableNamePattern = null;
						
						try (final ResultSet rsTables = connection.getMetaData().getTables(catalogName, schemaName, tableNamePattern, new String[] {"TABLE"})){
							while(rsTables.next()) {
								final String tableSchemaName = rsTables.getString(2);
								final String tableName = rsTables.getString(3);
								System.out.print("\t\t");
								System.out.print(tableSchemaName);
								System.out.print(".");
								System.out.print(tableName);
								System.out.println();
								
								/**
								 * Each table description has the following columns:

	TABLE_CAT String => table catalog (may be null)
	TABLE_SCHEM String => table schema (may be null)
	TABLE_NAME String => table name
	TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	REMARKS String => explanatory comment on the table
	TYPE_CAT String => the types catalog (may be null)
	TYPE_SCHEM String => the types schema (may be null)
	TYPE_NAME String => type name (may be null)
	SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
	REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)

								 */
							}
						}
						
					}
				}
				
				connection.getMetaData().getDatabaseMajorVersion();
				connection.getMetaData().getDatabaseMinorVersion();
				connection.getMetaData().getDatabaseProductName();
				connection.getMetaData().getDatabaseProductVersion();

				connection.getMetaData().getDriverMajorVersion();
				connection.getMetaData().getDriverMinorVersion();
				connection.getMetaData().getDriverName();
				connection.getMetaData().getDriverVersion();

				connection.getMetaData().getJDBCMajorVersion();
				connection.getMetaData().getJDBCMinorVersion();
				
				connection.getMetaData().getDefaultTransactionIsolation();

				connection.getMetaData().getMaxBinaryLiteralLength();
				connection.getMetaData().getMaxCatalogNameLength();
				connection.getMetaData().getMaxCharLiteralLength();
				connection.getMetaData().getMaxColumnNameLength();
				connection.getMetaData().getMaxColumnsInTable();
				connection.getMetaData().getMaxConnections();
				connection.getMetaData().getMaxTableNameLength();
				
				connection.getMetaData().getSchemas();
				
				
//				try (final Statement stmt = connection.createStatement()) {
//
//					try (final ResultSet rs = stmt.executeQuery("select count(*) from abc")) {
//
//						while (rs.next()) {
//							final Integer count = rs.getInt(1);
//							System.out.println(String.format("count=%d", count));
//						}
//					}
//				}

			}

		} catch (Exception e) {
			System.err.println("Unable to make connection with DB");
			e.printStackTrace();
		}
	}
}
