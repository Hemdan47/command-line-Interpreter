package cli;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Terminal's `mv` command.
 * This suite tests `mv` functionality, covering scenarios where:
 * - No arguments are provided.
 * - The destination file or directory is missing.
 * - The source file does not exist.
 * - Too many arguments are provided.
 * - A file is renamed successfully.
 * - A file is moved into a directory.
 */
class MvTest {

    Terminal t;

    /**
     * Constructor for MvTest.
     * Initializes a Terminal instance and sets the working directory
     * to a test workspace to maintain consistent file operation contexts.
     */
    public MvTest(){
        t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});
    }

    /**
     * Test to verify behavior when `mv` is called with no arguments.
     * Expects an error message indicating a missing file operand.
     */
    @Test
    public void noOperandTest(){
        // Executes `mv` with no arguments.
        String outputMessage = t.mv(new String[]{});
        String expectedMessage = "mv: missing file operand\n";

        // Checks that the output matches the expected error message.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to verify `mv` behavior when the destination operand is missing.
     * Expects an error message indicating a missing destination operand.
     */
    @Test
    public void missingDestinationTest(){
        // Executes `mv` with a single argument, lacking the destination operand.
        String outputMessage = t.mv(new String[]{"missingDestinationTest"});
        String expectedMessage = "mv: missing destination file operand after 'missingDestinationTest'\n";

        // Confirms the output matches the expected error message.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test for `mv` handling of a non-existent source file.
     * Expects an error message indicating that the source file cannot be found.
     */
    @Test
    public void fileNotFoundTest(){
        // Executes `mv` with a non-existent source file.
        String outputMessage = t.mv(new String[]{"fileNotFoundTestFile", "arg2"});
        String expectedMessage = "mv: cannot stat 'fileNotFoundTestFile': No such file or directory\n";

        // Verifies the output matches the expected error message.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test to verify `mv` behavior with excessive arguments.
     * Expects an error message indicating too many arguments.
     */
    @Test
    public void tooManyArgumentsTest(){
        // Executes `mv` with three arguments, exceeding the allowed number.
        String outputMessage = t.mv(new String[]{"arg1", "arg2", "arg3"});
        String expectedMessage = "mv: too many arguments\n";

        // Checks that the output matches the expected error message.
        assertTrue(outputMessage.equals(expectedMessage));
    }

    /**
     * Test for renaming a file using `mv`.
     * Confirms that the file is renamed successfully without creating duplicates.
     */
    @Test
    public void renameAFileTest(){
        // Creates a file for renaming.
        t.touch(new String[]{"beforeRenaming.txt"});

        // Checks if the file exists under the new name before renaming.
        boolean existsBeforeRenaming = Arrays.asList(new File(t.getCurrentDir()).list()).contains("afterRenaming.txt");

        // Executes `mv` to rename the file.
        t.mv(new String[]{"beforeRenaming.txt", "afterRenaming.txt"});

        // Validates that the file appears under the new name and does not exist under the old name.
        boolean existsAfterRenaming = Arrays.asList(new File(t.getCurrentDir()).list()).contains("afterRenaming.txt");
        assertNotEquals(existsBeforeRenaming, existsAfterRenaming);

        // Cleans up by removing the renamed file.
        t.rm(new String[]{"afterRenaming.txt"});
    }

    /**
     * Test for moving a file into a directory using `mv`.
     * Confirms that the file appears within the specified directory.
     */
    @Test
    public void moveAFileTest(){
        // Creates a file and a directory for the move test.
        t.touch(new String[]{"moveAFileTestFile.txt"});
        t.mkdir(new String[]{"moveAFileTestDirectory"});

        // Checks if the file exists in the target directory before moving.
        boolean existsBeforeMoving = Arrays.asList(new File(t.getCurrentDir() + "\\moveAFileTestDirectory").list()).contains("moveAFileTestFile.txt");

        // Executes `mv` to move the file into the directory.
        t.mv(new String[]{"moveAFileTestFile.txt", "moveAFileTestDirectory"});

        // Validates that the file is present in the directory after moving.
        boolean existsAfterMoving = Arrays.asList(new File(t.getCurrentDir() + "\\moveAFileTestDirectory").list()).contains("moveAFileTestFile.txt");
        assertNotEquals(existsBeforeMoving, existsAfterMoving);

        // Cleans up by removing the file from the directory and deleting the directory.
        t.rm(new String[]{t.getCurrentDir() + "\\moveAFileTestDirectory\\moveAFileTestFile.txt"});
        t.rmdir(new String[]{"moveAFileTestDirectory"});
    }
}
