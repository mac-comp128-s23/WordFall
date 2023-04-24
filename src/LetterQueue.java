import java.util.Queue;
import java.util.ArrayDeque;
import edu.macalester.graphics.*;

public class LetterQueue {
    private static final int length = 5;
    private static final int sideLength = Game.GAME_WIDTH / 5;
    private static final Point topLeft = new Point((Game.CANVAS_WIDTH - Game.GAME_WIDTH) / 2 + 6 * sideLength, 150);

    private Queue<QNode<String>> queue;
    private GraphicsGroup group;

    public LetterQueue() {
        queue = new ArrayDeque<QNode<String>>(length);
        group = new GraphicsGroup(Game.CANVAS_WIDTH / 2 + Game.GAME_WIDTH / 2, 150);

        initialSetup();
    }

    /**
     * First sets up the queue by filling it with five random letters.
     */
    private void initialSetup() {
        for(int i = 0; i < length; i++) {
            QNode<String> newNode = new QNode<String>();
            String newLetter = Game.getRandomLetter();
            newNode.setValue(newLetter, Game.getLetterScore(newLetter));
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
        String newLetter = Game.getRandomLetter();
        newNode.setValue(newLetter, Game.getLetterScore(newLetter));
        queue.add(newNode);
    } 

    public GraphicsGroup getGraphics() {
        Queue<QNode<String>> copy = new ArrayDeque<QNode<String>>(queue);
        for(int y = 150; y < 150 + sideLength * 5; y += sideLength) {
            GraphicsGroup pointGroup = new GraphicsGroup(topLeft.getX(), y);
            Rectangle rect = new Rectangle(0, 0, sideLength, sideLength);
            rect.setStrokeWidth(5);
            pointGroup.add(rect);

            QNode<String> tempNode = copy.poll();

            GraphicsText text = new GraphicsText(tempNode.getValue());
            text.setCenter(sideLength / 2, sideLength / 2);
            pointGroup.add(text);

            pointGroup.add(rect, y, y);
            GraphicsText pointText = new GraphicsText(Integer.toString(tempNode.getPoints()));
            pointText.setCenter(sideLength - 15, sideLength - 10);
            pointGroup.add(pointText);
        }
        return group;
    }
}
