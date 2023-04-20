import edu.macalester.graphics.*;

public class Game {
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final int CANVAS_WIDTH = 800;
    private final int CANVAS_HEIGHT = 800;

    private final int GAME_WIDTH = 375;
    private final int GAME_HEIGHT = 600;

    private long stepInterval; // in milliseconds
    private long lastStepTime;
    private long currentTime;

    private int fallingLetterRow;
    private int fallingLetterCol;

    private CanvasWindow canvas;
    private QGrid grid;
    private boolean gameIsRunning;
    private boolean letterHasLanded;

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

        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
        grid = new QGrid();
        drawGrid();

        // The lambda functions that run the game:
        canvas.onKeyDown(e -> {
            moveSideways(e.getKey().toString());
            canvas.draw();
        });

        // canvas.pause(2000);
        // moveDown();

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

        //TODO: Check for game end

        // Checks if the letter has landed. If so, start a new letter falling from the top center space.
        if(letterHasLanded) {
            int random = (int)(Math.random() * 26);
            String letter = alphabet.substring(random, random + 1);
            grid.setNode(0, 2, letter);

            fallingLetterRow = 0;
            fallingLetterCol = 2;

            letterHasLanded = false;
        // If it hasn't landed, try to make it fall by one step. If this can't happen, update letterHasLanded.
        } else {
            QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);
            if(!fallingNode.setLower(fallingNode.getValue())) {
                letterHasLanded = true;
            } else {
                fallingLetterRow++;
            }
        }

        stepInterval = 1000;

        // Updates the grid to show the new letter positions
        drawGrid();
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
    }

    public static void main(String[] args) {
        new Game();
    }
}
