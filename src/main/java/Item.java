import java.util.*;
import java.util.regex.*;

public class Item {

    private String name = "";
    private String type = "";
    private String expiration = "";
    private final Map<Double, Integer> priceFreq;


    public Item(String name, Double price, String type, String expiration) {
        this.name = name;
        this.type = type;
        this.expiration = expiration;
        this.priceFreq = new HashMap<>();
        this.priceFreq.put(price, 1);
    }

    public Item(Item oldItem, Double newPrice) {
        this.name = oldItem.getName();
        this.expiration = oldItem.getExpiration();
        this.priceFreq = new HashMap<>();
        for (Map.Entry<Double, Integer> entry : oldItem.getPrices()) {
            this.priceFreq.put(entry.getKey(), entry.getValue());
        }
        this.priceFreq.compute(newPrice, (k,v) -> (v==null)?1:v+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getName(), item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    // Getters
    public String getName() { return name; }
    public String getExpiration() { return expiration; }
    public Set<Map.Entry<Double, Integer>> getPrices() { return priceFreq.entrySet(); }

    public String printName() {
        int sum = 0;
        for (Map.Entry<Double, Integer> entry : this.priceFreq.entrySet()) {
            sum += entry.getValue();
        }
        return "name:    " + this.name + " \t\t seen: " + sum + " times\n============= \t \t =============";
    }

    public String getOccurences() {
        String output = "";
        for (Map.Entry<Double, Integer> entry : this.priceFreq.entrySet()) {
            output += "\nPrice: 	 " + entry.getKey() + "		 seen: " + entry.getValue() + " times\n------------- \t \t -------------";
        }
        return output;
    }
}
