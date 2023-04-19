package BusinessLogic;

public class FileWrite {
    public static void write(String fileName, String text) {
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter(fileName, true);
            fileWriter.write(text);
            fileWriter.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void emptyFile(String fileName) {
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter(fileName, false);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
