import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    // TODO - add test cases for constructor, add, remove, removeAny, value,
    // hasKey, and size
    @Test
    public final void testMapConstructor() {
        Map<String, String> test = this.constructorTest();
        Map<String, String> expect = this.constructorRef();
        assertEquals(expect, test);
    }

    @Test
    public final void add1() {
        Map<String, String> test = this.createFromArgsTest();
        Map<String, String> expect = this.createFromArgsRef("1", "2");
        test.add("1", "2");
        assertEquals(expect, test);
    }

    @Test
    public final void add2() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        Map<String, String> expect = this.createFromArgsRef("1", "2", "3", "4");
        test.add("3", "4");
        assertEquals(expect, test);
    }

    @Test
    public final void remove1() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        Map<String, String> expect = this.createFromArgsRef();
        test.remove("1");
        assertEquals(expect, test);
    }

    @Test
    public final void remove2() {
        Map<String, String> test = this.createFromArgsTest("1", "2", "3", "4");
        Map<String, String> expect = this.createFromArgsRef("3", "4");
        test.remove("1");
        assertEquals(expect, test);
    }

    @Test
    public final void removeAny() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        Map<String, String> expect = this.createFromArgsRef();
        test.removeAny();
        assertEquals(expect, test);
    }

    @Test
    public final void size1() {
        Map<String, String> test = this.createFromArgsTest();
        int size = 0;
        assertEquals(test.size(), size);
    }

    @Test
    public final void size2() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        int size = 1;
        assertEquals(test.size(), size);
    }

    @Test
    public final void size3() {
        Map<String, String> test = this.createFromArgsTest("1", "2", "3", "4");
        int size = 2;
        assertEquals(test.size(), size);
    }

    @Test
    public final void value1() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        Map<String, String> expect = this.createFromArgsRef("1", "2");
        String value = test.value("1");
        assertEquals(value, expect.value("1"));
    }

    @Test
    public final void value2() {
        Map<String, String> test = this.createFromArgsTest("1", "2", "3", "4");
        Map<String, String> expect = this.createFromArgsRef("3", "4");
        String value = test.value("3");
        assertEquals(value, expect.value("3"));
    }

    @Test
    public final void hasKey1() {
        Map<String, String> test = this.createFromArgsTest();
        assertEquals(false, test.hasKey(""));
    }

    @Test
    public final void hasKey2() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        assertTrue(test.hasKey("1"));
    }

    @Test
    public final void hasKey3() {
        Map<String, String> test = this.createFromArgsTest("1", "2", "3", "4");
        assertTrue(test.hasKey("3"));
    }

    @Test
    public final void hasKey4() {
        Map<String, String> test = this.createFromArgsTest("1", "2", "3", "4");
        assertEquals(false, test.hasKey("5"));
    }
}
