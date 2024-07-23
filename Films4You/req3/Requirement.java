package com.films4you.req3;

import com.films4you.main.Database;
import com.films4you.main.RequirementInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Class tasked with finding all actors with first name "PENELOPE".

 * @author will dan
 *
 */

public class Requirement implements RequirementInterface {

  public Requirement() { // Default constructor to initialise the attributes of the objects.
    super();
  }

  /**
   * Method for returning an ArrayList of actors's last name that have first name as "PENELOPE".

   * @return ArrayList of all last names that contain first name "PENELOPE" or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public ArrayList<String> findPenelopes() { 
    // Initialise ArrayList, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM actor");
    ArrayList<String> listOfPenelopes = new ArrayList<>();

    try {
      // While the cursor is pointed at the first row and if "first_name" is "PENELOPE", add "last_name" to ArrayList "listOfPenelopes".
      while(queryResult.next() == true) {
        if(queryResult.getString(2).equals("PENELOPE")) {
          String lastName = queryResult.getString(3);
          listOfPenelopes.add(lastName);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return null;

    }
    return listOfPenelopes;  
  }

  /**
   * Method to return the names of actors with first name "PENELOPE" as a string.

   * @return Name of actors with first name "PENELOPE".
   */

  @Override
  public @Nullable String getValueAsString() {
    ArrayList<String> lastNames = findPenelopes();
    ArrayList<String> penelopeNames = new ArrayList<>();
    String α = "PENELOPE ";
    String β = null;

    for(String name : lastNames) {
      β = α.concat(name);
      penelopeNames.add(β);

    }
    return penelopeNames.toString();
  }

  /**
   * Method to return the names of actors with first name "PENELOPE" in a human readable format.

   * @return Name of actors with first name "PENELOPE" for a human to read.
   */

  @Override
  public @NonNull String getHumanReadable() {
    ArrayList<String> lastNames = findPenelopes();
    ArrayList<String> penelopeNames = new ArrayList<>();
    String γ = "PENELOPE ";
    String δ = null;

    for(String name : lastNames) {
      δ = γ.concat(name);
      penelopeNames.add(δ);

    }
    return "The following actors have the name PENELOPE: " + "\n" + penelopeNames.get(0) + "\n" + 
    penelopeNames.get(1) + "\n" + 
    penelopeNames.get(2) + "\n" + 
    penelopeNames.get(3) + "\n";
  }

  public static void main(String[] args)  {
    Requirement r = new Requirement();
    System.out.println(r.getHumanReadable());
  }

}
