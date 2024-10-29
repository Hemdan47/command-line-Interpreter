package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `rmdir` command, which is responsible for removing directories.
 * Each test case checks specific scenarios and the expected error messages for invalid cases.
 */
class RmdirTest {
    Terminal t;

    /**
     * Constructor that initializes the Terminal instance and sets the working directory
     * to a predefined test workspace directory.
     */
    public RmdirTest() {
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to check that `rmdir` throws an error when no arguments are provided.
     * Confirms that the file count does not change and checks the expected error message.
     */
    @Test
    public void tooFewArgumentsTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rmdir(new String[]{});
        String expectedMessage = "rmdir: too few arguments\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to check that `rmdir` throws an error when attempting to delete a non-existing directory.
     * Confirms that the file count does not change and checks the expected error message.
     */
    @Test
    public void directoryDoesntExistTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rmdir(new String[]{"directoryDoesntExistTestFolder"});
        String expectedMessage = "rmdir: 'directoryDoesntExistTestFolder' does not exist.\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to check that `rmdir` throws an error when attempting to delete a non-empty directory.
     * The directory is populated with a test file, and the expected error message is verified.
     */
    @Test
    public void DirectoryIsNotEmptyTest() {
        t.mkdir(new String[]{"DirectoryIsNotEmptyTest"});
        t.cd(new String[]{"DirectoryIsNotEmptyTest"});
        t.touch(new String[]{"DirectoryIsNotEmptyTest.txt"});
        t.cd(new String[]{".."});

        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rmdir(new String[]{"DirectoryIsNotEmptyTest"});
        String expectedMessage = "rmdir: 'DirectoryIsNotEmptyTest' is not empty.\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));

        // Cleanup: remove the test file and directory
        t.cd(new String[]{"DirectoryIsNotEmptyTest"});
        t.rm(new String[]{"DirectoryIsNotEmptyTest.txt"});
        t.cd(new String[]{".."});
        t.rmdir(new String[]{"DirectoryIsNotEmptyTest"});
    }

    /**
     * Test to verify that `rmdir` throws an error when attempting to delete a file instead of a directory.
     * Confirms the file count does not change and checks the expected error message.
     */
    @Test
    public void fileIsNotADirectoryTest() {
        t.touch(new String[]{"fileIsNotADirectoryTestFile.txt"});

        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        String outputMessage = t.rmdir(new String[]{"fileIsNotADirectoryTestFile.txt"});
        String expectedMessage = "rmdir: 'fileIsNotADirectoryTestFile.txt' is not a directory.\n";
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount);
        assertTrue(outputMessage.equals(expectedMessage));

        // Cleanup: remove the test file
        t.rm(new String[]{"fileIsNotADirectoryTestFile.txt"});
    }

    /**
     * Test to verify the successful deletion of an empty directory using `rmdir`.
     * Checks that the file count decreases by one after deletion.
     */
    @Test
    public void deletingADirectoryTest() {
        t.mkdir(new String[]{"deletingADirectoryTestDirectory"});

        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        t.rmdir(new String[]{"deletingADirectoryTestDirectory"});
        int afterFilesCount = new File(t.getCurrentDir()).list().length;

        assertEquals(beforeFilesCount, afterFilesCount + 1);
    }
}
