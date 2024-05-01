import java.io.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.*;

class Abonent implements Serializable {
    private String name;
    private String phoneNumber;

    public Abonent(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone Number: " + phoneNumber;
    }
}

class PhoneDirectory {
    private Map<String, Abonent> directory;
    

    public void saveToJsonFile(String fileName) {
        JSONArray jsonArray = new JSONArray();
        for (Abonent abonent : directory.values()) {
            JSONObject jsonAbonent = new JSONObject();
            jsonAbonent.put("name", abonent.getName());
            jsonAbonent.put("phoneNumber", abonent.getPhoneNumber());
            jsonArray.add(jsonAbonent);
        }
        
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public PhoneDirectory() {
        directory = new HashMap<>();
    }

    public void addAbonent(String name, String phoneNumber) {
        Abonent abonent = new Abonent(name, phoneNumber);
        directory.put(name, abonent);
    }

    public void removeAbonent(String name) {
        directory.remove(name);
    }

    public Abonent findAbonent(String name) {
        return directory.get(name);
    }

    public List<Abonent> getAllAbonentsSortedByName() {
        List<Abonent> abonents = new ArrayList<>(directory.values());
        abonents.sort(Comparator.comparing(Abonent::getName));
        return abonents;
    }

    public List<Abonent> getAllAbonentsSortedByPhoneNumber() {
        List<Abonent> abonents = new ArrayList<>(directory.values());
        abonents.sort(Comparator.comparing(Abonent::getPhoneNumber));
        return abonents;
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            directory = (Map<String, Abonent>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addAbonentFromFile(String name, String phoneNumber, String fileName) {
        loadFromFile(fileName);
        addAbonent(name, phoneNumber);
        saveToFile(fileName);
    }

    public void removeAbonentFromFile(String name, String fileName) {
        loadFromFile(fileName);
        removeAbonent(name);
        saveToFile(fileName);
    }

    public void modifyAbonent(String name, String newPhoneNumber, String fileName) {
        loadFromFile(fileName);
        Abonent abonent = findAbonent(name);
        if (abonent != null) {
            abonent = new Abonent(name, newPhoneNumber);
            directory.put(name, abonent);
            saveToFile(fileName);
        } else {
            System.out.println("Abonent not found.");
        }
    }

    public List<Abonent> searchInFile(String query, String fileName) {
        List<Abonent> searchResults = new ArrayList<>();
        loadFromFile(fileName);
        for (Abonent abonent : directory.values()) {
            if (abonent.getName().contains(query) || abonent.getPhoneNumber().contains(query)) {
                searchResults.add(abonent);
            }
        }
        return searchResults;
    }
}

public class Lab4 {

    public static void main(String[] args) {
        PhoneDirectory phoneDirectory = new PhoneDirectory();

        phoneDirectory.addAbonent("John", "123456789");
        phoneDirectory.addAbonent("Alice", "987654321");
        phoneDirectory.addAbonent("Bob", "456789123");

        phoneDirectory.saveToFile("phone_directory.txt");

        // Додавання абонента з файлу
        phoneDirectory.addAbonentFromFile("Carol", "555555555", "phone_directory.txt");

        // Видалення абонента з файлу
        phoneDirectory.removeAbonentFromFile("John", "phone_directory.txt");

        // Модифікація абонента
        phoneDirectory.modifyAbonent("Alice", "999999999", "phone_directory.txt");

        // Пошук абонентів за запитом у файлі
        List<Abonent> searchResults = phoneDirectory.searchInFile("99", "phone_directory.txt");
        System.out.println("Search Results:");
        for (Abonent abonent : searchResults) {
            System.out.println(abonent);
        }

        List<Abonent> abonentsByName = phoneDirectory.getAllAbonentsSortedByName();
        System.out.println("\nSorted by name:");
        for (Abonent abonent : abonentsByName) {
            System.out.println(abonent);
        }

        List<Abonent> abonentsByPhoneNumber = phoneDirectory.getAllAbonentsSortedByPhoneNumber();
        System.out.println("\nSorted by phone number:");
        for (Abonent abonent : abonentsByPhoneNumber) {
            System.out.println(abonent);
        }
        phoneDirectory.saveToJsonFile("phone_directory.json");
    }
}
