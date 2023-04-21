import edu.macalester.graphics.*;
import java.awt.Color;

public class Game {
    private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // private final static String easyWords = "BAT";

    private final static int CANVAS_WIDTH = 800;
    private final static int CANVAS_HEIGHT = 800;

    private final static int GAME_WIDTH = 375;
    private final static int GAME_HEIGHT = 600;

    private long stepInterval; // in milliseconds
    private long lastStepTime;
    private long currentTime;

    private int fallingLetterRow;
    private int fallingLetterCol;

    private CanvasWindow canvas;
    private QGrid grid;
    private boolean gameIsRunning;
    private boolean letterHasLanded;

    private int totalLettersLanded;

    /**
     * The game constructor. 
     * Creating a new Game object causes the window to appear and the game to begin running.
     */
    public Game() {
        letterHasLanded = true;
        gameIsRunning = true;
        stepInterval = 1000;
        lastStepTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();
        totalLettersLanded = 0;

        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
        grid = new QGrid();
        drawGrid();

        // The lambda functions that run the game:
        canvas.onKeyDown(e -> {
            moveSideways(e.getKey().toString());
            drawGrid();
        });

        canvas.animate(() -> moveDown());
    }

    /**
     * Moves the falling letter down every second (give or take). 
     * It also detects when the falling letter has landed on another letter / the floor.
     */
    public void moveDown() {
        currentTime = System.currentTimeMillis();

        // Wait until the full interval between steps has passed and make sure the game is still running.
        if(currentTime - lastStepTime < stepInterval || !gameIsRunning) {
            return;
        }

        lastStepTime = currentTime;

        if(grid.gameIsOver()){
            
            GameOverScreen();
            gameIsRunning = false;
        }
        else{

            // Checks if the letter has landed. If so, start a new letter falling from the top center space.
            if(letterHasLanded) {
                String letter = getRandomLetter();
                grid.setNode(0, 2, letter);

                fallingLetterRow = 0;
                fallingLetterCol = 2;

                letterHasLanded = false;
            // If it hasn't landed, try to make it fall by one step. If this can't happen, update letterHasLanded.
            } else {
                QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);
                if(!fallingNode.setLower(fallingNode.getValue())) {
                    letterHasLanded = true;
                    totalLettersLanded++;

                    grid.afterWordSettles(grid.getNode(fallingLetterRow, fallingLetterCol), letterHasLanded);
                } else {
                    fallingLetterRow++;
                }
            }

            // Recalculate the step interval before continuing
            recalculateStepInterval();
            System.out.println(totalLettersLanded + " letters, interval = " + stepInterval);

            // Updates the grid to show the new letter positions
            drawGrid();
        }
    }

    private void GameOverScreen(){
        GraphicsText text = new GraphicsText("GAME OVER", CANVAS_WIDTH/4 + 16, CANVAS_HEIGHT/2); 
        text.setFillColor(new Color(128,5,0));
        text.setFontSize(60);
        text.setStrokeWidth(5);
        canvas.add(text);
    }

    /**
     * Moves the currently falling letter to the left or right depending on which arrow key is pressed.
     * @param key The name of the key ("LEFT_ARROW" or "RIGHT_ARROW")
     */
    private void moveSideways(String key) {
        // The letter can't be moved if it's already landed.
        if(letterHasLanded) {
            return;
        }

        QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);

        if(key.equals("DOWN_ARROW")) {
            stepInterval = 10;
        } else if(key.equals("LEFT_ARROW")) {
            if(fallingNode.setLeft(fallingNode.getValue())) {
                fallingLetterCol--;
            }
        } else if(key.equals("RIGHT_ARROW")) {
            if(fallingNode.setRight(fallingNode.getValue())) {
                fallingLetterCol++;
            }
        }
    }

    /**
     * Draws the initial grid
     * Subsequent calls will update the graphics, including any letters that are present.
     */
    private void drawGrid() {
        canvas.removeAll();

        Point topLeft = new Point((CANVAS_WIDTH - GAME_WIDTH) / 2, 150);
        int sideLength = GAME_WIDTH / 5;
        Rectangle background = new Rectangle(topLeft.getX(), topLeft.getY(), GAME_WIDTH, GAME_HEIGHT);
        background.setStrokeWidth(10);

        int row = 0;
        int col = 0;

        for(double y = topLeft.getY(); y <= topLeft.getY() + GAME_HEIGHT - sideLength; y += sideLength) {
            col = 0;
            for(double x = topLeft.getX(); x <= topLeft.getX() + GAME_WIDTH - sideLength; x += sideLength) {
                GraphicsGroup group = new GraphicsGroup(x, y);

                Rectangle rect = new Rectangle(0, 0, sideLength, sideLength);
                rect.setStrokeWidth(5);
                if(row<grid.redLine()+1){
                    rect.setFillColor(new Color(255, 114, 118));
                }
                grid.getNode(row, col).setGraphics(group);
                group.add(rect);

                GraphicsText text = new GraphicsText(grid.getNode(row, col).getValue(), 0, 0);
                text.setCenter(sideLength / 2, sideLength / 2);
                text.setFontSize(20);
                group.add(text);

                canvas.add(group);
                
                col++;
            }
            row++;
        }
        canvas.add(background);

        double lineNum = topLeft.getY() + sideLength*grid.redLine() + sideLength;
        Line redLine = new Line(topLeft.getX(), lineNum, topLeft.getX()+sideLength*5, lineNum);
        redLine.setStrokeColor(new Color(255, 0, 0));
        redLine.setStrokeWidth(5);
        canvas.add(redLine);
    }

    /**
     * Using the letter distribution in Banangrams as a reference, gives a random letter.
     * @return
     */
    private static String getRandomLetter() {
        int[] distribution = new int[]{13, 3, 3, 6, 18, 3, 4, 3, 12, 2, 2, 5, 3, 8, 11, 3, 2, 9, 6, 9, 6, 3, 3, 2, 3, 2};
        int random = (int) (Math.random() * 144); // Integer from 0 to 143
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
     * Based on the total number of letters that have landed, recalculate the step interval, which speeds up over time.
     * If x is the number of letters that have landed and i is the interval in milliseconds,
     *      i = 450cos(x/200)+550   {0 ≤ x ≤ 254}
     *      i = 1000(0.9985)^x      {255 ≤ x}
     * @return
     */
    private void recalculateStepInterval() {
        // The two functions first intersect at x = 254.306, so after that point, switch to exp. decay.
        if(totalLettersLanded <= 254) {
            this.stepInterval = (int) (450 * Math.cos(totalLettersLanded / 200.0) + 550);
        } else {
            this.stepInterval =  (int) (1000 * Math.pow(0.9985, totalLettersLanded));
        }
    }

    public static void main(String[] args) {
        new Game();

        // Testing letter distrubution
        // for(int i = 0; i < 100; i++) {
        //     System.out.println(getRandomLetter());
        // }
    }
}
