import edu.macalester.graphics.*;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private final int CANVAS_WIDTH = 800;
    private final int CANVAS_HEIGHT = 800;

    private final int GAME_WIDTH = 375;
    private final int GAME_HEIGHT = 600;

    private int row;
    private int col;

    private CanvasWindow canvas;
    private QGrid grid;
    private Timer timer;
    private boolean gameIsRunning;
    private boolean letterHasLanded;

    class MoveDownTask extends TimerTask {
        public void run() {
            if(gameIsRunning) {
                //TODO: Check for game end
                if(letterHasLanded) {
                    int random = (int)(Math.random() * 26);
                    String letter = alphabet.substring(random, random + 1);
                    grid.setNode(0, 2, letter);

                    row = 0;
                    col = 2;

                    letterHasLanded = false;

                    System.out.println("letterHasLanded == true");
                } else {
                    QNode<String> fallingNode = grid.getNode(row, col);
                    if(!fallingNode.setLower(fallingNode.getValue())) {
                        letterHasLanded = true;
                    } else {
                        row++;
                    }
                    System.out.println("letterHasLanded == false");
                }

                drawGrid();
            }
        }
    }

    public Game() {
        letterHasLanded = true;
        gameIsRunning = true;

        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
        grid = new QGrid();
        drawGrid();

        timer = new Timer();
        timer.schedule(new MoveDownTask(), 5000, 1000);

        canvas.onKeyDown(e -> {
            moveFallingLetter(e.getKey().toString());
        });
    }

    private void moveFallingLetter(String key) {
        //TODO: Make a letter "fall" from the top of the screen by moving it down every second(?).
        //Maybe make a new class for this, as we'll need to handle a few different things for it.

        //TODO: Add code to make the falling letter move left or right based on which arrow keys the user presses.
        if(key.equals("LEFT_ARROW")) {
            //Move the falling letter left
            row--;
        } else { //RIGHT_ARROW:
            //Move the falling letter right
            row++;
        }
    }

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
