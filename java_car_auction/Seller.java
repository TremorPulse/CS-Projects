package coursework_question4;

public class Seller extends User {

  private int sales;

  public Seller(String name) {
    super(name);
    this.sales = 0;

  }

  public void setSales(int sales) {
    this.sales = sales;
  }

  public int getSales() {
    return sales;
  }

  public String IdentityRating() {
    if (sales == 0) {
      return "Level 0";
    }
    if (sales <= 5) {
      return "Level 1";
    }
    if (sales >= 6 && sales <= 10) {
      return "Level 2";
    }
    if (sales > 10) {
      return "Level 3";
    } else {
      return null;

    }


  }

  @Override
  public String toString() {
    char[] charName = getSurname().toCharArray();
    return getName() + " " + charName[1] + ". (Sales: " + this.sales + ", " + "Rating: "
        + IdentityRating() + ")";
  }

}

