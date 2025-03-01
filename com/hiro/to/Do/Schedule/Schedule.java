package com.hiro.to.Do;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Schedule{
    private static final String[] TIME_SLOTS = {
        "09:00", "10:00", "11:00", "12:00", "13:00", 
        "14:00", "15:00", "16:00", "17:00", "18:00"
    };
    private static final Map<String, String> schedule = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== スケジュール管理 ===");
            System.out.println("1. 時間一覧の表示");
            System.out.println("2. 予定を書き込む");
            System.out.println("3. 予定を表示する");
            System.out.println("4. 終了");
            System.out.print("選択してください: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 改行を消費

            switch (choice) {
                case 1 -> displayTimeSlots();
                case 2 -> addSchedule();
                case 3 -> displaySchedule();
                case 4 -> {
                    System.out.println("終了します。");
                    scanner.close();
                    return;
                }
                default -> System.out.println("無効な選択です。もう一度選択してください。");
            }
        }
    }

    /** 時間一覧を表示 */
    private static void displayTimeSlots() {
        System.out.println("\n=== 時間の一覧 ===");
        for (String time : TIME_SLOTS) {
            System.out.println(time);
        }
    }

    /** 予定を書き込む */
    private static void addSchedule() {
        System.out.print("\n予定を追加する時間を選択（例: 10:00）: ");
        String time = scanner.nextLine();

        if (!isValidTime(time)) {
            System.out.println("無効な時間です。もう一度やり直してください。");
            return;
        }

        System.out.print("予定を入力してください: ");
        String event = scanner.nextLine();

        schedule.put(time, event);
        System.out.println(time + " に「" + event + "」を追加しました！");
    }

    /** 予定を表示する */
    private static void displaySchedule() {
        System.out.println("\n=== 予定一覧 ===");
        if (schedule.isEmpty()) {
            System.out.println("予定はありません。");
            return;
        }
        for (String time : TIME_SLOTS) {
            String event = schedule.getOrDefault(time, "（予定なし）");
            System.out.println(time + " : " + event);
        }
    }

    /** 入力が有効な時間かチェック */
    private static boolean isValidTime(String time) {
        for (String validTime : TIME_SLOTS) {
            if (validTime.equals(time)) return true;
        }
        return false;
    }
}
