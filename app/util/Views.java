package util;

public class Views {
    public interface Public {
    }

    public interface Owner extends Public {
    }

    public interface Admin extends Owner {
    }
}