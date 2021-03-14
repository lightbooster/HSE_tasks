import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        try {
            Person me = new Person("Sapozhnikov Andrey Mikchailovich 09.08.2001", true);
            System.out.print(me.getPersonInfo());
            Person friend = new Person("Lee Zoee 20.01.1990", false);
            System.out.print(friend.getPersonInfo());
        } catch (ParseException e) {
            System.out.print(e.getMessage());
        }
    }
}
// Output:
// >>> Sapozhnikov A.M.
// >>> Sex: male
// >>> Age: 19

// >>> Lee Z.
// >>> Sex: female
// >>> Age: 31