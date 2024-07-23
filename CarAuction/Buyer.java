package coursework_question4;

public class Buyer extends User {

  private int age;

  public Buyer(String name, int age) {
    super(name);
    if (age < 18) {
      throw new IllegalArgumentException("Must be over 18 to purchase a car.");
    } else {
    }
    this.age = age;

  }

  public int getAge() {
    return age;

  }

  @Override
  public String toString() {
    char[] charName = getName().toCharArray();
    int n = charName.length;
    return charName[0] + "***" + charName[n - 1];
  }

}

