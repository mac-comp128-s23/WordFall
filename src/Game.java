import edu.macalester.graphics.*;
import java.awt.Color;

public class Game {
    private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static int[] distribution = new int[]{13, 3, 3, 6, 18, 3, 4, 3, 12, 2, 2, 5, 3, 8, 11, 3, 2, 9, 6, 9, 6, 3, 3, 2, 3, 2}; //1, 6, 7, 8, 10, 11, 13, 14, 15, 16, 17
    private final static int[] points = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    // private final static String easyWords = "BAT";

    public final static int CANVAS_WIDTH = 800;
    public final static int CANVAS_HEIGHT = 800;

    public final static int GAME_WIDTH = 375;
    public final static int GAME_HEIGHT = 600;

    private LetterQueue queue;

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
        queue = new LetterQueue();

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
                // Takes the next node from the queue as the one to place on the grid
                QNode<String> next = queue.next();

                String letter = next.getValue();
                int letterPoints = next.getPoints();
                grid.setNode(0, 2, letterPoints, letter);

                fallingLetterRow = 0;
                fallingLetterCol = 2;

                letterHasLanded = false;
            // If it hasn't landed, try to make it fall by one step. If this can't happen, update letterHasLanded.
            } else {
                QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);
                if(!fallingNode.setLower(fallingNode.getValue(), fallingNode.getPoints())) {
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

    /**
     * Shows "GAME OVER" in big red letters when called
     */
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
            if(fallingNode.setLeft(fallingNode.getValue(), fallingNode.getPoints())) {
                fallingLetterCol--;
            }
        } else if(key.equals("RIGHT_ARROW")) {
            if(fallingNode.setRight(fallingNode.getValue(), fallingNode.getPoints())) {
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
        int strokeWidth = 8;
        Rectangle background = new Rectangle(topLeft.getX(), topLeft.getY(), GAME_WIDTH, GAME_HEIGHT);
        background.setStrokeWidth(strokeWidth);

        GraphicsGroup scoreGroup = new GraphicsGroup(topLeft.getX(), topLeft.getY()-60);
        Rectangle scoreBoard = new Rectangle(0, 0, GAME_WIDTH, 60);
        scoreBoard.setStrokeWidth(strokeWidth);
        GraphicsText score = new GraphicsText("Score: " + grid.getScore(), 0, 0);
        score.setCenter(GAME_WIDTH/2, 30);
        scoreGroup.add(score);   
        scoreGroup.add(scoreBoard);
        canvas.add(scoreGroup);

        canvas.add(queue.getGraphics());

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
                else{
                    rect.setFillColor(new Color(118, 114, 255));
                }
                grid.getNode(row, col).setGraphics(group);        

                GraphicsText text = new GraphicsText(grid.getNode(row, col).getValue(), 0, 0);
                text.setCenter(sideLength / 2, sideLength / 2);
                text.setFontSize(20);
                text.setStrokeWidth(1);
                
                if(grid.getNode(row, col).getValue() != null){
                    rect.setFillColor(findColor(grid.getNode(row, col).getPoints()));
                }

                GraphicsText points = new GraphicsText("" + grid.getNode(row, col).getPoints(), 0, 0);
                if(grid.getNode(row, col).getPoints() == 0){
                    points = new GraphicsText("", 0, 0);
                }
                points.setCenter(sideLength-15, sideLength-10);
                

                group.add(rect);
                group.add(text);
                group.add(points);
                
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
     * Returns the correct color for the point value of that letter.
     * @param letter
     * @return
     */
    private Color findColor(int points){
        Color col;
        //1, 6, 7, 8, 10, 11, 13, 14, 15, 16, 17
        if(points == 10){
            col = new Color(170,219,30);   //bright green
        }
        else if(points == 20){
            col = new Color(113,169,44);     //corn green
        }
        else if(points == 30){
            col = new Color(0,155,119);     //emerald green
        }
        else if(points == 40){
            col = new Color(0,124,128);     //teal blue
        }
        else if(points == 50){
            col = new Color(155,211,221);  //baby blue
        }
        else if(points == 80){
            col = new Color(5,195,221);    //aqua blue
        }
        // else if(points == 10){
        //     col = new Color(0,78,255);      //bright blue
        // }
        else{
            col = new Color(8,39,245);      //blue screen of death
        }
        // else if(points == 15){
        //     col = new Color(33,46,82);       //cetacean blue         changed to cloud burst blue
        // }
        // else if(points == 16){
        //     col = new Color(75,54,95);      //advent purple
        // }
        // else{
        //     col = new Color(128,49,167);    //grape purple
        // }
        return col;
    }

    /**
     * Using the letter distribution in Banangrams as a reference, gives a random letter.
     * @return
     */
    public static String getRandomLetter() {
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

    public static int getLetterScore(String letter){
        // int points = alphabet.indexOf(letter);
        // points = 19 - distribution[points];
        // return points;
        
        //Temporarily editing to have the same scores as Scrabble:
        return points[alphabet.indexOf(letter)]*10;
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
        if(grid.getScore() <= 254) {
            this.stepInterval = (int) (450 * Math.cos(grid.getScore() / 200.0) + 550);
        } else {
            this.stepInterval =  (int) (1000 * Math.pow(0.9985, grid.getScore()));
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
