import java.util.ArrayList;

public class QuadruplyLinkedGrid {
    private final int width = 5;
    private final int height = 8;

    private ArrayList<ArrayList<QuadruplyLinkedNode<String>>> grid;

    public QuadruplyLinkedGrid() {
        grid = new ArrayList<ArrayList<QuadruplyLinkedNode<String>>>();
        for(int y = 0; y < height; y++) {
            ArrayList<QuadruplyLinkedNode<String>> row = new ArrayList<QuadruplyLinkedNode<String>>();
            for(int x = 0; x < width; x++) {
                row.add(new QuadruplyLinkedNode<String>());
            }
            grid.add(row);
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                QuadruplyLinkedNode<String> node = grid.get(y).get(x);
                if(x != 0) {
                    node.linkLeft(grid.get(y).get(x-1));
                }
                if(y != 0) {
                    node.linkLower(grid.get(y-1).get(x));
                }
                if(x != width - 1) {
                    node.linkRight(grid.get(y).get(x+1));
                }
                if(y != height - 1) {
                    node.linkUpper(grid.get(y+1).get(x));
                }
            }
        }
    }

    public static void main(String[] args) {
        new QuadruplyLinkedGrid();
    }
}
