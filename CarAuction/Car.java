package coursework_question4;

import java.text.DecimalFormat;

public class Car {

  private int id;
  private int numberOfSeats;
  private double reservedPrice;
  private String name;
  private String colour;
  private CarType gearbox;
  private CarBody body;
  private Condition condition;
  private SaleType type;
  private double price;


  public Car(int id, String name, double price, Condition condition, SaleType type) {
    super();
    if (id <= 0 || name == null || price <= 0) {
      throw new IllegalArgumentException("Cannot initalise object.");
    } else {
      this.id = id;
      this.price = price;
      this.name = name;
      this.condition = condition;
      this.type = type;

    }
  }

  public String displayCarSpecification() {
    DecimalFormat style00 = new DecimalFormat(".00");
    return getID() + " - " + getName() + " " + "(" + "£" + style00.format(getPrice()) + ")" + "\n"
        + "\tType: " + getGearbox() + "\n" + "\tStyle: " + getBody() + "\n" + "\tColour: "
        + getColour() + "\n" + "\tNo. of Seats: " + getNumberOfSeats() + "\n" + "\tCondition: "
        + getCondition();


  }

  public String toString() {
    return this.id + "" + " - " + this.name;
  }



  public int getID() {
    return id;
  }



  public int getNumberOfSeats() {
    return numberOfSeats;
  }



  public double getReservedPrice() {
    return reservedPrice;
  }



  public String getName() {
    return name;
  }



  public String getColour() {
    return colour;
  }



  public CarType getGearbox() {
    return gearbox;
  }



  public CarBody getBody() {
    return body;
  }



  public Condition getCondition() {
    return condition;
  }

  public double getPrice() {
    return price;
  }



  public void setColour(String colour) {
    this.colour = colour;
  }



  public void setGearbox(CarType gearbox) {
    this.gearbox = gearbox;
  }



  public void setBody(CarBody body) {
    this.body = body;
  }



  public void setNumberOfSeats(int numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }



  public void setID(int id) {
    this.id = id;
  }



  public void setType(SaleType type) {
    this.type = type;
  }

  public void setReservedPrice(double reservedPrice) {
    this.reservedPrice = reservedPrice;
  }



  public void setName(String name) {
    this.name = name;
  }



  public void setCondition(Condition condition) {
    this.condition = condition;
  }



  public void setPrice(double price) {
    this.price = price;
  }

  public CarBody getBodyStyle() {
    return body;
  }

  public SaleType getType() {
    return type;
  }


}
