import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class heapsortTest {

    @Test
    public void testSort() {
        heapsort sorter = new heapsort();
        int[] input = {12, 11, 13, 5, 6, 7};
        int[] expected = {5, 6, 7, 11, 12, 13};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortEmptyArray() {
        heapsort sorter = new heapsort();
        int[] input = {};
        int[] expected = {};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortSingleElement() {
        heapsort sorter = new heapsort();
        int[] input = {42};
        int[] expected = {42};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortAlreadySortedArray() {
        heapsort sorter = new heapsort();
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortReversedArray() {
        heapsort sorter = new heapsort();
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }
    @Test
    public void testSortArrayWithNegativeValues() {
        heapsort sorter = new heapsort();
        int[] input = {2,-3,1,0,-1};
        int[] expected = {-3, -1, 0, 1, 2};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }
}
