package com.hiro.to.Do;

import javax.swing.*;
import java.io.*;

public class Open_Apo {
    private DefaultListModel<String> model;

    public Open_Apo(DefaultListModel<String> model) {
        this.model = model;
        read_Apo();
    }

    private void read_Apo() {
        File file = new File("./toDo.txt");

        if (!file.exists()) {
            System.out.println("ファイルが見つかりません: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("読み込み: " + line);
                model.addElement(line); // Add each line to the model
            }
        } catch (IOException error) {
            System.err.println("ファイル読み込みエラー: " + error.getMessage());
            error.printStackTrace();
        }
    }
}

