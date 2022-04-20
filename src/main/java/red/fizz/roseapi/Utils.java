package red.fizz.roseapi;

import static java.util.UUID.randomUUID;

public class Utils {
    public String createApiKey() {
        return randomUUID().toString();
    }
}
