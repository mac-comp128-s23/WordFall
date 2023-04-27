import edu.macalester.graphics.*;
import java.awt.Color;

public class Game {
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
    public QGrid grid;
    private boolean gameIsRunning;
    private boolean letterHasLanded;

    /**
     * The game constructor. 
     * Creating a new Game object causes the window to appear and the game to begin running.
     */
    public Game() {
        grid = new QGrid();
        queue = new LetterQueue(this);

        letterHasLanded = true;
        gameIsRunning = true;
        stepInterval = 1000;
        lastStepTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();

        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
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

                    grid.afterWordSettles(grid.getNode(fallingLetterRow, fallingLetterCol), letterHasLanded);
                } else {
                    fallingLetterRow++;
                }
            }

            // Recalculate the step interval before continuing
            recalculateStepInterval();

            // Updates the grid to show the new letter positions
            drawGrid();
        }
    }

    /**
     * Shows "GAME OVER" in big red letters when called
     */
    private void GameOverScreen(){
        GraphicsText text = new GraphicsText("GAME OVER"); 
        text.setFillColor(new Color(128,5,0));
        text.setFontSize(60);
        text.setStrokeWidth(5);
        text.setCenter(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
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

        Image img;
        // img = new Image(0, 0, Game.class.getResource("/space.png").toURI().getPath());
        img = new Image(0, 0, "space.png");
        img.rotateBy(90);
        img.moveBy(-100, 0);
        img.setScale(1.3);
        canvas.add(img);

        Point topLeft = new Point((CANVAS_WIDTH - GAME_WIDTH) / 2, 150);
        int sideLength = GAME_WIDTH / 5;
        int strokeWidth = 8;
        Rectangle background = new Rectangle(topLeft.getX(), topLeft.getY()-60, GAME_WIDTH, GAME_HEIGHT+60);
        background.setStrokeWidth(strokeWidth);

        GraphicsGroup titleGroup = new GraphicsGroup(topLeft.getX(), topLeft.getY() - 120);
        // Rectangle titleRect = new Rectangle(0, 0, GAME_WIDTH, 60);
        GraphicsText title = new GraphicsText("WORD CATCHER!!!");
        title.setFontSize(30);
        title.setStrokeWidth(2);
        title.setCenter(GAME_WIDTH / 2, 30);
        // titleGroup.add(titleRect);
        titleGroup.add(title);

        canvas.add(titleGroup);

        GraphicsGroup scoreGroup = new GraphicsGroup(topLeft.getX(), topLeft.getY()-60);
        // Rectangle scoreBoard = new Rectangle(0, 0, GAME_WIDTH, 60);
        // scoreBoard.setFillColor(new Color(172, 79, 188));
        // scoreBoard.setStrokeWidth(5);
        Image scoreBoard = new Image("red.png");
        scoreBoard.setScale(1.25, 1);
        scoreBoard.moveBy(40, 0);
        // scoreBoard.setAnchor(scoreBoard.getWidth() / 2, scoreBoard.getHeight() / 2);
        // scoreBoard.setScale(375.0 / 300.0, 60.0 / 168.0);
        // scoreBoard.setPosition(0, 0);

        GraphicsText score = new GraphicsText("Score: " + grid.getScore(), 0, 0);
        score.setStrokeWidth(1);
        score.setCenter(GAME_WIDTH/2, 30);
        scoreGroup.add(scoreBoard);
        scoreGroup.add(score);
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
    public static Color findColor(int points){
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
        else if(points == 60){
            col = new Color(5,195,221);    //aqua blue
        }
        else if(points == 80){
            col = new Color(0,78,255);      //bright blue
        }
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

    public int getLetterScore(String letter){
        return queue.getLetterScore(letter);
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
            this.stepInterval = (int) (1000 * Math.pow(0.9985, grid.getScore()));
        }

        // this.stepInterval = Math.min((int) (450 * Math.cos(grid.getScore() / 200.0) + 550), (int) (1000 * Math.pow(0.9985, grid.getScore())));
    }

    public static void main(String[] args) {
        new Game();
    }
}
