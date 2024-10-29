package cli;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `cat` command.
 * This test suite includes cases to verify that the `cat` command:
 * - Handles an incorrect number of arguments.
 * - Returns appropriate messages when files do not exist.
 * - Distinguishes between files and directories.
 * - Correctly concatenates the contents of multiple files.
 */
class CatTest {
    Terminal t;

    /**
     * Constructor for CatTest.
     * Initializes a Terminal instance and sets the current working directory
     * to a test workspace for file operation consistency.
     */
    public CatTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to validate `cat` command behavior when called with no arguments.
     * The `cat` command is expected to fail with an "Invalid number of arguments" message.
     */
    @Test
    public void invalidNumberOfArgumentsTest(){
        // Executes the `cat` command with an empty argument list.
        String outputMessage = t.cat(new String[]{});
        String expectedMessage = "cat: Invalid number of arguments\n";

        // Verifies that the actual output matches the expected error message.
        assertEquals(outputMessage , expectedMessage);
    }

    /**
     * Test to verify that `cat` correctly handles the case when a file does not exist.
     * It should return a message indicating the file was not found.
     */
    @Test
    public void fileDoesNotExistTest(){
        // Executes `cat` with a non-existent file.
        String outputMessage = t.cat(new String[]{"fileDoesNotExistTestFile.txt"});
        String expectedMessage = "cat: fileDoesNotExistTestFile.txt: No such file or directory\n";

        // Verifies that the actual output matches the expected error message.
        assertEquals(outputMessage , expectedMessage);
    }

    /**
     * Test to ensure `cat` command identifies and handles directories as arguments.
     * The command should indicate that the given argument is a directory, not a file.
     */
    @Test
    public void fileIsADirectoryTest(){
        // Creates a directory for the test case.
        t.mkdir(new String[]{"fileIsADirectoryTestDirectory"});

        // Executes `cat` with a directory name.
        String outputMessage = t.cat(new String[]{"fileIsADirectoryTestDirectory"});
        String expectedMessage = "cat: fileIsADirectoryTestDirectory: Is a directory\n";

        // Validates the output message.
        assertEquals(outputMessage , expectedMessage);

        // Cleans up by removing the test directory.
        t.rmdir(new String[]{"fileIsADirectoryTestDirectory"});
    }

    /**
     * Test to check `cat` functionality for reading and concatenating content from multiple files.
     * This test reads the content of two files and validates that `cat` combines them as expected.
     */
    @Test
    public void catFilesTest(){
        try {
            // Reads contents of two test files from the current directory.
            String firstFileText = Files.readString(Path.of(t.getCurrentDir() + "\\catTest1.txt").toAbsolutePath());
            String secondFileText = Files.readString(Path.of(t.getCurrentDir() + "\\catTest2.txt").toAbsolutePath());

            // Concatenates the expected output manually.
            String expectedOutput = firstFileText + secondFileText;

            // Executes `cat` on the two test files and captures the output.
            String actualOutput = t.cat(new String[]{"catTest1.txt" , "catTest2.txt"});

            // Asserts that the actual output from `cat` matches the manually concatenated content.
            assertTrue(actualOutput.equals(expectedOutput));
        } catch (IOException e) {
            // If an exception is caught, the test fails.
            assertTrue(false);
        }
    }
}
