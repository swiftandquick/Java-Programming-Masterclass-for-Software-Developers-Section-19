package com.chenyma16;

import java.sql.*;

public class Main {

    public static final String DB_NAME = "testjava.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Yong Chen\\Desktop" +
                    "\\IntelliJ Work Space\\Java Programming Masterclass for Software Developers Section 19\\" +
                    "TestDB\\" + DB_NAME;

    public static final String TABLE_CONTACTS = "contacts";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";


    public static void main(String[] args) {
	    try {
	        /* Establish connection to the database called testjava.db.  */
	        Connection conn = DriverManager.getConnection(CONNECTION_STRING);

	        /* Create a Statement object where I use to write SQL codes.  */
	        Statement statement = conn.createStatement();

            /* If the contacts (TABLE_CONTACTS) table exists, drop the table.  */
            statement.execute("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

	        /* Write SQL code with the statement object, where I create a TABLE called contacts, with columns:  name
	        (text type), phone (integer type), and email (text type).  */
	        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                    " (" + COLUMN_NAME + " text, " +
                    COLUMN_PHONE + " integer, " +
                    COLUMN_EMAIL + " text" + ")");

	        insertContacts(statement, "Tim", 6545678, "tim@email.com");
            insertContacts(statement, "Joe", 45632, "joe@anywhere.com");
            insertContacts(statement, "Jane", 4829484, "jane@somewhere.com");
            insertContacts(statement, "Fido", 9038, "jog@email.com");

            // Delete the contact with the name "Joe".
            statement.execute("DELETE FROM " + TABLE_CONTACTS +
                    " WHERE " + COLUMN_NAME + "='Joe'");

            // First, use execute() to get select all columns from contacts table.
            // ResultSet:  Representing a set of database result.  Here, representing the database of contacts table.
            // getResultSet():  Retrieve the result set from statement.
            // results.next():  Returns Boolean, returns true if there’s next item, and gets the next item.
            // getString() and getInt():  Use to retrieve the values from the columns of contacts table.
            // statement.execute("SELECT * FROM contacts");
            // ResultSet results = statement.getResultSet();

            // Executes a query and store the result in the ResultSet called results.
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_CONTACTS);
            while(results.next()) {
                System.out.println(results.getString(COLUMN_NAME) + " " +
                                    results.getInt(COLUMN_PHONE) + " " +
                                    results.getString(COLUMN_EMAIL));
            }
            results.close();

	        /* Close the connection (database resources).  */
	        conn.close();
        }
	    /* Each SQLException provides several kinds of information:
	    A string describing the error. This is used as the Java Exception message, available via the method getMesasge.
        A "SQLstate" string, which follows either the XOPEN SQLstate conventions or the SQL:2003 conventions. The values of the SQLState string are described in the appropriate spec. The DatabaseMetaData method getSQLStateType can be used to discover whether the driver returns the XOPEN type or the SQL:2003 type.
        An integer error code that is specific to each vendor. Normally this will be the actual error code returned by the underlying database.
        A chain to a next Exception. This can be used to provide additional error information.
        The causal relationship, if any for this SQLException. */
	    catch (SQLException e) {
            System.out.println("Something went wrong:  " + e.getMessage());
            e.printStackTrace();
        }
    }


    /* Insert data into the contacts table.  */
    private static void insertContacts(Statement statement, String name, int phone, String email) throws SQLException {
        statement.execute("INSERT INTO " + TABLE_CONTACTS +
                " (" + COLUMN_NAME + ", " +
                COLUMN_PHONE + ", " +
                COLUMN_EMAIL + ")" +
                "VALUES('" + name + "', " + phone + ", '" + email + "')");
    }
}
