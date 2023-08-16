package coursework_question4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Auctioneer extends Dealership {

  private Map<Seller, Integer> sales;
  private Seller topSeller;
  private int totalAutomaticCarsSold;
  private int totalManualCarsSold;

  public Auctioneer(String name) {
    super(name);
    this.topSeller = null;
    sales = new HashMap<Seller, Integer>();

  }


  private boolean checkExistence(Car car) {
    for (Advert v : carsForSale.keySet()) {
      if (v.getCar().equals(car)) {
        return true;
      }
    }
    return false;

  }


  public void endSale(Advert advert) {
    if (advert == null) {
      throw new IllegalArgumentException();
    } else if (checkExistence(advert.getCar()) == true) {
      if (advert.getHighestOffer().getValue() >= advert.getCar().getPrice()) {
        soldCars.put(advert, (Buyer) advert.getHighestOffer().getBuyer());
        updateStatistics(carsForSale.get(advert));
        carsForSale.remove(advert);
      } else {
        unsoldCars.put(advert, carsForSale.get(advert));
        carsForSale.remove(advert);
      }
    }
  }

  @Override
  public boolean placeOffer(Advert carAdvert, User user, double value) {
    if (carAdvert == null || user == null || value <= 0) {
      throw new IllegalArgumentException();
    } else if (checkExistence(carAdvert.getCar()) == true) {
      carAdvert.placeOffer(user, value);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void registerCar(Advert carAdvert, User user, String colour, CarType type, CarBody body,
      int noOfSeats) {
    if (carAdvert == null || user == null || colour == null || type == null || body == null) {
      throw new IllegalArgumentException();
    } else if (checkExistence(carAdvert.getCar()) == false) {
      carAdvert.getCar().setColour(colour);
      carAdvert.getCar().setGearbox(type);
      carAdvert.getCar().setBody(body);
      carAdvert.getCar().setNumberOfSeats(noOfSeats);
      carsForSale.put(carAdvert, (Seller) user);
    }

  }

  private void saveInFile(int noOfSales, double percentageOfManual, double percentageOfAutomatic) {
    File file = new File("auction_statistics.txt");
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(file);
      fileWriter.write("Total Auction Sales: " + noOfSales + "\n" + "Automatic Cars: "
          + percentageOfAutomatic + "%\n" + "Manual Cars: " + percentageOfManual + "%\n"
          + "Top Seller: " + topSeller.toString());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }



  private void updateStatistics(Seller seller) {
    seller.setSales(seller.getSales() + 1);
    double percentManual = 0D;
    double percentAuto = 0D;
    for (Advert x : soldCars.keySet()) {
      if (x.getCar().getGearbox() == CarType.AUTOMATIC) {
        percentAuto++;

      } else if (x.getCar().getGearbox() == CarType.MANUAL) {
        percentManual++;
      }
      sales.put(seller, seller.getSales());

    }
    if (topSeller == null || sales.get(topSeller) < sales.get(seller)) {
      this.topSeller = seller;
    }
    percentManual = (percentManual / soldCars.size()) * 100;
    percentAuto = (percentAuto / soldCars.size()) * 100;
    int total = soldCars.size();
    saveInFile(total, percentManual, percentAuto);


  }

  @Override
  public String displayStatistics() {
    BufferedReader reader = null;
    StringBuffer display = new StringBuffer();

    display.append("** Auctioneer - " + name + "**\n");
    try {
      reader = new BufferedReader(new FileReader("auction_statistics.txt"));
      String line = reader.readLine();

      while (line != null) {
        display.append(line + "\n");
        line = reader.readLine();
      }
      display.setLength(display.length() - 1);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return display.toString();
  }



  public Map<Seller, Integer> getSales() {
    return sales;
  }


  public Seller getTopSeller() {
    return topSeller;
  }


  public int getTotalAutomaticCarsSold() {
    return totalAutomaticCarsSold;
  }


  public int getTotalManualCarsSold() {
    return totalManualCarsSold;
  }

}

