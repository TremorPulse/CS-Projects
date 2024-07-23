package coursework_question4;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public abstract class Dealership {

  protected String name;
  protected Map<Advert, Seller> carsForSale;
  protected Map<Advert, Buyer> soldCars;
  protected Map<Advert, Seller> unsoldCars;

  public Dealership(String name) {
    super();
    if (name == null) {
      throw new IllegalArgumentException("No name found.");
    } else {
      this.name = name;
      carsForSale = new HashMap<Advert, Seller>();
      soldCars = new HashMap<Advert, Buyer>();
      unsoldCars = new HashMap<Advert, Seller>();
    }
  }


  public abstract boolean placeOffer(Advert carAdvert, User user, double value);

  public abstract void registerCar(Advert carAdvert, User user, String colour, CarType type,
      CarBody body, int noOfSeats);

  public abstract String displayStatistics();

  public String displaySoldCars() {
    StringBuffer sb = new StringBuffer();
    DecimalFormat style00 = new DecimalFormat(".00");
    sb.append("SOLD CARS:\n");
    for (Advert x : soldCars.keySet()) {
      sb.append(x.getCar().getID() + " - Purchased by " + x.getHighestOffer().getBuyer().toString()
          + " with a successful £" + style00.format(x.getHighestOffer().getValue()) + " bid.\n");
    }
    return sb.toString();

  }

  public String displayUnsoldCars() {
    StringBuffer sb = new StringBuffer();
    sb.append("UNSOLD CARS:\n");
    for (Advert y : unsoldCars.keySet()) {
      sb.append(y.toString());
    }
    return sb.toString();
  }

}


