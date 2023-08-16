package com.films4you.req2;

import com.films4you.main.Database;
import com.films4you.main.RequirementInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Class responsible for listing the top 10 most popular films based on the number of rentals.

 * @author will
 *
 */

public class Requirement implements RequirementInterface {

  public Requirement() { // Default constructor to initialise the attributes of the objects.
    super();
  }

  /**
   * Method for returning an ArrayList of all rental id's within the payment table.

   * @return ArrayList of all values within the "rental_id" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public ArrayList<Integer> rentalIDFromPayment() {
    // Initialise Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM payment");
    ArrayList<Integer> rentalIDs  = new ArrayList<>();

    try {
      // While the cursor is pointed at the first row, place "payment_id" and "rental_id" into the map.
      while(queryResult.next() == true) {
        int tableID = queryResult.getInt(4);
        rentalIDs.add(tableID);

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return rentalIDs;
  }

  /**
   * Method for returning a map of all inventory id's by using the rental_id as a key within the rental table 
   * in order to get a corresponding value.

   * @return Map of all values within the "inventory_id" and the corresponding "rental_id" key column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public Map<Integer, Integer> inventoryIDFromRental() {
    // Initialise HashMap, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM rental");
    Map<Integer, Integer> rentalMap = new HashMap<>();


    try {
      // While the cursor is pointed at the first row, place "rental_id" and "inventory_id" into the map.
      while(queryResult.next() == true) {
        rentalMap.put(queryResult.getInt(1), queryResult.getInt(3));

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return rentalMap;
  }

  /**
   * Method for returning a map of all film id's within the inventory table by using the inventory_id as a key 
   * in order to get the corresponding film_id value.

   * @return Map of all values within the "inventory_id" and the corresponding "film_id" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public Map<Integer, Integer> filmIDFromInventory() {
    // Initialise HashMap, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM inventory");
    Map<Integer, Integer> inventoryMap = new HashMap<>();

    try {
      // While the cursor is pointed at the first row, place "inventory_id" and "film_id" into the map.
      while(queryResult.next() == true) {
        inventoryMap.put(queryResult.getInt(1), queryResult.getInt(2));

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return inventoryMap;
  }

  /**
   * Method for returning a map of all film names within the film table by using the film_id as a key
   * in order to get the corresponding film name. 

   * @return Map of all values within the "film_id" and the corresponding "title" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public Map<Integer, String> filmNameFromFilm() {
    // Initialise HashMap, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM film");
    Map<Integer, String> filmMap = new HashMap<>();

    try {
      // While the cursor is pointed at the first row, place "film_id" and "title" into the map.
      while(queryResult.next() == true) {
        filmMap.put(queryResult.getInt(1), queryResult.getString(2));

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return filmMap;
  }

  /**
   * Method for sorting an ArrayList of integers by the most frequently recurring values in order from highest to lowest up
   * to the nth value.

   * @return ArrayList of most frequently recurring values in order up to an nth entry.
   * @param ArrayList list of type Integer representing the list we wish to order by most recurring values.
   * @param int n representing the nth entry.
   */

  public static ArrayList<Integer> mostFrequentValues(ArrayList<Integer> list, int n) {
    ArrayList<Integer> mostFrequentValues = new ArrayList<>();
    HashMap<Integer, Integer> frequencyMap = new HashMap<>();

    // Calculate the frequency of each value in the list by first iterating over the ArrayList.
    for (Integer value : list) {
      frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
    }

    // Sort the map by value in descending order and extract the first n entries.
    List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(frequencyMap.entrySet());
    sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    for (int i = 0; i < n && i < sortedEntries.size(); i++) {
      mostFrequentValues.add(sortedEntries.get(i).getKey());
    }

    return mostFrequentValues;              
  }

  /**
   * Method for returning an ArrayList of the top 10 most popular films based on rentals.

   * @return ArrayList of top 10 films.
   */

  public ArrayList<String> getTop10Films() {
    // Initialise list and maps.
    List<Integer> paymentList = rentalIDFromPayment();
    Map<Integer, Integer> rentalMap = inventoryIDFromRental();
    Map<Integer, Integer> inventoryMap = filmIDFromInventory();
    Map<Integer, String> filmMap = filmNameFromFilm();

    // Initialise an ArrayList to contain all possible film id's.
    ArrayList<Integer> filmIDList = new ArrayList<>();

    // Iterate over the list of rental id's collected from the payment table.
    for (Integer α : paymentList) {
      // Collect all rental id's from the rentalMap to get a corresponding inventory_id. Initialise this as an Integer x.
      Integer x = rentalMap.get(α);
      // Collect all inventory id's from the inventoryMap to get a corresponding film_id. Initialise this as an Integer y.
      Integer y = inventoryMap.get(x);
      // Add the corresponding film_id to an ArrayList "filmIDList".
      filmIDList.add(y);
    }

    // Initialise an ArrayList to contain the top 10 most popular films based on rentals.
    ArrayList<String> top10Films = new ArrayList<>();
    // Initialise an ArrayList to contain the top 10 most frequently recurring film id's.
    ArrayList<Integer> mostFrequentID = mostFrequentValues(filmIDList, 10);

    // Iterate over the ArrayList of most frequently recurring film id's.
    for(Integer β : mostFrequentID) {
      // Collect the top 10 most frequently recurring film id's to get a corresponding title. Initialise this as a String z.
      String z = filmMap.get(β);
      // Add the corresponding titles to an ArrayList "top10Films".
      top10Films.add(z);
    }

    return top10Films;
  }

  /**
   * Method to return top 10 films based on the numbers of rentals as a string.

   * @return Top 10 films as a string.
   */

  @Override
  public @Nullable String getValueAsString() {
    return getTop10Films().toString();
  }

  /**
   * Method to return top 10 films in a human readable format.

   * @return Top 10 films formatted for a human to read.
   */

  @Override
  public @NonNull String getHumanReadable() {
    return "The top 10 most popular films based on numbers of rentals are:" + "\n 1." + getTop10Films().get(0) + 
        "\n 2." + getTop10Films().get(1) + "\n 3." + getTop10Films().get(2) + "\n 4." + getTop10Films().get(3) + 
        "\n 5." + getTop10Films().get(4) + "\n 6." + getTop10Films().get(5) + "\n 7." + getTop10Films().get(6) +
        "\n 8." + getTop10Films().get(7) + "\n 9." + getTop10Films().get(8) + "\n 10." + getTop10Films().get(9);
  } 

  public static void main(String[] args)  {
    Requirement r = new Requirement();
    System.out.println(r.getHumanReadable());
  }

}








