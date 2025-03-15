import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File currentFile = new File("");
        String currentPath = currentFile.getAbsolutePath();
        //System.out.println("Current path: " + currentPath);

        Scanner s = new Scanner(System.in);
        
        System.out.print("Input the file that you need renamed: ");
        String oldName = s.nextLine();
        File originalFile = new File(currentPath + "\\" + oldName);
        if (originalFile.exists()){ 
            System.out.println(oldName + " found.");
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