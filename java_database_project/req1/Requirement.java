package com.films4you.req1;

import com.films4you.main.Database;
import com.films4you.main.RequirementInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Class responsible for returning the total number of actors.

 * @author will
 *
 */

public class Requirement implements RequirementInterface {

  public Requirement() { // Default constructor to initialise the attributes of the objects.
    super();

  }
  /**
   * Method for finding total number of actors.

   * @return The number of rows within the actor table as a String or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public String getTotalActors() { 
    // Initialise Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM actor");

    try {
      // While the cursor is pointed at the first row, get number of rows and return to a string format.
      while(queryResult.next() == true) {

      }
      int numberOfRows = queryResult.getRow();
      return Integer.toString(numberOfRows);

    } catch (SQLException e) {
      e.printStackTrace();
      return null;

    } 
  }

  /**
   * Method to return total numbers of actors as a string.

   * @return Top 10 films as a string.
   */

  @Override
  public @Nullable String getValueAsString() {
    return getTotalActors();
  }

  /**
   * Method to return total number of actors in a human readable format.

   * @return Total number of actors formatted for a human to read.
   */

  @Override
  public @NonNull String getHumanReadable() {
    return "The total number of actors is " + getTotalActors() + ".";
  }

  public static void main(String[] args)  {
    Requirement r = new Requirement();
    System.out.println(r.getHumanReadable());

  }

}

