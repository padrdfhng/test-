package com.j2fe.pricing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestCsvz
{
    public static final String SPLIT_CHAR = ",";
    
    public static void main(String[] args) throws FileNotFoundException
    {
        String rootDirectory = args[0];
        String outDir = args[1];
        String currentStock = "";
        Map<String, Double> lastRangeSum = new HashMap<>();
        int currentDay = 0;
        try
        {
            Map<String, String> writeMap = new HashMap<>();
            File[] files = new File(rootDirectory).listFiles();//E:\\Test
            for (File f : files)
            {
                if(f.isDirectory())
                {
                    continue;
                }
                currentDay++;
                try (BufferedReader reader = new BufferedReader(new FileReader(f));)
                {
                    StringBuilder builder = new StringBuilder();
                    int index = 0;
                    while ((currentStock = reader.readLine()) != null)
                    {
                        if (index == 0)
                        {
                            builder.append(currentStock + "," + "Range" + "," + "DailyRangeAverage" + '\n');
                        }
                        else
                        {
                            String[] stockDetail = currentStock.split(SPLIT_CHAR);
                            builder.append(stockDetail[0] + ",");
                            Double low = Double.valueOf(stockDetail[1]);
                            Double high = Double.valueOf(stockDetail[2]);

                            builder.append(low + ",");
                            builder.append(high + ",");

                            Double difference;
                            Double average;

                            difference = high - low;
                            if (lastRangeSum.get(stockDetail[0]) == null)
                            {
                                average = difference;
                                lastRangeSum.put(stockDetail[0], average);
                            }
                            else
                            {
                                Double lastRange = lastRangeSum.get(stockDetail[0]);
                                lastRange = lastRange + difference;
                                average = lastRange / currentDay;
                                lastRangeSum.put(stockDetail[0], lastRange);
                            }

                            builder.append(difference + ",");
                            builder.append(average);
                            builder.append('\n');
                        }
                        index++;

                    }
                    writeMap.put(f.getName() + "_Out", builder.toString());
                }
            }
            System.out.println(writeMap.toString());
            writeFile(writeMap,outDir);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void writeFile(Map<String, String> writeMap,String rootDirectory) throws FileNotFoundException
    {
        for (Entry<String, String> map : writeMap.entrySet())
        {
            String outName = map.getKey();
            String value = map.getValue();
            String fileName = rootDirectory + "\\" + outName + ".csv";
            try (PrintWriter writer = new PrintWriter(new File(fileName)))
            {
                writer.write(value);
            }
        }

    }
}
