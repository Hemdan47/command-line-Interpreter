package cli;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the Terminal's `pwd` command.
 * This test verifies that the `pwd` command accurately outputs the current directory path.
 */
class PwdTest {

    /**
     * Test to verify `pwd` command's output matches the expected absolute path.
     * The test changes the working directory to a specific workspace directory,
     * then confirms `pwd` returns the correct path.
     */
    @Test
    public void pwdTest() {
        // Initializes the Terminal and sets the working directory to a test workspace.
        Terminal t = new Terminal();
        String testWorkSpace = "src\\test\\test_workspace";
        t.cd(new String[]{testWorkSpace});

        // Gets the expected absolute path of the workspace directory.
        String curDirAbsolutePath = Path.of(testWorkSpace).toAbsolutePath().toString();

        // Verifies that `pwd` returns the correct absolute path of the current directory.
        assertEquals(curDirAbsolutePath, t.pwd());
    }
}
