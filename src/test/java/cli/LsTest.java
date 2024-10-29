package cli;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `ls` command.
 * This suite tests the `ls` command functionality, verifying its handling of:
 * - Excessive arguments.
 * - Regular listing of files and directories.
 * - Reverse order listing.
 * - Listing with hidden files.
 * - Invalid argument handling.
 */
class LsTest {
    Terminal t;

    /**
     * Constructor for LsTest.
     * Initializes a Terminal instance and sets the working directory
     * to a test workspace for consistency in file and directory listings.
     */
    public LsTest(){
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace\\lsTestFolder";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to verify `ls` behavior when called with too many arguments.
     * An error message should be returned, indicating an argument limit.
     */
    @Test
    public void tooManyArgs(){
        // Executes `ls` with two arguments, exceeding the supported number.
        String outputMessage = t.ls(new String[]{"arg1" , "arg2"});
        String expectedMessage = "ls: too many arguments\n";

        // Validates that the output message matches the expected error message.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test for regular `ls` functionality.
     * Verifies that `ls` lists files and directories in the expected order.
     */
    @Test
    public void lsTest(){
        // Executes `ls` with no arguments to get a standard directory listing.
        String outputMessage = t.ls(new String[]{});
        String expectedMessage = "file1.txt folder1 folder2 \n";

        // Checks that the output matches the expected listing order.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test for reverse listing (`ls -r`).
     * Verifies that `ls` lists files and directories in reverse order.
     */
    @Test
    public void lsRTest(){
        // Executes `ls` with the `-r` option for reverse order listing.
        String outputMessage = t.ls(new String[]{"-r"});
        String expectedMessage = "folder2 folder1 file1.txt \n";

        // Confirms that the output matches the expected reverse order.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test for listing with hidden files (`ls -a`).
     * Ensures that `ls` includes hidden files in the directory listing when `-a` is specified.
     */
    @Test
    public void lsATest(){
        // Executes `ls` with the `-a` option to include hidden files in the listing.
        String outputMessage = t.ls(new String[]{"-a"});
        String expectedMessage = ".hiddenFile1.txt file1.txt folder1 folder2 \n";

        // Validates that the output includes hidden files as expected.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to verify `ls` handling of invalid arguments.
     * Ensures that an error message is returned for unsupported arguments.
     */
    @Test
    public void invalidArgumentTest(){
        // Executes `ls` with an unsupported argument.
        String outputMessage = t.ls(new String[]{"-wrongArg"});
        String expectedMessage = "ls: invalid argument (currently only supports ls, ls -r, ls -a)\n";

        // Checks that the output matches the expected invalid argument message.
        assertTrue(outputMessage.equals(expectedMessage));
    }
}
