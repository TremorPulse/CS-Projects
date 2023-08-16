package coursework_question4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Trader extends Dealership {

  private List<Seller> sellers;

  public Trader(String name) {
    super("AutoTrader");
    sellers = new ArrayList<>();
  }


  private boolean checkExistence(Car car) {
    for (Advert v : carsForSale.keySet()) {
      if (v.getCar().equals(car)) {
        return true;

      }
    }
    for (Advert x : unsoldCars.keySet()) {
      if (x.getCar().equals(car)) {
        return true;
      }

    }
    return false;

  }

  private void endSale(Advert advert) {
    if (advert == null) {
      throw new IllegalArgumentException();
    } else if (checkExistence(advert.getCar()) == true) {
      if (carsForSale.get(advert) == null) {
        if (advert.getHighestOffer().getValue() >= advert.getCar().getPrice()) {
          soldCars.put(advert, advert.getHighestOffer().getBuyer());
          updateStatistics(unsoldCars.get(advert));
          unsoldCars.remove(advert);
        }
      } else {
        if (advert.getHighestOffer().getValue() >= advert.getCar().getPrice()) {
          soldCars.put(advert, advert.getHighestOffer().getBuyer());
          updateStatistics(carsForSale.get(advert));
          carsForSale.remove(advert);
        } else {
          unsoldCars.put(advert, carsForSale.get(advert));
          carsForSale.remove(advert);
        }
      }
    }
  }


  @Override
  public boolean placeOffer(Advert carAdvert, User user, double value) {
    if (carAdvert == null || user == null || value <= 0) {
      throw new IllegalArgumentException();
    } else if (checkExistence(carAdvert.getCar()) == true
        && carAdvert.getCar().getType() == SaleType.FORSALE
        && value >= carAdvert.getCar().getPrice()) {
      carAdvert.placeOffer(user, value);
      endSale(carAdvert);
      return true;
    } else {
      carAdvert.placeOffer(user, value);
      endSale(carAdvert);
      return false;
    }
  }


  @Override
  public void registerCar(Advert carAdvert, User user, String colour, CarType type, CarBody body,
      int noOfSeats) {
    if (carAdvert == null || user == null || colour == null || type == null || body == null) {
      throw new IllegalArgumentException();
    } else if (checkExistence(carAdvert.getCar()) == false) {
      {

        carAdvert.getCar().setColour(colour);
        carAdvert.getCar().setGearbox(type);
        carAdvert.getCar().setBody(body);
        carAdvert.getCar().setNumberOfSeats(noOfSeats);
        carsForSale.put(carAdvert, (Seller) user);
      }


    }

  }

  private void saveInFile(int noOfSales) {
    File file = new File("trade_statistics.txt");
    FileWriter fileWriter = null;

    try {
      fileWriter = new FileWriter(file);
      fileWriter.write("Total Sales: " + noOfSales + "\n" + "All Sellers:\n");
      for (Seller seller : sellers) {
        fileWriter.write("\t" + seller.toString());
        if (!(sellers.indexOf(seller) == sellers.size() - 1)) {
          fileWriter.write("\n");
        }
      }
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

  @Override
  public String displayStatistics() {
    BufferedReader reader = null;
    StringBuffer display = new StringBuffer();

    display.append("** Trader - " + name + "**\n");
    try {
      reader = new BufferedReader(new FileReader("trade_statistics.txt"));
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

  private void updateStatistics(Seller seller) {
    seller.setSales(seller.getSales() + 1);
    if (!sellers.contains(seller)) {
      sellers.add(seller);
    }
    int total = soldCars.size();
    saveInFile(total);

  }


}

