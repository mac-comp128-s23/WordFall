import edu.macalester.graphics.*;

public class Game {
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private final int CANVAS_WIDTH = 800;
    private final int CANVAS_HEIGHT = 800;

    private final int GAME_WIDTH = 375;
    private final int GAME_HEIGHT = 600;

    private int letterStepTime = 1000; //milliseconds

    private int fallingLetterRow;
    private int fallingLetterCol;

    private CanvasWindow canvas;
    private QGrid grid;
    private boolean gameIsRunning;
    private boolean letterHasLanded;

    public void moveDown() {
        if(gameIsRunning) {
            //TODO: Check for game end
            if(letterHasLanded) {
                int random = (int)(Math.random() * 26);
                String letter = alphabet.substring(random, random + 1);
                grid.setNode(0, 2, letter);

                fallingLetterRow = 0;
                fallingLetterCol = 2;

                letterHasLanded = false;
            } else {
                QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);
                if(!fallingNode.setLower(fallingNode.getValue())) {
                    letterHasLanded = true;
                } else {
                    fallingLetterRow++;
                }
            }

            canvas.pause(letterStepTime);
            drawGrid();
        }
    }

    /**
     * The game constructor. 
     * Creating a new Game object causes the window to appear and the game to begin running.
     */
    public Game() {
        letterHasLanded = true;
        gameIsRunning = true;

        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
        grid = new QGrid();
        drawGrid();

        canvas.animate(() -> moveDown());

        canvas.onKeyDown(e -> {
            moveFallingLetter(e.getKey().toString());
        });
    }

    /**
     * Moves the currently falling letter to the left or right depending on which arrow key is pressed.
     * @param key The name of the key ("LEFT_ARROW" or "RIGHT_ARROW")
     */
    private void moveFallingLetter(String key) {
        //TODO: Add code to make the falling letter move left or right based on which arrow keys the user presses.
        QNode<String> fallingNode = grid.getNode(fallingLetterRow, fallingLetterCol);
        if(key.equals("LEFT_ARROW")) {
            if(fallingNode.setLeft(fallingNode.getValue())) {
                fallingLetterCol--;
            }
        } else { //RIGHT_ARROW:
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

                GraphicsText text = new GraphicsText(grid.getNode(row, col).getValue(), 0, 0); //grid.getNode(row, col).getValue()
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
