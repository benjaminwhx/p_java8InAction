package entity;

/**
 * User: 吴海旭
 * Date: 2016-11-16
 * Time: 下午1:06
 * 贸易员
 */
public class Trader {
    private final String name;
    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Trader:" + this.name + " in " + this.city;
    }
}

