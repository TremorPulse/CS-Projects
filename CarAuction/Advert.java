package coursework_question4;

import java.util.ArrayList;
import java.util.List;

public class Advert {

  private Car car;
  private List<Offer> offers;

  public Advert(Car car) {
    if (car == null) {
      throw new IllegalArgumentException("Cannot create an advert");
    } else {
      this.car = car;
      offers = new ArrayList<>();
    }

  }

  public Offer getHighestOffer() {
    if (offers.get(0) == null) {
      throw new IllegalArgumentException("Cannot place offer");
    }
    Offer highest = offers.get(0);
    for (Offer offer : offers) {
      if (offer.getValue() > highest.getValue()) {
        highest = offer;
      }
    }
    return highest;
  }

  public boolean placeOffer(User buyer, double value) {
    if (buyer == null || value <= 0) {
      throw new IllegalArgumentException();
    } else {
      Offer newOffer = new Offer(buyer, value);
      offers.add(newOffer);
      return true;
    }
  }

  public String toString() {
    return "Ad: " + car.displayCarSpecification() + "\n";
  }

  public Car getCar() {
    return car;
  }

  public List<Offer> getOffers() {
    return offers;
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }

  public void setCar(Car car) {
    this.car = car;
  }

}
