import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LetterCounter {
    private BufferedReader source = null;
    private final Map<Character, Integer> counterInfo = new HashMap<Character, Integer>();

    public LetterCounter(BufferedReader source) {
        this.source = source;
    }

    public LetterCounter(String filename) {
        try{
            File sourceFile = new File(filename);
            source = new BufferedReader(new FileReader(sourceFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.print("*ERROR* : file '" + filename + "' was not found");
        }
    }

    Map<Character, Integer> getCounterInfo() {
        return counterInfo;
    }

    private boolean isValidSymbol(char character) {
        return (int) character >= 97 && (int) character <= 122;
    }

    public void count(boolean onlyAlphabet) {
        try {
            String row = source.readLine();
            while (row != null) {
                row = row.toLowerCase();
                for (int i = 0; i < row.length(); i++) {
                    Character curChar = row.charAt(i);
                    if (onlyAlphabet && !isValidSymbol(curChar))
                        continue;
                    int value = 0;
                    if (counterInfo.containsKey(curChar))
                        value = counterInfo.get(curChar);
                    value += 1;
                    counterInfo.put(curChar, value);
                }
                row = source.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("*ERROR* : during reading from source");
        }
    }

    public void writeCounterInfo(String filename) {
        try (FileWriter writer = new FileWriter(filename, false)) {
            for (char curChar : counterInfo.keySet()) {
                writer.write("" + curChar + " : " + counterInfo.get(curChar) + '\n');
            }
            writer.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.print("*ERROR* : during writing to file");
        }
    }
}
