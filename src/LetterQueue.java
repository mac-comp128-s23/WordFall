import java.util.Queue;
import java.util.ArrayDeque;
import edu.macalester.graphics.*;

/**
 * This class controls the queue of the five next letters on the right side of the screen.
 * Additionally, it deals with the semi-random generation of new letters, letter pairs, or triplets.
 */
public class LetterQueue {
    private static final int length = 5;
    private static final int sideLength = Game.GAME_WIDTH / 5;
    private static final Point topLeft = new Point((Game.CANVAS_WIDTH - Game.GAME_WIDTH) / 2 + 6 * sideLength, 150);

    private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static String doubleLetters = "AT,EN,ER,IN,LE,LY,NT,ON,QU,RA,RE,TE,TI,ST,TH,";
    private final static String tripleLetters = "EST,IVE,ING,ERS,ICK,AST,";
    
    private final static int[] distribution = new int[]{13, 3, 3, 6, 18, 3, 4, 3, 12, 2, 2, 5, 3, 8, 11, 3, 1, 9, 6, 9, 6, 3, 3, 2, 3, 2};
    private final static int[] points = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

    private Queue<QNode<String>> queue;
    private GraphicsGroup group;
    private Game game;

    public LetterQueue(Game game) {
        queue = new ArrayDeque<QNode<String>>(length);
        group = new GraphicsGroup(topLeft.getX(), 150);
        this.game = game;

        initialSetup();
    }

    /**
     * First sets up the queue by filling it with five random letters.
     */
    private void initialSetup() {
        for(int i = 0; i < length; i++) {
            QNode<String> newNode = new QNode<String>();
            String newLetter = getRandomLetter();
            newNode.setValue(newLetter, getLetterScore(newLetter));
            group.add(newNode.getGraphics());
            queue.add(newNode);
        }
    }

    /**
     * Returns the next node in the queue and re-fills it.
     * @return
     */
    public QNode<String> next() {
        // group.remove(queue.peek().getGraphics());
        QNode<String> output = queue.poll();
        updateQueue();
        return output;
    }

    private void updateQueue() {
        QNode<String> newNode = new QNode<String>();
        String newLetter = getRandomLetter();
        newNode.setValue(newLetter, getLetterScore(newLetter));
        queue.add(newNode);
    }

    /**
     * Returns the graphical representation of the queue.
     * @return
     */
    public GraphicsGroup getGraphics() {
        group = new GraphicsGroup(topLeft.getX(), 150);
        GraphicsText label = new GraphicsText("Next up:");
        label.setFontSize(20);
        label.setCenter(sideLength / 2, -20);
        group.add(label);

        Queue<QNode<String>> copy = new ArrayDeque<QNode<String>>(queue);
        for(int y = 0; y < sideLength * 5; y += sideLength) {
            GraphicsGroup pointGroup = new GraphicsGroup(0, y);
            QNode<String> tempNode = copy.poll();

            Rectangle rect = new Rectangle(0, 0, sideLength, sideLength);
            rect.setStrokeWidth(5);
            rect.setFillColor(Game.findColor(tempNode.getPoints()));

            GraphicsText text = new GraphicsText(tempNode.getValue());
            text.setStrokeWidth(1);
            text.setCenter(sideLength / 2, sideLength / 2);

            GraphicsText pointText = new GraphicsText(Integer.toString(tempNode.getPoints()));
            pointText.setCenter(sideLength - 15, sideLength - 10);
            

            pointGroup.add(rect);
            pointGroup.add(text);
            pointGroup.add(pointText);

            group.add(pointGroup);
        }
        return group;
    }

    /**
     * Using the letter distribution in Banangrams as a reference for single letters, gives a random letter.
     * At later points in the game, chances for a double or triple letter are given.
     * @return
     */
    private String getRandomLetter() {
        //Checks for double/triple letters beforehand
        if(game.grid.getScore() >= 300) {
            if((int)(Math.random() * 10) == 0) { // 1 in 10 chance for a double letter
                int random = (int) (Math.random() * (doubleLetters.length() / 3));
                return doubleLetters.substring(random * 3, random * 3 + 2);
            }
        }
        if(game.grid.getScore() >= 600) {
            if((int)(Math.random() * 15) == 0) { // 1 in 15 chance for a triple letter
                int random = (int) (Math.random() * (tripleLetters.length() / 4));
                return tripleLetters.substring(random * 4, random * 4 + 3);
            }
        }

        int random = (int) (Math.random() * 143); // Integer from 0 to 142, the total amount of tiles in the distribution array
        int total = 0;

        for(int outputIndex = 0; outputIndex < 26; outputIndex++) {
            total += distribution[outputIndex];
            if(total >= random) {
                return alphabet.substring(outputIndex, outputIndex + 1);
            }
        }
        return "";
    }

    /**
     * Returns the score for a tile (which could have 1, 2, or 3 letters on it)
     * @param letter the String value that the tile holds
     * @return an integer score for the tile.
     */
    public int getLetterScore(String letter){
        if(letter.length() == 1) {
            return points[alphabet.indexOf(letter)] * 10;
        }
        else {
            int sum = 0;
            for(int i = 0; i < letter.length(); i++) {
                try {
                    sum += points[alphabet.indexOf(letter.substring(i, i + 1))] * 10;
                } catch(Exception e) {
                    System.out.println(letter + " " + letter.charAt(i));
                }
            }
            return sum;
        }
    }
}
