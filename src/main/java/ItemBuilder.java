import java.util.regex.*;

public class ItemBuilder {
    private String name;
    private Double price;
    private String type;
    private String expiration;

    public Item createItem() {
        return new Item(name, price, type, expiration);
    }

    public ItemBuilder setName(String name) {
        Pattern pat = Pattern.compile("[A-Z]");
        Matcher matchy = pat.matcher(name);

        StringBuffer sb = new StringBuffer();
        while(matchy.find()){
            String match = matchy.group();
            if (match.length() == 1){
                matchy.appendReplacement(sb, match.toLowerCase());
            } else {
                matchy.appendReplacement(sb, Matcher.quoteReplacement(match));
            }
        }
        matchy.appendTail(sb);
        String replaced = sb.toString();
        pat = Pattern.compile("[0]");
        matchy = pat.matcher(replaced);
        sb = new StringBuffer();
        if (matchy.find()) {
            replaced = matchy.replaceAll("o");
        }
        this.name = replaced; return this;
    }
    public ItemBuilder setPrice(Double price) { this.price = price; return this; }
    public ItemBuilder setType(String type) { this.type = type; return this; }
    public ItemBuilder setExpiration(String expiration) { this.expiration = expiration; return this; }

}