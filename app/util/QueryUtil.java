package util;

public class QueryUtil {

    /**
     * Private constructor for the class to prevent instantiation.
     */
    private QueryUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This function builds a string to use in an sql query in a like clause. It places percentage signs on either
     * side of the given string parameter.
     * @param field     The string to be concatenated with percentage signs on either side of the field.
     * @return          A string containing the field wrapped in percentage signs.
     */
    public static String queryComparator(String field) {
        return "%" + field + "%";
    }
}
