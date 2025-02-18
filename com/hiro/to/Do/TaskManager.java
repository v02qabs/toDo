package com.hiro.to.Do;

import javax.swing.DefaultListModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;
    private DefaultListModel<String> model;
    private static final String FILE_NAME = "toDo.txt"; // 保存ファイル

    private TaskManager() {
        model = new DefaultListModel<>();
        loadTasksFromFile(); // 初期化時にファイルからデータ読み込み
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public DefaultListModel<String> getModel() {
        return model;
    }

    // 🔥 toDo.txt から全予定を読み込む（起動時のみ）
    private void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // ファイルがない場合は何もしない

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                model.addElement(line); // JList に読み込み
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 予定を追加（リストとファイル両方）
    public void addItem(String item) {
        if (!item.isEmpty() && !model.contains(item)) { // 重複防止
            model.addElement(item);
            writeItemToFile(item); // ファイルに書き込む
        }
    }

    // ✅ ファイルに新しい予定を1行追加
    private void writeItemToFile(String item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(item);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 選択された項目を削除（リスト＆ファイル）
    public void removeItem(int index) {
        if (index >= 0 && index < model.size()) {
            String item = model.get(index);
            model.remove(index); // JList から削除
            removeItemFromFile(item); // ファイルから削除
        }
    }

    // ✅ ファイル (toDo.txt) から該当する項目を削除
    private void removeItemFromFile(String item) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // ファイルがない場合は何もしない

        List<String> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(item)) { // 削除対象と一致しないものだけ保持
                    tasks.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 🔄 上書き保存（削除後のリストでファイルを更新）
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

