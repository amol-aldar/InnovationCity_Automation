package org.rakdao.utils;

import java.io.*;

public class CounterUtil {
    private static final String COUNTER_FILE = "counter.txt";
    private static final int START_COUNT = 1000;

    public static synchronized int getNextCount() {
        int count = START_COUNT;

        File file = new File(COUNTER_FILE);

        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    count = Integer.parseInt(line.trim());
                }
                reader.close();
            }

            // Write the next count back to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.valueOf(count + 1));
            writer.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return count;
    }
}

