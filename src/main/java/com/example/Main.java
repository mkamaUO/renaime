package src.main.java.com.example;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;

public class Main {
    public static void main(String[] args) {
        
        String host = "http://localhost:11434/";

        OllamaAPI ollamaAPI = new OllamaAPI(host);

        ollamaAPI.setRequestTimeoutSeconds(30);
        ollamaAPI.setVerbose(false);
        Options options = new OptionsBuilder().setStop(".jpg").setStop(".png").build();

        OllamaResult result = new OllamaResult("image", 0, 0);

        File currentFile = new File("");
        String currentPath = currentFile.getAbsolutePath() + "\\";
        //System.out.println("Current path: " + currentPath);

        Scanner s = new Scanner(System.in);
        
        System.out.print("Input the file that you need renamed: ");
        String oldName = s.nextLine();
        File originalFile = new File(currentPath + oldName);
        if (originalFile.exists()){ 
            System.out.println(oldName + " found.");
            String prompt = "What would you name this image? Ensure that you only output the one new name. Try and keep the names short, between 1 and 5 words. Never include .jpg and .png in any outputs. Stick to only characters from the English alphabet, underscores, and hyphens.";
            try {
                result = ollamaAPI.generateWithImageFiles(OllamaModelType.LLAVA_PHI3, prompt, List.of(new File(currentPath + oldName)), new OptionsBuilder().build());
            } catch (Exception e) {
                System.out.println("Something happened: " + e);
            }
                                                        
            String[] fileType = oldName.split("[.]");
            System.out.println("This is the response: " + result.getResponse() + "." + fileType[fileType.length-1]);

            if (originalFile.renameTo(new File(currentPath + result.getResponse().replaceAll("\\s","_") + "." + fileType[fileType.length-1]))) { // missing try catch for when the new name already exists
                System.out.println("File renamed successfully.");
            } else {
                System.out.println("Failed to automatically rename file.\n");
                System.out.print("Type in the new name: "); // unnecessary when llm renames it
                String newName = s.nextLine();
                File renamedFile = new File(currentPath + "\\" + newName);
                if (originalFile.renameTo(renamedFile)) { // missing try catch for when the new name already exists
                    System.out.println("File renamed successfully.");
                } else {
                    System.out.println("Failed to rename file.");
                }
            }
        }
        else {
            System.out.println(currentPath + oldName + " not found.");
        }        
    }
}