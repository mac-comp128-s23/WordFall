import edu.macalester.graphics.*;

public class Game {
    private final int CANVAS_WIDTH = 800;
    private final int CANVAS_HEIGHT = 800;

    private final int GAME_WIDTH = 375;
    private final int GAME_HEIGHT = 600;

    private CanvasWindow canvas;
    private QGrid grid;

    public Game() {
        canvas = new CanvasWindow("Word Capture", CANVAS_WIDTH, CANVAS_HEIGHT);
        grid = new QGrid();
        drawGrid();

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
        } else { //RIGHT_ARROW:
            //Move the falling letter right
        }
    }

    private void drawGrid() {
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

                GraphicsText text = new GraphicsText("A"); //grid.getNode(row, col).getValue()
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
