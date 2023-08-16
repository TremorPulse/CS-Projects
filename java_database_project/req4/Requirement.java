package com.films4you.req4;

import com.films4you.main.Database;
import com.films4you.main.RequirementInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Class tasked with finding the most popular film in each category based on the number of rentals.

 * @author will dan
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
   * Method for returning a map of all category id's within the film category table by using the film_id as a key
   * in order to get the corresponding category id.

   * @return Map of all values within the "film_id" and the corresponding "category_id" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public Map<Integer, Integer> categoryIDFromFilmCategory(){
    // Initialise HashMap, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM film_category");
    Map<Integer, Integer> filmCategoryMap = new HashMap<>();

    try {
      // While the cursor is pointed at the first row, place "film_id" and "category_id" into the map.
      while(queryResult.next() == true) {
        filmCategoryMap.put(queryResult.getInt(1), queryResult.getInt(2));

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return filmCategoryMap;
  }

  /**
   * Method for returning a map of all categories within the category table by using the category_id as a key
   * in order to get the corresponding category 

   * @return Map of all values within the "category_id" and the corresponding "name" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public Map<Integer, String> categoryFromCategory(){
    // Initialise HashMap, Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM category");
    Map<Integer, String> categoryMap = new HashMap<>();

    try {
      // While the cursor is pointed at the first row, place "category_id" and "name" into the map.
      while(queryResult.next() == true) {
        categoryMap.put(queryResult.getInt(1), queryResult.getString(2));

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return categoryMap;
  }

  /**
   * Method for returning an ArrayList of all categories within the category table.

   * @return ArrayList of all values within the "name" column or null on error.
   * @exception Catch SQLException in the case of error, print stack trace and return null.
   */

  public ArrayList<String> getCategoryName() {
    // Initialise Database and ResultSet objects to allow for querying.
    Database db = new Database();
    ResultSet queryResult = db.query("SELECT * FROM category");
    ArrayList<String> listOfCategories = new ArrayList<>();

    try {
      // While the cursor is pointed at the first row, place "payment_id" and "rental_id" into the map.
      while(queryResult.next() == true) {
        String name = queryResult.getString(2);
        listOfCategories.add(name);

      }
    } catch(SQLException e) {
      e.printStackTrace();
      return null;

    }
    return listOfCategories;
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

  public ArrayList<String> getPopularFilms() {
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

    // Initialise an ArrayList to contain the most popular films based on rentals.
    ArrayList<String> mostPopularFilms = new ArrayList<>();
    // Initialise an ArrayList to contain the most frequently recurring film id's.
    ArrayList<Integer> mostFrequentID = mostFrequentValues(filmIDList, 500);

    // Iterate over the ArrayList of most frequently recurring film id's.
    for(Integer β : mostFrequentID) {
      // Collect the top 10 most frequently recurring film id's to get a corresponding title. Initialise this as a String z.
      String z = filmMap.get(β);
      // Add the corresponding titles to an ArrayList "mostPopularFilms".
      mostPopularFilms.add(z);
    }

    return mostPopularFilms;
  }


  /**
   * Method for returning an ArrayList of the top 10 most popular films based on rentals.

   * @return ArrayList of top 10 films.
   */

  public ArrayList<String> getPopularFilmByCategory(){
    // Initialise maps and lists.
    ArrayList<Integer> actionList = new ArrayList<>();
    ArrayList<Integer> animationList = new ArrayList<>();
    ArrayList<Integer> childrenList = new ArrayList<>();
    ArrayList<Integer> classicsList = new ArrayList<>();
    ArrayList<Integer> comedyList = new ArrayList<>();
    ArrayList<Integer> documentaryList = new ArrayList<>();
    ArrayList<Integer> dramaList = new ArrayList<>();
    ArrayList<Integer> familyList = new ArrayList<>();
    ArrayList<Integer> foreignList = new ArrayList<>();
    ArrayList<Integer> gamesList = new ArrayList<>();
    ArrayList<Integer> horrorList = new ArrayList<>();
    ArrayList<Integer> musicList = new ArrayList<>();
    ArrayList<Integer> newList = new ArrayList<>();
    ArrayList<Integer> sciFiList = new ArrayList<>();
    ArrayList<Integer> sportsList = new ArrayList<>();
    ArrayList<Integer> travelList = new ArrayList<>();
    Map<Integer, Integer> categoryID = categoryIDFromFilmCategory();

    // Iterate over the map of category id's.
    for(Entry<Integer, Integer> entry : categoryID.entrySet()) {
      // Switch statement that adds the corresponding film id's to its respective category list.
      switch(entry.getValue()) {
        // Case 1-16 represents the list of categories.
        case 1:
          actionList.add(entry.getKey());
          break;

        case 2:
          animationList.add(entry.getKey());
          break;

        case 3:
          childrenList.add(entry.getKey());
          break;

        case 4:
          classicsList.add(entry.getKey());
          break;

        case 5:
          comedyList.add(entry.getKey());
          break;

        case 6:
          documentaryList.add(entry.getKey());
          break;

        case 7:
          dramaList.add(entry.getKey());
          break;

        case 8:
          familyList.add(entry.getKey());
          break;

        case 9:
          foreignList.add(entry.getKey());
          break;

        case 10:
          gamesList.add(entry.getKey());
          break;

        case 11:
          horrorList.add(entry.getKey());
          break;

        case 12:
          musicList.add(entry.getKey());
          break;

        case 13:
          newList.add(entry.getKey());
          break;

        case 14:
          sciFiList.add(entry.getKey());
          break;

        case 15:
          sportsList.add(entry.getKey());
          break;

        case 16:
          travelList.add(entry.getKey());
          break;   

        default:
          System.out.println("Couldn't find category.");
      }

    }

    // Initialise lists and map.
    ArrayList<Integer> sortedActionList = mostFrequentValues(actionList, 500);
    ArrayList<Integer> sortedAnimationList = mostFrequentValues(animationList, 500);
    ArrayList<Integer> sortedChildrenList = mostFrequentValues(childrenList, 500);
    ArrayList<Integer> sortedClassicsList = mostFrequentValues(classicsList, 500);
    ArrayList<Integer> sortedComedyList = mostFrequentValues(comedyList, 500);
    ArrayList<Integer> sortedDocumentaryList = mostFrequentValues(documentaryList, 500);
    ArrayList<Integer> sortedDramaList = mostFrequentValues(dramaList, 500);
    ArrayList<Integer> sortedFamilyList = mostFrequentValues(familyList, 500);
    ArrayList<Integer> sortedForeignList = mostFrequentValues(foreignList, 500);
    ArrayList<Integer> sortedGamesList = mostFrequentValues(gamesList, 500);
    ArrayList<Integer> sortedHorrorList = mostFrequentValues(horrorList, 500);
    ArrayList<Integer> sortedMusicList = mostFrequentValues(musicList, 500);
    ArrayList<Integer> sortedNewList = mostFrequentValues(newList, 500);
    ArrayList<Integer> sortedSciFiList = mostFrequentValues(sciFiList, 500);
    ArrayList<Integer> sortedSportsList = mostFrequentValues(sportsList, 500);
    ArrayList<Integer> sortedTravelList = mostFrequentValues(travelList, 500);
    ArrayList<String> finalFilms = new ArrayList<>();
    ArrayList<Integer> returnList = new ArrayList<>();
    Map<Integer, String> filmName = filmNameFromFilm();


    // Add the most popular film in each category to a list.
    returnList.add(sortedActionList.get(0));
    returnList.add(sortedAnimationList.get(0));
    returnList.add(sortedChildrenList.get(0));
    returnList.add(sortedClassicsList.get(0));
    returnList.add(sortedComedyList.get(0));
    returnList.add(sortedDocumentaryList.get(0));
    returnList.add(sortedDramaList.get(0));
    returnList.add(sortedFamilyList.get(0));
    returnList.add(sortedForeignList.get(0));
    returnList.add(sortedGamesList.get(0));
    returnList.add(sortedHorrorList.get(0));
    returnList.add(sortedMusicList.get(0));
    returnList.add(sortedNewList.get(0));
    returnList.add(sortedSciFiList.get(0));
    returnList.add(sortedSportsList.get(0));
    returnList.add(sortedTravelList.get(0));

    // Iterate over the ArrayList that contains the most popular film from each category 1-16.
    for(Integer λ : returnList) {
      // Collect the top most frequently recurring film id to get a corresponding title. Initialise this as a String μ.
      String μ = filmName.get(λ);
      // Add the corresponding titles to an ArrayList "finalFilms".
      finalFilms.add(μ);
    }

    return finalFilms;
  }

  /**
   * Method to return the most popular film from each category as a string.

   * @return Most popular film from each category.
   */

  @Override
  public @Nullable String getValueAsString() {
    return getPopularFilmByCategory().toString();
  }

  /**
   * Method to return the most popular film from each category in a human readable format.

   * @return Most popular film from each category for a human to read.
   */

  @Override
  public @NonNull String getHumanReadable() {
    return "The top action movie is: " + getPopularFilmByCategory().get(0) + "\n" + 
        "The top animation movie is: " + getPopularFilmByCategory().get(1) + "\n" +
        "The top childrens movie is: " + getPopularFilmByCategory().get(2) + "\n" + 
        "The top classics movie is: " + getPopularFilmByCategory().get(3) + "\n" + 
        "The top comedy movie is: " + getPopularFilmByCategory().get(4) + "\n" +
        "The top documentary movie is: " + getPopularFilmByCategory().get(5) + "\n" + 
        "The top drama movie is: " + getPopularFilmByCategory().get(6) + "\n" + 
        "The top family movie is: " + getPopularFilmByCategory().get(7) + "\n" +
        "The top foreign movie is: " + getPopularFilmByCategory().get(8) + "\n" + 
        "The top game movie is: " + getPopularFilmByCategory().get(9) + "\n" + 
        "The top horror movie is: " + getPopularFilmByCategory().get(10) + "\n" + 
        "The top music movie is: " + getPopularFilmByCategory().get(11) + "\n" + 
        "The top new movie is: " + getPopularFilmByCategory().get(12) + "\n" + 
        "The top action sci-fi is: " + getPopularFilmByCategory().get(13) + "\n" +
        "The top sports movie is: " + getPopularFilmByCategory().get(14) + "\n" + 
        "The top travel movie is :" + getPopularFilmByCategory().get(15) + "\n";
  }

  public static void main(String[] args)  {
    Requirement r = new Requirement();
    System.out.println(r.getHumanReadable());
  }

}
