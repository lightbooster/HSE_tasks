import java.util.*;
import java.text.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Person {

    public enum Sex { MALE, FEMALE, ELSE}

    private final String firstName;
    private final String lastName;
    private String patronymicName = "";
    private final Date birthDate;
    private int age;
    private Sex sex;
    private String sexString;

    public Person(String inputData, boolean hasPatronymicName) throws ParseException {
        String[] splitStr = inputData.trim().split("\\s+");
        if (hasPatronymicName && splitStr.length != 4 ||
                !hasPatronymicName && splitStr.length != 3)
            throw new ParseException("*Error* : invalid personal info format (number of elements)", 0);
        firstName = splitStr[1];
        lastName = splitStr[0];
        String dateString;
        if (hasPatronymicName) {
            patronymicName = splitStr[2];
            dateString = splitStr[3];
        } else {
            dateString = splitStr[2];
        }
        birthDate = parseDate(dateString);
        calculateAge();
        predictSex();
    }

    String getName() {
        return firstName + lastName + patronymicName;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    String getPatronymicName() {
        return patronymicName;
    }

    Date getBirthDate() {
        return birthDate;
    }

    int getAge() {
        return age;
    }

    Sex getSex() {
        return sex;
    }

    String getSexString() {
        return sexString;
    }

    String getPersonInfo() {
        String initials = lastName + ' ' + firstName.charAt(0) + '.';
        if (!patronymicName.equals("")){
            initials += "" + patronymicName.charAt(0) + '.';
        }
        return initials + "\nSex: " + sexString + "\nAge: " + age + '\n';
    }

    public static Date parseDate(String birthDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        return sdf.parse(birthDate);
    }

    public void calculateAge() {
        Date curDate = new Date();
        long diff = Math.abs(curDate.getTime() - birthDate.getTime());
        age = (int)((double)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) / 365);
    }

    public void predictSex() {
        // using 'gender-api.com' service
        try {
            String myKey = "snflttahtomeJUPutU";
            URL url = new URL("https://gender-api.com/get?key=" + myKey + "&name=" + firstName.toLowerCase());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error: " + conn.getResponseCode());
            }

            InputStreamReader input = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(input);

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            conn.disconnect();

            sexString = json.get("gender").getAsString();
            if (sexString.equals("male")) {
                sex = Sex.MALE;
            } else if (sexString.equals("female")) {
                sex = Sex.MALE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
