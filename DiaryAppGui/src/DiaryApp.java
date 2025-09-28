import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class DiaryApp extends JFrame {

    private static final String APP_TITLE = "Diary App";
    private static final String DIARY_DIR = "diaries";
    private static final String IMAGE_DIR = "images";
    private static final String FILE_EXTENSION = ".txt";
    private static final String ENTRY_SEPARATOR = "\n--------------------\n";
    private static final String TIMESTAMP_PREFIX = "ENTRY_TIME: ";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JCalendar jCalendar;
    private JTextArea textArea;
    private JList<String> entryList;
    private DefaultListModel<String> listModel;
    private JTextField searchBar;
    private JLabel[] imageLabels = new JLabel[3];

    private AtomicReference<String> selectedEntryTimestamp = new AtomicReference<>(null);
    private String[] currentImageNames = new String[3];

    public DiaryApp() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // 少し縦長に
        setLocationRelativeTo(null);

        new File(DIARY_DIR).mkdirs();
        new File(IMAGE_DIR).mkdirs();

        initComponents();
        layoutComponents();
        addListeners();

        loadEntriesFromSelectedDate();
    }

    private void initComponents() {
        jCalendar = new JCalendar();

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        listModel = new DefaultListModel<>();
        entryList = new JList<>(listModel);

        searchBar = new JTextField(20);

        for (int i = 0; i < 3; i++) {
            imageLabels[i] = new JLabel("No Image", SwingConstants.CENTER);
            imageLabels[i].setPreferredSize(new Dimension(150, 150));
            imageLabels[i].setBorder(BorderFactory.createTitledBorder("添付画像 " + (i + 1) + " (150x150)"));
        }
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createTitledBorder("日記エントリ"));

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(jCalendar, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(entryList), BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createTitledBorder("日付とエントリ一覧"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton saveButton = new JButton("保存 (Save)");
        saveButton.addActionListener(e -> saveNewEntry());
        JButton importButton = new JButton("画像インポート (Import)");
        importButton.addActionListener(e -> importImage());
        JButton loadButton = new JButton("再読込 (Load)");
        loadButton.addActionListener(e -> loadSelectedEntryContent());
        buttonPanel.add(saveButton);
        buttonPanel.add(importButton);
        buttonPanel.add(loadButton);

        // 検索バー
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("検索:"));
        searchPanel.add(searchBar);

        // 画像表示をフレームの一番下に配置
        JPanel imagePanel = new JPanel(new GridLayout(1, 3, 5, 5)); // 1行3列のグリッド
        for (int i = 0; i < 3; i++) {
            imagePanel.add(imageLabels[i]);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.SOUTH);
        add(imagePanel, BorderLayout.PAGE_END); // ★ MainFrame下部に配置
    }

    private void addListeners() {
        jCalendar.addPropertyChangeListener("calendar", e -> loadEntriesFromSelectedDate());
        entryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && entryList.getSelectedIndex() != -1) {
                selectedEntryTimestamp.set(entryList.getSelectedValue());
                loadSelectedEntryContent();
            }
        });
    }

    // --- Import ---
    private void importImage() {
        int availableIndex = -1;
        for (int i = 0; i < 3; i++) {
            if (currentImageNames[i] == null) {
                availableIndex = i;
                break;
            }
        }

        if (availableIndex == -1) {
            JOptionPane.showMessageDialog(this, "画像は3枚までです");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JPEG images", "jpg", "jpeg"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                String newName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + availableIndex + ".jpg";
                File destFile = new File(IMAGE_DIR, newName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath());
                currentImageNames[availableIndex] = newName;
                textArea.append("\n[IMAGE_" + availableIndex + ": " + newName + "]\n");
                showImage(destFile, availableIndex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "画像インポートエラー: " + ex.getMessage());
            }
        }
    }

    // --- Save ---
    private void saveNewEntry() {
        String content = textArea.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "内容を入力してください");
            return;
        }
        String dateString = DATE_FORMAT.format(jCalendar.getDate());
        String currentTimestamp = DATETIME_FORMAT.format(new Date());
        File diaryFile = new File(DIARY_DIR, dateString + FILE_EXTENSION);

        try (PrintWriter writer = new PrintWriter(new FileWriter(diaryFile, true))) {
            if (diaryFile.length() > 0) writer.print(ENTRY_SEPARATOR);
            writer.println(TIMESTAMP_PREFIX + currentTimestamp);
            writer.println(content);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "保存エラー: " + ex.getMessage());
        }
        loadEntriesFromSelectedDate();
        clearEntry();
    }

    // --- Load ---
    private void loadEntriesFromSelectedDate() {
        String dateString = DATE_FORMAT.format(jCalendar.getDate());
        File diaryFile = new File(DIARY_DIR, dateString + FILE_EXTENSION);
        listModel.clear();
        if (!diaryFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(diaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(TIMESTAMP_PREFIX)) {
                    listModel.addElement(line.substring(TIMESTAMP_PREFIX.length()).trim());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "読み込みエラー: " + ex.getMessage());
        }
    }

    private void loadSelectedEntryContent() {
        String timestamp = selectedEntryTimestamp.get();
        if (timestamp == null) return;
        String dateString = DATE_FORMAT.format(jCalendar.getDate());
        File diaryFile = new File(DIARY_DIR, dateString + FILE_EXTENSION);
        if (!diaryFile.exists()) return;

        // Clear previous images
        for (int i = 0; i < 3; i++) {
            imageLabels[i].setIcon(null);
            imageLabels[i].setText("No Image");
            currentImageNames[i] = null;
        }
        textArea.setText("");

        try {
            String wholeFileContent = new String(java.nio.file.Files.readAllBytes(diaryFile.toPath()), java.nio.charset.StandardCharsets.UTF_8);
            String[] entries = wholeFileContent.split(ENTRY_SEPARATOR);

            for (String entry : entries) {
                if (entry.trim().startsWith(TIMESTAMP_PREFIX + timestamp)) {
                    StringBuilder entryContent = new StringBuilder();
                    String[] lines = entry.trim().lines().toArray(String[]::new);

                    for (int i = 1; i < lines.length; i++) { // Skip the timestamp line
                        String line = lines[i];
                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[IMAGE_(d+):\s*(.*?)]");
                        java.util.regex.Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            int index = Integer.parseInt(matcher.group(1));
                            String imgName = matcher.group(2).trim();
                            if (index >= 0 && index < 3) {
                                currentImageNames[index] = imgName;
                                File imgFile = new File(IMAGE_DIR, imgName);
                                showImage(imgFile, index);
                            }
                        } else {
                            entryContent.append(line).append("\n");
                        }
                    }
                    textArea.setText(entryContent.toString().trim());
                    break; // Found the entry, no need to check others
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "読み込みエラー: " + ex.getMessage());
        }
    }

    // --- Show Image ---
    private void showImage(File file, int index) {
        if (file != null && file.exists()) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabels[index].setIcon(new ImageIcon(scaled));
            imageLabels[index].setText(null);
        } else {
            imageLabels[index].setText("No Image");
            imageLabels[index].setIcon(null);
        }
    }

    private void clearEntry() {
        textArea.setText("");
        for (int i = 0; i < 3; i++) {
            imageLabels[i].setIcon(null);
            imageLabels[i].setText("No Image");
            currentImageNames[i] = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiaryApp().setVisible(true));
    }
}
