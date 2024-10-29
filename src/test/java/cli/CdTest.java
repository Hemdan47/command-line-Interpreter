package cli;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `cd` command.
 * This suite verifies that `cd` correctly handles:
 * - Excessive arguments.
 * - Missing arguments.
 * - Nonexistent paths.
 * - Non-directory paths.
 * - Correctly navigating between directories.
 */
class CdTest {
    Terminal t;

    /**
     * Constructor for CdTest.
     * Initializes a Terminal instance and sets the working directory
     * to a predefined test workspace for consistent test file operations.
     */
    public CdTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to verify that `cd` handles cases with too many arguments.
     * The command should return an error message without changing the current directory.
     */
    @Test
    public void tooManyArgsTest() {
        // Stores the current directory before executing the command.
        String beforeDir = t.getCurrentDir();

        // Executes `cd` with multiple arguments.
        String outputMessage = t.cd(new String[]{"arg1", "arg2"});
        String expectedMessage = "cd: too many arguments.\n";

        // Ensures the error message matches expectations and the directory remains unchanged.
        String afterDir = t.getCurrentDir();
        assertTrue(outputMessage.equals(expectedMessage));
        assertEquals(beforeDir, afterDir);
    }

    /**
     * Test to verify that `cd` correctly handles cases with no arguments.
     * `cd` should not change the directory and return an empty output.
     */
    @Test
    public void NoArgsTest() {
        // Stores the current directory before executing the command.
        String beforeDir = t.getCurrentDir();

        // Executes `cd` with no arguments.
        String outputMessage = t.cd(new String[]{});

        // Ensures no output message and verifies the directory is unchanged.
        String afterDir = t.getCurrentDir();
        assertTrue(outputMessage.isEmpty());
        assertEquals(beforeDir, afterDir);
    }

    /**
     * Test to check `cd` response when attempting to navigate to a non-existent path.
     * An error message should be returned, and the directory should remain the same.
     */
    @Test
    public void fileDoesntExistTest() {
        // Stores the current directory before executing the command.
        String beforeDir = t.getCurrentDir();

        // Executes `cd` with a non-existent path.
        String outputMessage = t.cd(new String[]{"nonExistingFile"});
        String expectedMessage = "cd: no such file or directory: nonExistingFile\n";

        // Validates the error message and ensures the directory remains unchanged.
        String afterDir = t.getCurrentDir();
        assertEquals(outputMessage, expectedMessage);
        assertEquals(beforeDir, afterDir);
    }

    /**
     * Test to ensure `cd` command correctly identifies and prevents navigation to non-directory files.
     * An error message should be returned, and the current directory should remain unchanged.
     */
    @Test
    public void aPathIsNotADirectoryTest() {
        // Stores the current directory before executing the command.
        String beforeDir = t.getCurrentDir();

        // Creates a file that will be treated as a non-directory.
        t.touch(new String[]{"notADirectoryTestFile.txt"});

        // Attempts to `cd` into the non-directory file.
        String outputMessage = t.cd(new String[]{"notADirectoryTestFile.txt"});
        String expectedMessage = "cd: not a directory: notADirectoryTestFile.txt\n";

        // Validates the error message and ensures the directory remains unchanged.
        String afterDir = t.getCurrentDir();
        assertEquals(outputMessage, expectedMessage);
        assertEquals(beforeDir, afterDir);

        // Cleans up by removing the test file.
        t.rm(new String[]{"notADirectoryTestFile.txt"});
    }

    /**
     * Test to validate `cd` command's ability to navigate to a directory.
     * The command should correctly handle directory navigation with paths that include references
     * such as `..` (parent directory) and `.` (current directory).
     */
    @Test
    public void changingDirectoryTest(){
        // Stores the current directory before executing the command.
        String beforeDir = t.getCurrentDir();

        // Creates a new directory for testing.
        t.mkdir(new String[]{"parentDirInCdTestCase"});

        // Changes directory using a path that includes parent (`..`) and current (`.`) directory references.
        t.cd(new String[]{beforeDir + "\\parentDirInCdTestCase\\..\\.\\parentDirInCdTestCase"});
        String expectedCurrentDirectory = beforeDir + "\\parentDirInCdTestCase";

        // Validates that the current directory matches the expected path.
        String afterDir = t.getCurrentDir();
        assertEquals(expectedCurrentDirectory , afterDir);

        // Cleans up by removing the test directory.
        t.rmdir(new String[]{"..\\parentDirInCdTestCase"});
    }
}
