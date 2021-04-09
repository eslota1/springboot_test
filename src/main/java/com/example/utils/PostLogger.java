package com.example.utils;

import java.io.*;
import java.util.regex.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Iterator;

public class PostLogger {
    private String filePath;

    public PostLogger(String path){
        this.filePath = path;
        File logFile = new File(path);
        try {
            if (logFile.createNewFile()) {
                System.out.println("Log file created: " + logFile.getName());
            } else {
                System.out.println("Log file already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String line){
        Path p = Paths.get(filePath);
        String s = System.lineSeparator() + line;
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p,
                StandardOpenOption.APPEND))) {
            out.write(s.getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String readHistory(){
        String content = "";
        ArrayDeque<String> stack = new ArrayDeque<String>();
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            //read the file and add only not an empty string to the stack
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().length() > 0)
                    stack.push(line.trim());
                line = reader.readLine();
            }
            reader.close();
            fr.close();
            //add posts to the content in inverse order
            Iterator<String> it = stack.iterator();
            while (it.hasNext()) {
                content += it.next() +"\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        return content;
    }

    public boolean deleteInputText (String delete){
        boolean d = false;
        String fPath = filePath+".temp";
        try{
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            File file1 = new File(fPath);
            FileWriter fw = new FileWriter(file1);
            BufferedWriter writer = new BufferedWriter(fw);
            String line = reader.readLine();

            while (line != null){
                String deleteTrimLn = delete.trim();

                if (line.trim().length() > 0 && !deleteTrimLn.equals(line.trim())){
                    writer.write(line+System.lineSeparator());
                }
                else
                    d = true;

                line = reader.readLine();
            }
            reader.close();
            writer.close();
            fr.close();
            fw.close();
            return (d && file1.renameTo(file));
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String searchInputText (String searchT) {
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            int linecount = 0;

            while ((line= reader.readLine()) != null) {
                linecount++;
                int indexfound;
                indexfound = line.indexOf(searchT);

                if (indexfound > -1){
                    sb.append(line);
                    sb.append("\n");
                }
            }

            reader.close();
            fr.close();
            return sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "Nothing found";
    }
}
