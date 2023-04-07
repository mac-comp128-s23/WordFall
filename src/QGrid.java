import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.util.Map;

public class QGrid {
    private final int width = 5;
    private final int height = 8;

    private ArrayList<ArrayList<QNode<String>>> grid;
    private Map<Integer, ArrayList<String>> dictionary;

    /*
     * Constructor for QuadruplyLinkedNode grid. First nested loop instantiates each ArrayList within each Arraylist.
     * Second nested loop links all the nodes up.
     */
    public QGrid() {
        dictionary = readFile("src/wordlist.txt");
        grid = new ArrayList<ArrayList<QNode<String>>>();

        for(int y = 0; y < height; y++) {
            ArrayList<QNode<String>> row = new ArrayList<QNode<String>>();
            for(int x = 0; x < width; x++) {
                row.add(new QNode<String>());
            }
            grid.add(row);
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                QNode<String> node = grid.get(y).get(x);
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
     * Checks for any words on the grid that have been formed with the placement of the newest letter.
     * Words can be formed vertically (top->bottom or bottom->top) and horizontally (left->right).
     * @param current the most recently placed letter
     * @return All of the words that were found
     */
    public ArrayList<String> wordChecker(QNode<String> current){
        ArrayList<String> wordsFound = new ArrayList<String>();
        
        // Finding the lowest node in the column and the leftmost node in the row
        QNode<String> lowerMostNode = current;
        while(lowerMostNode.getLower() != null) {
            lowerMostNode = lowerMostNode.getLower();
        }
        QNode<String> leftMostNode = current;
        while(leftMostNode.getLeft() != null) {
            leftMostNode = leftMostNode.getLeft();
        }

        // Finding words horizontally
        for(int wordLength = width; wordLength >= 2; wordLength--) {
            for(int startIndex = 0; startIndex + 1 <= width; startIndex++) {
                QNode<String> temp = leftMostNode;
                StringBuilder word = new StringBuilder("");
                // for(int i = 0; i < startIndex; i++){                     I added this so that the word being built actually starts at startIndex
                //     temp = temp.getRight();
                // }
                for(int step = 0; step < wordLength; step++) {
                    word.append(temp.getValue());
                    temp = temp.getRight();
                }

                if(dictionary.get(wordLength).contains(word.toString())) {
                    wordsFound.add(word.toString());
                    break;
                }
            }
        }

        // Finding words vertically
        for(int wordLength = height; wordLength >= 2; wordLength--) {
            for(int startIndex = 0; startIndex + 1 <= height; startIndex++) {
                QNode<String> temp = lowerMostNode;
                StringBuilder word = new StringBuilder("");
                // for(int i = 0; i < startIndex; i++){                     I added this so that the word being built actually starts at startIndex
                //     temp = temp.getRight();
                // }

                for(int step = 0; step < wordLength; step++) {
                    word.append(temp.getValue());
                    temp = temp.getUpper();
                }

                if(dictionary.get(wordLength).contains(word.toString())) {
                    wordsFound.add(word.toString());
                    break;
                }
                if(dictionary.get(wordLength).contains(reverse(word.toString()))) {
                    wordsFound.add(word.toString());
                    break;
                }
            }
        }

        return wordsFound;
    }

    /**
     * Reverses a given string. This is used for checking vertical words, since they can be oriented up-down or down-up.
     * @param s The string to be reversed
     * @return The reversed string
     */
    private String reverse(String s) {
        StringBuilder output = new StringBuilder("");
        for(int i = s.length(); i >= 0; i--) {
            output.append(s.charAt(i));
        }
        return output.toString();
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
        new QGrid();
    }
}
