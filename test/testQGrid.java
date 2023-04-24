import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testQGrid {

    // Ensure that the grid is created with the correct size.
    @Test
    public void testSize() {
        QGrid grid = new QGrid();

        assertEquals(grid.getGrid().size(), 8);
        assertEquals(grid.getGrid().get(0).size(), 5);
    }

    // Checks that the nodes properly link to one another.
    @Test
    public void testNodeLinks() {
        QGrid grid = new QGrid();
        grid.setNode(2, 2, 0, ":)");

        QNode<String> node = grid.getNode(2, 2);
        assertEquals(node.getLeft().getValue(), grid.getNode(1, 2).getValue());
        assertEquals(node.getRight().getValue(), grid.getNode(3, 2).getValue());
        assertEquals(node.getUpper().getValue(), grid.getNode(2, 1).getValue());
        assertEquals(node.getLower().getValue(), grid.getNode(2, 3).getValue());
    }
}