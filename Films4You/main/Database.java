package com.films4you.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A class to connect to, query, and close the testing database in one go.
 * 

 * 
 *
 */
public class Database {
  /* URI specifying the JBDC connection method and testing database location. */
  private static final String URI_TESTING_DB = "jdbc:sqlite:testing.db";
  private @Nullable Connection conn = null;

  /**
   * Execute query on database and handle opening and closing the connection. 

   * @param sql String containing SQL statement to execute.
   * @return ResultSet for query or null on error which is read-only and 
   *     scroll-forward only (as SQLite supports only forward cursors).
   */
  public @Nullable ResultSet query(String sql) {
    try {
      if (conn == null) {
        conn = DriverManager.getConnection(URI_TESTING_DB);
      }
      Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, 
          ResultSet.CONCUR_READ_ONLY);
      return st.executeQuery(sql);
    } catch (SQLException e) {
      System.err.println(e);
      return null;
    }
  }
  
  /**
   * Close the database connection once query finished.
   */
  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }
}
