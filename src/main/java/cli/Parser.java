package cli;

import java.util.Arrays;

/**
 * The Parser class processes input commands from the command line.
 */
public class Parser {
    private String command;
    private String[] arguments;

    /**
     * Parses the input line into a command and its arguments.
     *
     * @param line The input line containing the command and its arguments.
     */
    public boolean parse(String line) {
        String[] tmp = line.split("\\s+");
        command = tmp[0];
        arguments = new String[tmp.length - 1];
        System.arraycopy(tmp, 1, arguments, 0, tmp.length - 1);
        return true;
    }

    /**
     * Returns the parsed command.
     *
     * @return The command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns a copy of the parsed arguments.
     *
     * @return The arguments.
     */
    public String[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }
}
