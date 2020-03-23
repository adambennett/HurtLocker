package models;

import models.*;

import java.util.*;
import java.util.regex.*;

public class JerksonHandler {

    private final Map<Item, Integer> freq;
    private Integer errors;

    public JerksonHandler() {
        freq = new HashMap<>();
        errors = 0;
    }

    public String getParsed(Pattern pattern, String current, String replaceWith) {
        Matcher match = pattern.matcher(current);
        return match.replaceAll(replaceWith);
    }

    public ArrayList<String> getObjects(String[] initialParseArray) {
        String replace = ":";
        ArrayList<String> parsed = new ArrayList<>();
        for (String s : initialParseArray) {
            String curr = getParsed(Pattern.compile(":;"), s, replace);
            String currB = getParsed(Pattern.compile(";"), curr, replace);
            String currC = getParsed(Pattern.compile(":"), currB, replace);
            String currD = getParsed(Pattern.compile("@"), currC, replace);
            String currE = getParsed(Pattern.compile("!"), currD, replace);
            String currF = getParsed(Pattern.compile("\\*"), currE, replace);
            String currG = getParsed(Pattern.compile("%"), currF, replace);
            String currH = getParsed(Pattern.compile("\\^"), currG, replace);
            parsed.add(currH);
        }
        return parsed;
    }

    public void updateFreq(String nextObject) {
        String[] data = Pattern.compile(":").split(nextObject);
        if (data.length != 8) { this.errors++; }
        else {
            String name = data[1];
            String type = data[5];
            String exp = data[7];
            double price = 0.0;
            try { price = Double.parseDouble(data[3]); } catch (NumberFormatException ignored) {}
            Item next = new ItemBuilder().setName(name).setPrice(price).setType(type).setExpiration(exp).createItem();
            boolean found = false;
            for (Item key : this.freq.keySet()) {
                if (updateExisting(key, next, price)) {
                    found = true;
                    break;
                }
            }
            if (!found) { this.freq.put(next, 1); }
        }
    }

    public Boolean updateExisting(Item key, Item next, Double price) {
        if (key.equals(next)) {
            Item actualNext = new Item(key, price);
            int oldAmt = this.freq.get(next);
            this.freq.remove(next);
            this.freq.put(actualNext, oldAmt + 1);
            return true;
        }
        return false;
    }

    public void parse(String input) {
        String[] objs = Pattern.compile("##").split(input);
        ArrayList<String> parsed = getObjects(objs);
        this.errors = 0;
        for (String s : parsed) {
            updateFreq(s);
        }
    }

    public String getOutput(String input) {
        parse(input);
        String output = "";
        for (Map.Entry<Item, Integer> entry : this.freq.entrySet()) {
            output += entry.getKey().printName() + entry.getKey().getOccurences() + "\n";
        }

        if (this.errors>0) {
            output += "Errors         \t \t seen: " + this.errors + " times";
        }
        return output;
    }

}
