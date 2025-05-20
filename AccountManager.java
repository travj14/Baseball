import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AccountManager {
    public boolean accountCreator(String username, String password) {
        if (usernameExists(username)) {
            return false;
        }
        try (FileWriter writer = new FileWriter("usernames.csv", true)) {
            writer.write(username + "," + password + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean usernameExists(String username) {

        try (BufferedReader reader = new BufferedReader(new FileReader("usernames.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }

    public boolean passwordCorrect(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usernames.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }
}