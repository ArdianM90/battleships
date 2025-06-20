package app.project.utils;

public class ValidationUtils {

    public static boolean nameIsValid(String name) {
        boolean isEmpty = name.trim().isEmpty();
        boolean hasForbiddenChars = name.trim().matches(".*[\\[\\];].*");
        return !isEmpty && !hasForbiddenChars;
    }

    public static boolean ipIsValid(String ip) {
        String trimmedIp = ip.trim();
        if (trimmedIp.isEmpty()) {
            return false;
        }
        if ("localhost".endsWith(trimmedIp)) {
            return true;
        }
        String ipRegex = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
        return trimmedIp.matches(ipRegex);
    }
}
