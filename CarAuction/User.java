package coursework_question4;

public abstract class User {

  private String name;
  private String surname;
  private String fullname = name + " " + surname;
  private String regex_1 = "[A-Z]{1}[a-z]* [A-Z]{1}[a-z]*";
  private String regex_2 = "[A-Z]{1}[a-z]*";

  public User(String fullname) {
    super();
    if (fullname.matches(regex_1)) {
      this.fullname = fullname;
      name = fullname.substring(0, fullname.indexOf(" "));
      surname = fullname.substring(fullname.indexOf(" "), fullname.length());
    }
    if (fullname.matches(regex_2)) {
      name = fullname.substring(0, fullname.indexOf(" "));
    }

    if (name == null) {
      throw new IllegalArgumentException("Error.");
    } else {
      this.fullname = fullname;

    }

  }


  public String getName() {
    return name;

  }

  public String getSurname() {
    return surname;
  }

  public abstract String toString();

}

