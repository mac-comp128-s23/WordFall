import java.util.ArrayList;

public class QuadruplyLinkedGrid {
    private final int width = 5;
    private final int height = 8;

    private ArrayList<ArrayList<QuadruplyLinkedNode<String>>> grid;

    /*Constructor for QuadruplyLinkedNode grid. First nested loop instantiates each ArrayList within each Arraylist.
     * Second nested loop links all the nodes up.
     */
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
                //System.out.print(node);
            }
            //System.out.println();
        }        
    }
        //Will need a big method that checks if a word was formed in each side, making sure its checking for larger words first.
    public void wordChecker(QuadruplyLinkedNode<String> current){
        
    }

    public static void main(String[] args) {
        new QuadruplyLinkedGrid();
    }
}
