package models;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ItemTest {

    private static Item item;

    @Before
    public void setUp() {
        item = new Item("milk", 10.5, "Food", "1/16/2016");
    }

    @Test
    public void testEquals() {
        Item compare = new Item("milk", 100.0, "Liquor", "Never");
        assertEquals(item, compare);
    }

    @Test
    public void testGetName() {
        String expected = "milk";
        String actual = item.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetExpiration() {
        String expected = "1/16/2016";
        String actual = item.getExpiration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPrices() {
        Map<Double, Integer> expect = new HashMap<>();
        expect.put(10.5, 1);
        Set<Map.Entry<Double, Integer>> expected = expect.entrySet();
        Set<Map.Entry<Double, Integer>> actual = item.getPrices();
        assertEquals(expected, actual);
    }

    @Test
    public void testItemBuilder() {
        Item compare = new ItemBuilder().setName("milk").setPrice(10.5).setType("Food").setExpiration("1/16/2016").createItem();
        assertEquals(compare, item);
    }
}