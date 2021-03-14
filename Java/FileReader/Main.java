
public class Main {

    public static void main(String[] args) {
        LetterCounter myCounter = new LetterCounter("/Users/sanduser/IdeaProjects/HSE/src/filename.txt");
        myCounter.count(true);
        myCounter.writeCounterInfo("output.txt");
    }
}
