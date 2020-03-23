import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        return IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("RawData.txt")));
    }

    public static void main(String[] args) throws Exception{
        String input = (new Main()).readRawDataToString();

        Map<Item, Integer> freq = new HashMap<>();
        Pattern EoL = Pattern.compile("##");
        Pattern separatorA = Pattern.compile(":;");
        Pattern separatorB = Pattern.compile(";");
        Pattern separatorC = Pattern.compile(":");
        Pattern separatorD = Pattern.compile("@");
        Pattern separatorE = Pattern.compile("!");
        Pattern separatorF = Pattern.compile("\\*");
        Pattern separatorG = Pattern.compile("%");
        Pattern separatorH = Pattern.compile("\\^");
        Pattern finSep = Pattern.compile(":");
        String[] objs = EoL.split(input);
        String replace = ":";
        ArrayList<String> parsed = new ArrayList<>();
        for (String s : objs) {
            Matcher match = separatorA.matcher(s);
            String curr = match.replaceAll(replace);

            Matcher matchB = separatorB.matcher(curr);
            String currB = matchB.replaceAll(replace);

            Matcher matchC = separatorC.matcher(currB);
            String currC = matchC.replaceAll(replace);

            Matcher matchD = separatorD.matcher(currC);
            String currD = matchD.replaceAll(replace);

            Matcher matchE = separatorE.matcher(currD);
            String currE = matchE.replaceAll(replace);

            Matcher matchF = separatorF.matcher(currE);
            String currF = matchF.replaceAll(replace);

            Matcher matchG = separatorG.matcher(currF);
            String currG = matchG.replaceAll(replace);

            Matcher matchH = separatorH.matcher(currG);
            String currH = matchH.replaceAll(replace);

            parsed.add(currH);
        }

        int errors = 0;
        for (String s : parsed) {
            String[] data = finSep.split(s);
            if (data.length != 8) {
                errors++;
            } else {
                String name = data[1];
                String type = data[5];
                String exp = data[7];
                double price = 0.0;
                try { price = Double.parseDouble(data[3]); } catch (NumberFormatException ignored) {}
                Item next = new ItemBuilder()
                        .setName(name)
                        .setPrice(price)
                        .setType(type)
                        .setExpiration(exp)
                        .createItem();
                boolean found = false;
                for (Item key : freq.keySet()) {
                    if (key.equals(next)) {
                        Item actualNext = new Item(key, price);
                        int oldAmt = freq.get(next);
                        freq.remove(next);
                        freq.put(actualNext, oldAmt + 1);
                        //freq.compute(actualNext, (k,v) -> (v==null)?1:v+1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    freq.put(next, 1);
                }
            }
        }

        for (Map.Entry<Item, Integer> entry : freq.entrySet()) {
            System.out.println(entry.getKey().printName() + entry.getKey().getOccurences() + "\n");
        }

        System.out.println("Errors         \t \t seen: " + errors + " times");
    }
}
