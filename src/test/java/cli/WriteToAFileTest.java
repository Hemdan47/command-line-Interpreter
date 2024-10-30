package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class provides unit tests for writing text to a file using the Terminal class.
 * It includes tests for two scenarios:
 * 1. Writing to a non-existing file, which should create the file and write the specified content.
 * 2. Writing to an existing file, which should overwrite the existing content.
 */
class WriteToAFileTest {
    Terminal t;

    /**
     * Constructor initializes the Terminal object and sets the working directory
     * to a test workspace folder.
     */
    public WriteToAFileTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test for writing text to a non-existing file. This should create the file
     * with the specified text, and the file count in the directory should increase by one.
     */
    @Test
    public void writeToANonExistingFile() {
        // Count of files in the directory before creating a new file
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Text to be written to the new file
        String textToWrite = "testing the writeToANonExistingFile functionality\n";

        // Write text to a new file
        t.writeToAFile(new String[]{textToWrite, "writeToANonExistingFile.txt"});

        // Count of files in the directory after writing
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        // Verify the content of the created file
        String textWrote = "";
        try {
            textWrote = Files.readString(Path.of(t.getCurrentDir() + "\\writeToANonExistingFile.txt").toAbsolutePath());
        } catch (IOException e) {
            // Fail the test if an IOException occurs
            assertTrue(false);
        }

        // Assert the file count increased by 1 and the content matches
        assertEquals(beforeFilesCount, afterFilesCount - 1);
        assertTrue(textToWrite.equals(textWrote));

        // Cleanup: Remove the created file after test
        t.rm(new String[]{"writeToANonExistingFile.txt"});
    }

    /**
     * Test for writing text to an existing file. This should overwrite the existing content
     * without increasing the file count in the directory.
     */
    @Test
    public void writeToAnExistingFile() {
        // Initial content to write to the file before overwriting
        t.writeToAFile(new String[]{"the text before rewriting", "writeToAnExistingFile.txt"});

        // Count of files in the directory before overwriting
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Text to overwrite the existing file content
        String textToWrite = "testing the writeToAnExistingFile functionality\n";
        t.writeToAFile(new String[]{textToWrite, "writeToAnExistingFile.txt"});

        // Count of files in the directory after overwriting
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        // Verify the content after overwriting
        String textWrote = "";
        try {
            textWrote = Files.readString(Path.of(t.getCurrentDir() + "\\writeToAnExistingFile.txt").toAbsolutePath());
        } catch (IOException e) {
            // Fail the test if an IOException occurs
            assertTrue(false);
        }

        // Assert the file count remains the same and content is correctly overwritten
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(textToWrite.equals(textWrote));

        // Cleanup: Remove the file after test
        t.rm(new String[]{"writeToAnExistingFile.txt"});
    }
}
