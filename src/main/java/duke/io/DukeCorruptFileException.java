package duke.io;

import duke.error.DukeException;

/**
 * The DukeException to be thrown when a the file at the given file path is not formatted
 * in the way the method reading the file expects it to be formatted.
 */
class DukeCorruptFileException extends DukeException {

    /**
     * Constructs the DukeException thrown when a file is not formatted in the way the method that
     * throws this expects.
     *
     * @param filePath The file path of the badly formatted file
     */
    DukeCorruptFileException(String filePath) {
        super("The file at ", filePath, " is formatted incorrectly.");
        assert filePath != null;
    }
}