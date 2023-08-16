package coursework_question4;

import java.text.DecimalFormat;

public class Offer {

  private double value;
  private User buyer;

  public Offer(User buyer, double value) {
    super();
    if (value < 0) {
      throw new IllegalArgumentException("The value must be greater than 0.");
    } else {
      this.value = value;
      this.buyer = buyer;
    }
    if (buyer == null) {
      throw new IllegalArgumentException("There is no buyer.");
    } else {
      this.value = value;
      this.buyer = buyer;
    }


  }

  public Buyer getBuyer() {
    return (Buyer) buyer;
  }

  public double getValue() {
    return value;

  }

  public String toString() {
    DecimalFormat style00 = new DecimalFormat(".00");
    return getBuyer() + " " + "offered" + " " + "£" + style00.format(getValue());

  }

}

