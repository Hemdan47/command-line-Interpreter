package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class provides unit tests for appending text to a file using the Terminal class.
 * It tests two cases:
 * 1. Appending to a non-existing file, which should create the file and add the text.
 * 2. Appending to an existing file, which should add text to the end of the existing content.
 */
class AppendToAFileTest {
    Terminal t;

    /**
     * Constructor initializes the Terminal object and sets the working directory
     * to a test workspace folder.
     */
    public AppendToAFileTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test for appending text to a non-existing file. This should create the file
     * with the specified text, and the file count in the directory should increase by one.
     */
    @Test
    public void appendToANonExistingFile() {
        // Count of files in the directory before creating a new file
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Text to be appended
        String textToAppend = "testing the appendToANonExistingFile functionality\n";

        // Append text to a new file
        t.appendToAFile(new String[]{textToAppend, "appendToANonExistingFile.txt"});

        // Count of files in the directory after appending
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        // Verify the content of the created file
        String textAppended = "";
        try {
            textAppended = Files.readString(Path.of(t.getCurrentDir() + "\\appendToANonExistingFile.txt").toAbsolutePath());
        } catch (IOException e) {
            // Fail the test if an IOException occurs
            assertTrue(false);
        }

        // Assert the file count increased by 1 and the content matches
        assertEquals(beforeFilesCount, afterFilesCount - 1);
        assertTrue(textToAppend.equals(textAppended));

        // Cleanup: Remove the created file after test
        t.rm(new String[]{"appendToANonExistingFile.txt"});
    }

    /**
     * Test for appending text to an existing file. The text should be added
     * to the existing content without increasing the file count in the directory.
     */
    @Test
    public void appendToAnExistingFile() {
        // Initial content to be written to the file before appending
        String textInTheFileBeforeAppending = "the text before appending";
        t.writeToAFile(new String[]{textInTheFileBeforeAppending, "appendToAnExistingFile.txt"});

        // Count of files in the directory before appending
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Text to append
        String textToAppend = "testing the appendToAnExistingFile functionality\n";
        t.appendToAFile(new String[]{textToAppend, "appendToAnExistingFile.txt"});

        // Count of files in the directory after appending
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        // Verify the content after appending
        String textPlusAppendedText = "";
        try {
            textPlusAppendedText = Files.readString(Path.of(t.getCurrentDir() + "\\appendToAnExistingFile.txt").toAbsolutePath());
        } catch (IOException e) {
            // Fail the test if an IOException occurs
            assertTrue(false);
        }

        // Assert the file count remains the same and content is correctly appended
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(textPlusAppendedText.equals(textInTheFileBeforeAppending + textToAppend));

        // Cleanup: Remove the file after test
        t.rm(new String[]{"appendToAnExistingFile.txt"});
    }
}
