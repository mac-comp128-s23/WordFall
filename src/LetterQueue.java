import java.util.Queue;
import java.util.ArrayDeque;
import edu.macalester.graphics.*;

public class LetterQueue {
    private static final int length = 5;
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
        QNode<String> output = queue.remove();
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
        return group;
    }
}
