package models;

import org.apache.commons.io.*;
import org.junit.*;

import java.util.*;
import java.util.regex.*;

import static org.junit.Assert.*;

public class JerksonHandlerTest {

    private String input;
    private JerksonHandler handler;

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        input = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("RawData.txt")));
        handler = new JerksonHandler();
    }

    @Test
    public void getParsed() {
        String replace = ":";
        String process = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016";
        String actual = handler.getParsed(Pattern.compile(";"), process, replace);
        String expected = "naMe:Milk:price:3.23:type:Food:expiration:1/25/2016";
        assertEquals(expected, actual);
    }

    @Test
    public void getObjects() {
        String[] objs = Pattern.compile("##").split(input);
        ArrayList<String> actual = handler.getObjects(objs);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("naMe:Milk:price:3.23:type:Food:expiration:1/25/2016");
        expected.add("naME:BreaD:price:1.23:type:Food:expiration:1/02/2016");
        expected.add("NAMe:BrEAD:price:1.23:type:Food:expiration:2/25/2016");
        expected.add("naMe:MiLK:price:3.23:type:Food:expiration:1/11/2016");
        expected.add("naMe:Cookies:price:2.25:type:Food:expiration:1/25/2016");
        expected.add("naMe:CoOkieS:price:2.25:type:Food:expiration:1/25/2016");
        expected.add("naMe:COokIes:price:2.25:type:Food:expiration:3/22/2016");
        expected.add("naMe:COOkieS:price:2.25:type:Food:expiration:1/25/2016");
        expected.add("NAME:MilK:price:3.23:type:Food:expiration:1/17/2016");
        expected.add("naMe:MilK:price:1.23:type:Food:expiration:4/25/2016");
        expected.add("naMe:apPles:price:0.25:type:Food:expiration:1/23/2016");
        expected.add("naMe:apPles:price:0.23:type:Food:expiration:5/02/2016");
        expected.add("NAMe:BrEAD:price:1.23:type:Food:expiration:1/25/2016");
        expected.add("naMe:price:3.23:type:Food:expiration:1/04/2016");
        expected.add("naMe:Milk:price:3.23:type:Food:expiration:1/25/2016");
        expected.add("naME:BreaD:price:1.23:type:Food:expiration:1/02/2016");
        expected.add("NAMe:BrEAD:price:1.23:type:Food:expiration:2/25/2016");
        expected.add("naMe:MiLK:priCe:type:Food:expiration:1/11/2016");
        expected.add("naMe:Cookies:price:2.25:type:Food:expiration:1/25/2016");
        expected.add("naMe:Co0kieS:pRice:2.25:type:Food:expiration:1/25/2016");
        expected.add("naMe:COokIes:price:2.25:type:Food:expiration:3/22/2016");
        expected.add("naMe:COOkieS:Price:2.25:type:Food:expiration:1/25/2016");
        expected.add("NAME:MilK:price:3.23:type:Food:expiration:1/17/2016");
        expected.add("naMe:MilK:priCe:type:Food:expiration:4/25/2016");
        expected.add("naMe:apPles:prIce:0.25:type:Food:expiration:1/23/2016");
        expected.add("naMe:apPles:pRice:0.23:type:Food:expiration:5/02/2016");
        expected.add("NAMe:BrEAD:price:1.23:type:Food:expiration:1/25/2016");
        expected.add("naMe:price:3.23:type:Food:expiration:1/04/2016");
        assertEquals(expected, actual);
    }

    @Test
    public void updateExisting() {
        Item init = new Item("milk", 10.5, "Food", "1/16/2016");
        Item existingUpdate = new Item(init, 20.0);
        Item additionalUpdate = new Item("milk", 10.5, "Food", "1/16/2016");
        handler.getFreq().put(init, 1);
        handler.updateExisting(handler.getKeyFromMap(init), existingUpdate, 20.0);
        handler.updateExisting(handler.getKeyFromMap(init), additionalUpdate, 10.5);
        int expectedSize = 1;
        int actualSize = handler.getFreq().size();
        Map<Double, Integer> expectedPriceMap = new HashMap<>();
        expectedPriceMap.put(10.5,2);
        expectedPriceMap.put(20.0, 1);
        Set<Map.Entry<Double, Integer>> expectedPrices = new HashSet<>(expectedPriceMap.entrySet());
        Set<Map.Entry<Double, Integer>> actualPrices = new HashSet<>();
        for (Item key : handler.getFreq().keySet()) {
            if (key.equals(init)) {
                actualPrices = key.getPrices();
                break;
            }
        }
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedPrices, actualPrices);
    }

    @Test
    public void parse() {
        handler.parse(input);
        Integer expectedSize = 4;
        Integer actualSize = handler.getFreq().size();
        Set<Map.Entry<Item, Integer>> entryset = handler.getFreq().entrySet();
        for (Map.Entry<Item, Integer> entry : entryset) {
            if (entry.getKey().getName().equals("milk")) { assertEquals(new Integer(6), entry.getValue()); }
            else if (entry.getKey().getName().equals("apples")) { assertEquals(new Integer(4), entry.getValue()); }
            else if (entry.getKey().getName().equals("cookies")) { assertEquals(new Integer(8), entry.getValue()); }
            else if (entry.getKey().getName().equals("bread")) { assertEquals(new Integer(6), entry.getValue()); }
        }
        assertEquals(expectedSize, actualSize);
    }
}