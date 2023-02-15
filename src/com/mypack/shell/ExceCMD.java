package com.mypack.shell;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExceCMD {

  public static void executeCommandsFromFile(String filePath) throws IOException, InterruptedException {
   
       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String commandShell = isWindows() ? "cmd /c" : "/bin/bash -c";
            String command;
            while ((command = br.readLine()) != null) {
               
                handleDirectory(command); // handle directory creation or deletion before executing the command
                handleFiles(command);
                // execute the command
                Process process = Runtime.getRuntime().exec(commandShell + command);

                // read the output of the command
                try (BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = output.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                CheckFiles(command);
            }
        } catch (IOException e) {
            System.err.println("Error reading commands from file: " + e.getMessage());
        }

  }

  private static void handleDirectory(String command) {
    if (command.startsWith("mkdir") || command.startsWith("md")) {
      String[] args = command.split(" ");
      if (args.length > 1) {
        String dirName = args[1];
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
          System.out.println("The directory : " + dirName +" to be deleted as it already exists");
          if (dir.delete()) {
            System.out.println("Deleted existing directory: " + dirName);
          } else {
            System.out.println("Failed to delete existing directory: " + dirName);
          }
        }
      }
    } else if (command.startsWith("rmdir") || command.startsWith("rd")) {
      String[] args = command.split(" ");
      if (args.length > 1) {
        String dirName = args[1];
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            System.out.println("Proceeding to delete existing directory: " + dirName);
        } else {
          System.out.println("Cannot delete directory: " + dirName + " as it does not exist");
        }
      }
    }
  }

    private static void handleFiles(String command) {


      if (command.startsWith("ren") || command.startsWith("rename")) {
      String[] args = command.split(" ");
      if (args.length == 3) {
        String oldName = args[1];
        String newName = args[2];

        File oldFile = new File(oldName);
        File newFile = new File(newName);

        if (oldFile.exists()) {
          if (newFile.exists()) {
            System.out.println("Cannot rename " + oldName + " to " + newName + " because a file with the new name already exists");
          } else {
              System.out.println("Proceeding to Rename " + oldName + " to " + newName);
            } 
          }
         else {
          System.out.println("Cannot rename " + oldName + " to " + newName + " because the old file does not exist");
        }
      }
    }
    else if (command.startsWith("del") || command.startsWith("erase") || command.startsWith("rm")) {


            String[] args = command.split(" ");
            if (args.length > 1) {
                String fileName = args[1];
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    System.out.println("Proceeding to delete existing file: " + fileName);
                  }
                else {
                   System.out.println("Cannot delete file: " + fileName + " as it does not exist");
                }

          }
      }
      else if (command.startsWith("copy") || command.startsWith("cp") ) {
          String[] args = command.split(" ");
          if (args.length == 3) {
            String sourceName = args[1];
            String destName = args[2];
            
            File sourceFile = new File(sourceName);
            File destFile = new File(destName);

            if (sourceFile.exists() && sourceFile.isFile()) {
              if (destFile.exists()) {
                if (destFile.isDirectory()) {
                  
                } else {
                  System.out.println("Cannot copy " + sourceName + " to " + destName + " because a file with the destination name already exists");
                  return;
                }
              }

              System.out.println("Proceeding to Copy " + sourceName + " to " + destFile.getAbsolutePath());
             
            } else {
              System.out.println("Cannot copy " + sourceName + " to " + destName + " because the source file does not exist");
            }
          }
      }
  }


  private static void CheckFiles(String command) {
    if (command.startsWith("ren") || command.startsWith("rename")) {
      String[] args = command.split(" ");
      if (args.length == 3) {
        String oldName = args[1];
        String newName = args[2];

        File oldFile = new File(oldName);
        File newFile = new File(newName);

        if (oldFile.exists()) {
          
         System.out.println("file rename failed " + oldName + " to " + newName +" as file still has oldfile name");

      }
      else {
          if (newFile.exists()) {
          System.out.println("file rename successful " + oldName + " to " + newName );
        }
        else{
          System.out.println("file rename failed " + oldName + " to " + newName );
        }
      }
    }
  }
    else if (command.startsWith("del") || command.startsWith("erase") || command.startsWith("rm")) {


            String[] args = command.split(" ");
            if (args.length > 1) {
                String fileName = args[1];
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    System.out.println("file deletion failed for file: " + fileName);
                  }
                else {
                   System.out.println("file deletion successful for file: " + fileName);
                }

          }
      }
      else if (command.startsWith("copy") || command.startsWith("cp"))
      {
        String[] args = command.split(" ");
        if (args.length == 3) {
            String source = args[1];
            String destination = args[2];
            File sourceFile = new File(source);
            File destinationFile = new File(destination);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (destinationFile.exists()) {
                    System.out.println("File copy  successful: " + destination);
                } else {
                    System.out.println("File copy failed: " + source + " to " + destination);
                }
            } 
        }
    }
}


  private static void printOutput(Process process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
    reader.close();
  }

  private static boolean isWindows() {
    String os = System.getProperty("os.name");
    return os.contains("Windows");
  }

  public static void main(String[] args) {
    try {
      executeCommandsFromFile("commands.txt");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}

