package wtf.expensive;

public class Profile {

    private final String name;
    private final int uid;


    public Profile(String name, int uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public int getUID() {
        return this.uid;
    }

}