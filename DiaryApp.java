import java.io.*;
import java.util.*;

class DiaryEntry implements Serializable {
    private String title;
    private String content;
    private Date date;

    public DiaryEntry(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + title + "\n" + content + "\n";
    }
}

public class DiaryApp {
    private static final String FILE_NAME = "diary.dat";
    private List<DiaryEntry> diaryEntries;

    public DiaryApp() {
        diaryEntries = loadDiary();
    }

    public void addEntry(String title, String content) {
        diaryEntries.add(new DiaryEntry(title, content));
        saveDiary();
    }

    public void listEntries() {
        for (DiaryEntry entry : diaryEntries) {
            System.out.println(entry);
        }
    }

    public void searchEntry(String keyword) {
        for (DiaryEntry entry : diaryEntries) {
            if (entry.getTitle().contains(keyword)) {
                System.out.println(entry);
            }
        }
    }

    public void deleteEntry(String title) {
        diaryEntries.removeIf(entry -> entry.getTitle().equals(title));
        saveDiary();
    }

    private void saveDiary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(diaryEntries);
        } catch (IOException e) {
            System.out.println("Error saving diary: " + e.getMessage());
        }
    }

    private List<DiaryEntry> loadDiary() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<DiaryEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DiaryApp diary = new DiaryApp();

        while (true) {
            System.out.println("1. Add Entry  2. List Entries  3. Search  4. Delete Entry  5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter content (type 'END' on a new line to finish):");
                    StringBuilder contentBuilder = new StringBuilder();
                    String line;
                    while (!(line = scanner.nextLine()).equals("END")) {
                        contentBuilder.append(line).append("\n");
                    }
                    String content = contentBuilder.toString();
                    diary.addEntry(title, content);
                    break;
                case 2:
                    diary.listEntries();
                    break;
                case 3:
                    System.out.print("Search keyword: ");
                    String keyword = scanner.nextLine();
                    diary.searchEntry(keyword);
                    break;
                case 4:
                    System.out.print("Title to delete: ");
                    String delTitle = scanner.nextLine();
                    diary.deleteEntry(delTitle);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
