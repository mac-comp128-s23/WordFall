import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.util.Map;

public class QuadruplyLinkedGrid {
    private final int width = 5;
    private final int height = 8;

    private ArrayList<ArrayList<QuadruplyLinkedNode<String>>> grid;
    private Map<Integer, ArrayList<String>> dictionary;

    /*
     * Constructor for QuadruplyLinkedNode grid. First nested loop instantiates each ArrayList within each Arraylist.
     * Second nested loop links all the nodes up.
     */
    public QuadruplyLinkedGrid() {
        dictionary = readFile("src/wordlist.txt");
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
    
    /**
     * Checks for any words 
     * @param current
     */
    public ArrayList<String> wordChecker(QuadruplyLinkedNode<String> current){
        ArrayList<String> wordsFound = new ArrayList<String>();
        QuadruplyLinkedNode<String>[] takenOut;
        StringBuilder word = new StringBuilder("");
        int row;
        
        for(int i = 0; i < grid.size(); i++){
            if (grid.get(i).contains(current)){
                
            }
        }

        for (int i = width; i>= 2; i--){
            for(int j = 0; j+i <= width; j++){
                for(int k = 0; k< i; k++){
                    word.append(grid.get(grid.indexOf(current)).get(k).getValue());
                }
            }
        }

        return wordsFound;
    }

    /**
     * Reads the wordlist file and returns an ArrayList of Strings representing the words.
     * In this case we will use "src/wordlist.txt" as the fileName argument
     * @param fileName
     * @return
     */
    public Map<Integer, ArrayList<String>> readFile(String fileName) {
        Map<Integer, ArrayList<String>> wordMap = new HashMap<Integer, ArrayList<String>>();
        try {
            File myFile = new File(fileName);
            Scanner fileScanner = new Scanner(myFile);

            // For each word in the file (since they are separated by new lines):
            while(fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().toLowerCase();
                int wordLength = word.length();
                ArrayList<String> updatedList = wordMap.get(wordLength);
                updatedList.add(word);

                // Adding words to the map based on their length
                wordMap.put(wordLength, updatedList);
            }

            fileScanner.close();
        } catch(Exception e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return wordMap;
    }

    public static void main(String[] args) {
        new QuadruplyLinkedGrid();
    }
}
