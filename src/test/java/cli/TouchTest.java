package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the behavior of the Terminal's `touch` command.
 * This class contains test cases to ensure the `touch` command handles missing
 * file operands, creates non-existing files, and handles attempts to recreate existing files correctly.
 */
class TouchTest {

    Terminal t;

    public TouchTest(){
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Tests the behavior of `touch` when no file operand is provided.
     * Ensures that the file count in the `test_workspace` directory remains unchanged.
     */
    @Test
    public void missingFileOperandTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        t.touch(new String[]{}); // Attempt to touch without a filename
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount); // File count should remain the same
    }

    /**
     * Tests the creation of a new file using the `touch` command.
     * Verifies that the file count increases by one after creating a non-existing file.
     */
    @Test
    public void creatingNonExistingFileTest() {
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        t.touch(new String[]{"temporaryFileForTestingPurpose"});
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount - 1); // File count should increase by 1
        t.rm(new String[]{"temporaryFileForTestingPurpose"}); // Clean up the created file
    }

    /**
     * Tests the behavior of `touch` when attempting to create an already existing file.
     * Verifies that the file count in the `test_workspace` directory remains unchanged.
     */
    @Test
    public void creatingAnExistingFileTest() {
        t.touch(new String[]{"temporaryFileForTestingPurpose"}); // Create the file initially
        int beforeFilesCount = new File(t.getCurrentDir()).list().length;
        t.touch(new String[]{"temporaryFileForTestingPurpose"}); // Attempt to create it again
        int afterFilesCount = new File(t.getCurrentDir()).list().length;
        assertEquals(beforeFilesCount, afterFilesCount); // File count should not change
        t.rm(new String[]{"temporaryFileForTestingPurpose"}); // Clean up the created file
    }
}
