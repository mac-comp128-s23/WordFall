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
        group = new GraphicsGroup(topLeft.getX(), 150);

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
            text.setCenter(sideLength / 2, sideLength / 2);

            // pointGroup.add(rect, y, y);
            GraphicsText pointText = new GraphicsText(Integer.toString(tempNode.getPoints()));
            pointText.setCenter(sideLength - 15, sideLength - 10);
            

            pointGroup.add(rect);
            pointGroup.add(text);
            pointGroup.add(pointText);

            group.add(pointGroup);
        }
        return group;
    }
}
