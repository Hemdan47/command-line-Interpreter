package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `rm` command, which is responsible for removing files.
 * Each test case checks specific scenarios, including handling of invalid arguments and
 * edge cases like removing directories or non-existent files.
 */
class RmTest {

    Terminal t;

    /**
     * Constructor that initializes the Terminal instance and sets the working directory
     * to a predefined test workspace directory.
     */
    public RmTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to verify that `rm` throws an error when no arguments are provided.
     * Confirms that the file count does not change and checks the expected error message.
     */
    @Test
    public void tooFewArgumentsTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rm(new String[]{});
        String expectedMessage = "rm: too few arguments\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to check that `rm` throws an error when attempting to remove a directory.
     * Confirms that the file count does not change and checks the expected error message.
     */
    @Test
    public void removingADirectoryTest() {
        t.mkdir(new String[]{"removingADirectoryTestDirectory"});

        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rm(new String[]{"removingADirectoryTestDirectory"});
        String expectedMessage = "rm: cannot remove 'removingADirectoryTestDirectory': is a directory\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));

        // Cleanup: remove the created test directory
        t.rmdir(new String[]{"removingADirectoryTestDirectory"});
    }

    /**
     * Test to verify that `rm` throws an error when attempting to remove a non-existent file.
     * Confirms that the file count does not change and checks the expected error message.
     */
    @Test
    public void fileDoesNotExistTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rm(new String[]{"fileDoesNotExistTestFile.txt"});
        String expectedMessage = "rm: The system cannot find the file specified: 'fileDoesNotExistTestFile.txt'\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to verify the successful removal of a file using `rm`.
     * Checks that the file count decreases by one after deletion.
     */
    @Test
    public void removingAFileTest() {
        t.touch(new String[]{"removingAFileTestFile.txt"});

        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        t.rm(new String[]{"removingAFileTestFile.txt"});
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount + 1);
    }
}
