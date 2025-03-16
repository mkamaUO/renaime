package src.main.java.com.example;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.OptionsBuilder;

public class Main {
    public static void main(String[] args) {
        
        String host = "http://localhost:11434/";

        OllamaAPI ollamaAPI = new OllamaAPI(host);

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
            String prompt = "What would you rename this image file? Ensure that you only output the new filename. Make sure you DO NOT include the file extension at the end.";
            try {
                result = ollamaAPI.generateWithImageFiles(OllamaModelType.LLAVA_PHI3, prompt, List.of(new File(currentPath + oldName)), new OptionsBuilder().build());
            } catch (Exception e) {
                System.out.println("Something happened: " + e);
            }
            String[] fileType = oldName.split("[.]");
            System.out.println("This is the response: " + result.getResponse() + "." + fileType[fileType.length-1]);

            if (originalFile.renameTo(new File(currentPath + result + "." + fileType[fileType.length-1]))) { // missing try catch for when the new name already exists
                System.out.println("File renamed successfully.");
            } else {
                System.out.println("Failed to rename file.");
            }
        }
        else {
            System.out.println(currentPath + oldName + " not found.");
        }

        System.out.print("new name: "); // unnecessary when llm renames it
        String newName = s.nextLine();
        File renamedFile = new File(currentPath + "\\" + newName);

        if (originalFile.exists()){
            if (originalFile.renameTo(renamedFile)) { // missing try catch for when the new name already exists
                System.out.println("File renamed successfully.");
            } else {
                System.out.println("Failed to rename file.");
            }
        }
        else {
            System.out.println("File does not exist.");
        }
        
    }
}