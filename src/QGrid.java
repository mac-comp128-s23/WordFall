import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;

public class QGrid {
    private final int width = 5;
    private final int height = 8;
    private final int redLine = 2;

    private final static int MIN_WORD_LENGTH = 3;

    private ArrayList<ArrayList<QNode<String>>> grid;
    private Map<Integer, ArrayList<String>> dictionary;
    private boolean gameOver;
    private int score;

    /*
     * Constructor for QNode grid. First nested loop instantiates each ArrayList within each Arraylist.
     * Second nested loop links all the nodes to their neighbors.
     */
    public QGrid() {
        score = 0;
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
                    node.linkLeft(getNode(y, x-1));
                }
                if(y != height - 1) {
                    node.linkLower(getNode(y+1,x));
                }
                if(x != width - 1) {
                    node.linkRight(getNode(y,x+1));
                }
                if(y != 0) {
                    node.linkUpper(getNode(y-1,x));
                }
                //System.out.print(node);
            }
            //System.out.println();
        }
    }

    public QNode<String> getNode(int row, int col) {
        return grid.get(row).get(col);
    }

    public void setNode(int row, int col, int points, String value) {
        getNode(row, col).setValue(value, points);
    }

    public ArrayList<ArrayList<QNode<String>>> getGrid() {
        return grid;
    }

    public int getScore(){
        return score;
    }
    
    public boolean gameIsOver(){
        return gameOver;
    }
    public int redLine(){
        return redLine;
    }
    
    /**
     * Checks for any words on the grid that have been formed with the placement of the newest letter.
     * Words can be formed vertically (top->bottom) and horizontally (left->right).
     * @param current the most recently placed letter
     * @return All of the words that were found
     */
    private ArrayList<QNode<String>> wordChecker(QNode<String> current){
        ArrayList<QNode<String>> wordsFound = new ArrayList<QNode<String>>();
        
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
        for(int wordLength = width; wordLength >= MIN_WORD_LENGTH; wordLength--) {
            for(int startIndex = 0; startIndex + MIN_WORD_LENGTH <= width; startIndex++) {
                QNode<String> temp = leftMostNode;
                StringBuilder word = new StringBuilder("");
                ArrayList<QNode<String>> wordNodes = new ArrayList<QNode<String>>();
                for(int i = 0; i < startIndex; i++){
                    temp = temp.getRight();
                }
                for(int step = 0; step < wordLength; step++) {
                    if(temp != null){
                        word.append(temp.getValue());
                        wordNodes.add(temp);
                        temp = temp.getRight();
                    }
                }

                System.out.println(word);

                if(word.length() < 8 && word.length() > 1 && dictionary.get(word.length()).contains(word.toString().toLowerCase())) {
                    for(int i = 0; i < wordNodes.size(); i++){
                        if(!wordsFound.contains(wordNodes.get(i))){
                            wordsFound.add(wordNodes.get(i));
                        }
                    }
                    break;
                }
            }
        }

        // Finding words vertically
        for(int wordLength = height; wordLength >= MIN_WORD_LENGTH; wordLength--) {
            for(int startIndex = 0; startIndex + MIN_WORD_LENGTH <= height; startIndex++) {
                QNode<String> temp = lowerMostNode;
                StringBuilder word = new StringBuilder("");
                ArrayList<QNode<String>> wordNodes = new ArrayList<QNode<String>>();
                for(int i = 0; i < startIndex; i++){
                    temp = temp.getUpper();
                }

                for(int step = 0; step < wordLength; step++) {
                    if(temp != null) {
                        word.append(temp.getValue());
                        wordNodes.add(temp);
                        temp = temp.getUpper();
                    }
                }

                if(word.length() < 8 && word.length() > 1 && dictionary.get(word.length()).contains(reverse(word.toString().toLowerCase()))) {
                    for(int i = 0; i < wordNodes.size(); i ++){
                        if(!wordsFound.contains(wordNodes.get(i))){
                            wordsFound.add(wordNodes.get(i));
                        }
                    }
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
        for(int i = s.length()-1; i >= 0; i--) {
            output.append(s.charAt(i));
        }
        return output.toString();
    }

    /**
     * Deletes the nodes in the list and then moves everything down to normal.
     * @param list the list to be deleted
     */
    private void deleteNodes(ArrayList<QNode<String>> list){
        //System.out.println(list.toString());
        if(list.size() == 0){
            return;
        }
        for(int i = 0; i < list.size(); i++){
            score += list.get(i).getPoints();
            for(int row = 0; row < height; row++){
                for(int col = 0; col < width; col++){
                    if(getNode(row,col) == (list.get(i))){
                        getNode(row,col).setValue(null, 0);
                    }
                }
            }
        }

        for(int row = height-1; row >= 0; row--){
            for(int col = width-1; col >= 0; col--){
                if(getNode(row,col).setLower(getNode(row,col).getValue(), getNode(row,col).getPoints())){
                    getNode(row,col).setValue(null, 0);
                }
            }
        }

    }

    /**
     * Puts together all helper methods to do everything that happens once a words gets to the bottom of its column.
     * Then checks the entire grid for any new words that may have been 
     * @param current is the node that just landed
     */
    public void afterWordSettles(QNode<String> current, boolean letterHasLanded) {
        ArrayList<QNode<String>> wordsFound = wordChecker(current);        

        boolean wordsLeft = true;
        
        while(wordsLeft){
            deleteNodes(wordsFound);
            wordsLeft = false;
            for(int i = 0; i < width; i ++){
                wordsFound = wordChecker(getNode(0,i));
                if(wordsFound.size()>0){
                    wordsLeft = true;
                }
                deleteNodes(wordsFound);
            }
        }

        gameOver = gameOver(letterHasLanded);
    }

    /**
     * returns whether the game is over, determined by whether or not there is a block on the penultimate row.
     * @return
     */
    public boolean gameOver(boolean letterHasLanded){

        for(int i = 0; i < width; i ++){
            if(getNode(redLine, i).getValue() != null && letterHasLanded){
                return true;
            }
        }

        return false;
    }

    /**
     * Reads the wordlist file and returns an ArrayList of Strings representing the words.
     * In this case we will use "src/wordlist.txt" as the fileName argument
     * @param fileName
     * @return
     */
    private Map<Integer, ArrayList<String>> readFile(String fileName) {
        Map<Integer, ArrayList<String>> wordMap = new HashMap<Integer, ArrayList<String>>();
        try {
            File myFile = new File(fileName);
            Scanner fileScanner = new Scanner(myFile);

            // For each word in the file (since they are separated by new lines):
            while(fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().toLowerCase();
                int wordLength = word.length();

                ArrayList<String> updatedList = wordMap.get(wordLength);
                if(updatedList == null){
                    updatedList = new ArrayList<String>();
                }
                updatedList.add(word);

                // Adding words to the map based on their length
                wordMap.put(wordLength, updatedList);
            }
            fileScanner.close();
        } catch(FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return wordMap;
    }
}
