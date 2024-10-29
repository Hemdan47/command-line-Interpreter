package cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Terminal {
    private Parser parser;
    private Path currentDir;


    /**
     * Constructs a Terminal instance.
     * Initializes the parser and sets the current directory to the user's working directory.
     */
    public Terminal(){
        parser = new Parser();
        currentDir = Path.of(System.getProperty("user.dir"));
    }

    /**
     * Prints the current working directory to the console.
     */
    public String pwd(){
        System.out.println(getCurrentDir());
        return getCurrentDir();
    }

    /**
     * Changes the current directory based on the provided argument.
     *
     * @param args the directory path to change to. If no arguments are provided, does nothing.
     */
    public String cd(String args[]){
        StringBuilder output = new StringBuilder();
        if(args.length == 0 ){ // No arguments case
            return output.toString();
        }
        else if(args.length > 1){
            output.append("cd: too many arguments.\n");
            System.out.print(output.toString());
            return output.toString();
        }
        else{
            Path targetPath = Path.of(args[0]);
            if(!targetPath.isAbsolute()){ // If path is relative, resolve it against the current directory
                targetPath = currentDir.resolve(targetPath);
            }
            File file = new File(targetPath.toAbsolutePath().toString());
            if(!file.exists()){ // If directory does not exist
                output.append("cd: no such file or directory: " + targetPath.getFileName() + "\n");
                System.out.print(output.toString());
                return output.toString();
            }
            if(!file.isDirectory()){ // If path is not a directory
                output.append("cd: not a directory: " + targetPath.getFileName() + "\n");
                System.out.print(output.toString());
                return output.toString();
            }
            currentDir = targetPath.normalize(); // Set the new current directory
        }
        return output.toString();
    }

    /**
     * Lists the contents of the current directory with optional flags.
     * -a: lists all files, including hidden files.
     * -r: lists files in reverse order.
     *
     * @param args optional flags for displaying files. If no flags are given, lists visible files only.
     */
    public String ls(String args[]){
        File cur = new File(getCurrentDir());
        String[] list = cur.list();
        StringBuilder output = new StringBuilder();
        if(args.length > 1){
            output.append("ls: too many arguments\n");
        }
        else if(args.length == 0){ // Default listing excluding hidden files
            Arrays.stream(list).filter(e -> !e.startsWith("."))
                    .forEach((e) -> output.append(e + " "));
            output.append("\n");

        }
        else if(args[0].equals("-a")){ // List all files, including hidden
            Arrays.stream(list).forEach(e -> output.append(e + " "));
            output.append("\n");
        }
        else if(args[0].equals("-r")){ // List files in reverse order, excluding hidden
            Collections.reverse(Arrays.asList(list));
            Arrays.stream(list).filter(e -> !e.startsWith("."))
                    .forEach((e) -> output.append(e + " "));
            output.append("\n");
        }
        else{ // Invalid argument case
            output.append("ls: invalid argument (currently only supports ls, ls -r, ls -a)\n");
        }
        System.out.print(output);
        return output.toString();
    }

    /**
     * Creates directories based on the provided arguments.
     *
     * @param args the names or paths of directories to create.
     */
    public String mkdir(String args[]){
        StringBuilder output = new StringBuilder();
        if(args.length < 1){
            output.append("mkdir: too few arguments\n");
            System.out.print(output.toString());
            return output.toString();
        }
        Arrays.stream(args).forEach((e) -> {
            Path cur = Path.of(e);
            if(!cur.isAbsolute()){ // Resolve relative paths against current directory
                cur = currentDir.resolve(cur);
            }

            if(Files.exists(cur)){ // Directory already exists case
                output.append("mkdir: A subdirectory or file already exists: '" + cur.getFileName() + "'\n");
            }
            else{
                try {
                    Files.createDirectories(cur); // Attempt to create the directory
                }
                catch (IOException ex) {
                    output.append("mkdir: An error occurred, can't create the directory: '" + cur.getFileName() + "'\n");
                }
            }
        });
        System.out.print(output.toString());
        return output.toString();
    }

    /**
     * Removes directories if they are empty.
     *
     * @param args the names or paths of directories to remove.
     */
    public String rmdir(String[] args){
        StringBuilder output = new StringBuilder();
        if(args.length < 1){
            output.append("rmdir: too few arguments\n");
            System.out.print(output.toString());
            return output.toString();
        }

        Arrays.stream(args).forEach((e) -> {
            Path cur = Path.of(e);
            if(!cur.isAbsolute()){ // Resolve relative paths against current directory
                cur = currentDir.resolve(cur);
            }

            if(Files.exists(cur)){
                if(!Files.isDirectory(cur)){ // If not a directory, output an error
                    output.append("rmdir: '" + cur.getFileName() + "' is not a directory.\n");
                }
                else{
                    try {
                        if(Files.list(cur).findAny().isPresent()){ // Check if directory is not empty
                            output.append("rmdir: '" + cur.getFileName() + "' is not empty.\n");
                        }
                        else{
                            Files.delete(cur); // Delete if empty
                        }
                    }
                    catch (IOException ex){
                        output.append("rmdir: An error occurred while removing directory '" + cur.getFileName() + "'\n");
                    }
                }
            }
            else{ // Directory does not exist case
                output.append("rmdir: '" + cur.getFileName() + "' does not exist.\n");
            }
        });
        System.out.print(output.toString());
        return output.toString();
    }

    /**
     * Creates new files for each argument provided.
     *
     * @param args the names or paths of files to create.
     */
    public void touch(String[] args){
        if(args.length < 1){ // Error for missing file operand
            System.out.println("touch: missing file operand");
            return;
        }

        Arrays.stream(args).forEach((e) -> {
            Path cur = Path.of(e);
            if (!cur.isAbsolute()) { // Resolve relative paths against current directory
                cur = currentDir.resolve(cur);
            }

            if (!Files.exists(cur)) { // Create file if it does not exist
                try {
                    Files.createFile(cur);
                }
                catch (IOException ex) {
                    System.out.println("touch: An error occurred while creating file '" + cur.getFileName() + "'");
                }
            }
        });
    }

    /**
     * Moves or renames a file or directory.
     *
     * @param args the source path and destination path for the move operation.
     */
    public String mv(String[] args){
        StringBuilder output = new StringBuilder();
        if(args.length == 0){ // Error for missing file operand
            output.append("mv: missing file operand\n");
        }
        else if(args.length == 1){ // Error for missing destination operand
            output.append("mv: missing destination file operand after '" + args[0] + "'\n");
        }
        else if(args.length == 2) {
            Path sourcePath = Path.of(args[0]);
            Path destPath = Path.of(args[1]);
            if (!sourcePath.isAbsolute()) {
                sourcePath = currentDir.resolve(sourcePath);
            }
            if (!destPath.isAbsolute()) {
                destPath = currentDir.resolve(destPath);
            }

            if(Files.exists(sourcePath)){
                File file = new File(sourcePath.toAbsolutePath().toString());

                if(Files.isDirectory(destPath)){
                    file.renameTo(new File(destPath.resolve(sourcePath.getFileName()).toAbsolutePath().toString()));
                }
                else{
                    file.renameTo(new File(destPath.toAbsolutePath().toString()));
                }
            }
            else{
                output.append("mv: cannot stat '" + sourcePath.getFileName() + "': No such file or directory\n");
            }


        }
        else{
            output.append("mv: too many arguments\n");
        }
        System.out.print(output.toString());
        return output.toString();
    }

    /**
     * Removes specified files if they are regular files, not directories.
     *
     * @param args the names or paths of files to delete.
     */
    public String rm(String[] args){
        StringBuilder output = new StringBuilder();
        if(args.length < 1){
            output.append("rm: too few arguments\n");
            return output.toString();
        }

        Arrays.stream(args).forEach((e) -> {
            Path cur = Path.of(e);
            if(!cur.isAbsolute()){
                cur = currentDir.resolve(cur);
            }

            try {
                if(Files.exists(cur)) {
                    if(Files.isRegularFile(cur)) { // Delete if a regular file
                        Files.delete(cur);
                    }
                    else { // Error if trying to delete a directory
                        output.append("rm: cannot remove '" + cur.getFileName() + "': is a directory\n");
                    }
                }
                else { // Error if file does not exist
                    output.append("rm: The system cannot find the file specified: '" + cur.getFileName() + "'\n");
                }
            }
            catch (IOException ex) {
                output.append("rm: An error occurred while trying to delete '" + cur.getFileName() + "'\n");
            }
        });
        System.out.print(output.toString());
        return output.toString();
    }

    /**
     * Reads and outputs the contents of the specified files.
     *
     * @param args the names or paths of files to read.
     * @return the contents of the files as a single string.
     */
    public String cat(String[] args){
        StringBuilder output = new StringBuilder();
        if(args.length == 0){ // Error for missing arguments
            output.append("cat: Invalid number of arguments\n");
            return output.toString();
        }
        Arrays.stream(args).forEach((e) -> {
            Path cur = Path.of(e);
            if(!cur.isAbsolute()){
                cur = currentDir.resolve(cur);
            }
            File file = new File(cur.toAbsolutePath().toString());
            if(file.exists()){
                if(file.isDirectory()){ // Error if argument is a directory
                    output.append("cat: " + file.getName() + ": Is a directory\n");
                }
                else{
                    try {
                        output.append(Files.readString(cur)); // Append file contents to output
                    } catch (IOException ex) {
                        output.append("cat: An error occurred, can't read the file: '" + file.getName() + "'\n");
                    }
                }
            }
            else{
                output.append("cat: " + file.getName() + ": No such file or directory\n");
            }
        });
        System.out.println(output.toString());
        return output.toString();
    }



    /**
     * Prints the list of supported commands and their descriptions.
     */
    private void help() {
        System.out.println("1.help     -> prints the list of supported commands");
        System.out.println("2.pwd      -> prints the current working directory");
        System.out.println("3.cd       -> changes the current working directory");
        System.out.println("4.ls       -> lists the contents of the current directory");
        System.out.println("5.ls -a    -> lists all contents even entries starting with .(hidden files)");
        System.out.println("6.ls -r    -> lists the contents of the current directory in reverse order");
        System.out.println("7.mkdir    -> creates a new directory");
        System.out.println("8.rmdir    -> removes an empty directory");
        System.out.println("9.touch    -> creates a new file");
        System.out.println("10.mv      -> command is used to move or rename files and directories from one location to another in a file system.");
        System.out.println("11.rm      -> removes a file");
        System.out.println("12.cat     -> prints the contents of a file");
        System.out.println("13.exit    -> exits the terminal");
    }

    /**
     * Exits the terminal program.
     */
    private void exit(){
        System.out.println("exiting...");
        System.exit(0);
    }

    /**
     * Retrieves the current working directory as a string.
     *
     * @return the absolute path of the current working directory.
     */
    public String getCurrentDir() {
        return currentDir.toAbsolutePath().toString();
    }



    /**
     * Prints the prompt of the terminal, which is the current directory
     */
    private void showPrompt() {
        System.out.print(getCurrentDir() + " > ");
    }


    /**
     * Simple function takes the command and choose the proper function
     */
    private void execute(String command , String[] args){

        if(command.equals("help")){
            help();
        }
        else if(command.equals("pwd")){
            pwd();
        }
        else if(command.equals("cd")){
            cd(args);
        }
        else if(command.equals("ls")){
            ls(args);
        }
        else if(command.equals("mkdir")){
            mkdir(args);
        }
        else if(command.equals("rmdir")){
            rmdir(args);
        }
        else if(command.equals("touch")){
            touch(args);
        }
        else if(command.equals("mv")){
            mv(args);
        }
        else if(command.equals("rm")){
            rm(args);
        }
        else if(command.equals("cat")){
            cat(args);
        }
        else if(command.equals("exit")){
            exit();
        }
        else{
            System.out.println("'" + command + "' is not recognized as an internal or external command");
        }


    }


    /**
     * Runs the terminal interface until the user exits
     */
    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showPrompt();
            String command = scanner.nextLine();
            if (parser.parse(command)) {
                String parsedCommand = parser.getCommand();
                String[] parsedArgs = parser.getArguments();
                execute(parsedCommand , parsedArgs);
            }
        }
    }
}
