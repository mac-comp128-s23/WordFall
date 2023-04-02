import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class QuadruplyLinkedGrid {
    private final int width = 5;
    private final int height = 8;

    private ArrayList<ArrayList<QuadruplyLinkedNode<String>>> grid;
    private ArrayList<String> dic;

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
        dic = readFile("src/wordlist.txt");
    }
        //Will need a big method that checks if a word was formed in each side, making sure its checking for larger words first.
    public void wordChecker(QuadruplyLinkedNode<String> current){
        QuadruplyLinkedNode<String>[] takenOut;
        StringBuilder word = new StringBuilder("");
        int row;
        for(int i = 0; i < grid.size(); i++){
            if (grid.get(i).contains(current)){
                row 
            }
        }
        for (int i = width; i>= 2; i--){
            for(int j = 0; j+i <= width; j++){
                for(int k = 0; k< i; k++){
                    word.append(grid.get(grid.indexOf(current)).get(k).getValue());
                }
            }
        }
    }

    /**
     * Reads the wordlist file and returns an ArrayList of Strings representing the words.
     * In this case we will use "src/wordlist.txt" as the fileName argument
     * @param fileName
     * @return
     */
    public ArrayList<String> readFile(String fileName) {
        ArrayList<String> wordList = new ArrayList<String>();
        try {
            File myFile = new File(fileName);
            Scanner fileScanner = new Scanner(myFile);
            while(fileScanner.hasNextLine()) {
                String data = fileScanner.nextLine().toLowerCase();
                wordList.add(data);
            }
            fileScanner.close();
        } catch(Exception e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        return wordList;
    }

    public static void main(String[] args) {
        new QuadruplyLinkedGrid();
    }
}
