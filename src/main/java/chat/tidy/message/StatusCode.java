package chat.tidy.message;

import com.google.gson.annotations.Expose;

public enum StatusCode {

    BLACKLIST(200),
    SPAM(201),
    PRIVACY(202),
    TIME(203),
    LENGTH(204),
    UPPERCASE(205);

    @Expose
    private final int id;

    StatusCode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "id=" + id +
                ", name=" + name() +
                '}';
    }
}