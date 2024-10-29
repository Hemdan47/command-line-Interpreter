package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `mkdir` command.
 * This suite verifies `mkdir` functionality, covering cases where:
 * - Too few arguments are provided.
 * - An attempt is made to create an already existing directory.
 * - A new directory is created successfully.
 */
class MkdirTest {
    Terminal t;

    /**
     * Constructor for MkdirTest.
     * Initializes a Terminal instance and sets the working directory
     * to a test workspace to maintain file operation consistency.
     */
    public MkdirTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to check `mkdir` command's behavior when called with no arguments.
     * `mkdir` should return an error message and avoid creating any directories.
     */
    @Test
    public void tooFewArgumentsTest(){
        // Stores the file count before executing the command.
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Executes `mkdir` with no arguments.
        String outputMessage = t.mkdir(new String[]{});
        String expectedMessage = "mkdir: too few arguments\n";

        // Verifies the error message and ensures file count remains unchanged.
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to verify `mkdir` handling of an already existing directory.
     * The command should return an error message, and no additional directory should be created.
     */
    @Test
    public void directoryAlreadyExistsTest(){
        // Creates a directory to simulate an existing one.
        t.mkdir(new String[]{"directoryAlreadyExistsTestDirectory"});

        // Stores the file count before attempting to create the same directory.
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Executes `mkdir` with a name that already exists as a directory.
        String outputMessage = t.mkdir(new String[]{"directoryAlreadyExistsTestDirectory"});
        String expectedMessage = "mkdir: A subdirectory or file already exists: 'directoryAlreadyExistsTestDirectory'\n";

        // Validates the error message and ensures the file count remains unchanged.
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));

        // Cleans up by removing the test directory.
        t.rmdir(new String[]{"directoryAlreadyExistsTestDirectory"});
    }

    /**
     * Test for creating a new, non-existing directory.
     * Ensures that the `mkdir` command successfully creates the directory,
     * and the file count increases by one.
     */
    @Test
    public void creatingNonExistingDirectoryTest() {
        // Stores the file count before creating a new directory.
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;

        // Executes `mkdir` with a new directory name.
        t.mkdir(new String[]{"creatingNonExistingDirectoryTestDirectory"});

        // Validates that the file count has increased by one after directory creation.
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount - 1);

        // Cleans up by removing the created directory.
        t.rmdir(new String[]{"creatingNonExistingDirectoryTestDirectory"});
    }
}
