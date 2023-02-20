package manager;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, Exception e) {
        super(message);
    }

    public ManagerSaveException(String message, IOException e) {
        super(message);
    }
}