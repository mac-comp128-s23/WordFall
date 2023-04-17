import edu.macalester.graphics.*;
import java.util.Iterator;

/**
 * Quadruply linked nodes have four links to other nodes. One for the upper, lower, left, and right nodes.
 * These make up the grid which will be used for storing letters in our game.
 */
public class QNode<E> {
    private QNode<E> upper, lower, left, right;
    private E value;
    private GraphicsText text;
    private Rectangle rect;

    public QNode() {
        this.upper = null;
        this.lower = null;
        this.left = null;
        this.right = null;
        this.value = null;
        this.text = new GraphicsText();
        this.rect = new Rectangle(0, 0, 0, 0);
    }

    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(rect);
        group.add(text);
        return group;
    }

    public void setGraphics(GraphicsGroup g) {
        Iterator<GraphicsObject> it = g.iterator();
        // The rectangle is the first object in the GraphicsGroup passed from Game.drawGrid().
        if(it.hasNext()) {
            rect = (Rectangle) it.next();
            System.out.println("Rectangle has been changed");
        }
        if(it.hasNext()) {
            text = (GraphicsText) it.next();
            System.out.println("GraphicsText has been changed.");
        }
    }

    public E getValue() {
        return value;
    }

    public void setValue(E element) {
        if(element == null) {
            this.text.setText("");
        } else {
            this.text.setText((String)element);
        }
        this.value = element;
    }

    public QNode<E> getLeft() {
        return left;
    }

    public QNode<E> getRight() {
        return right;
    }

    public QNode<E> getUpper() {
        return upper;
    }

    public QNode<E> getLower() {
        return lower;
    }

    public boolean setLeft(E element) {
        if(this.left != null)
            if(this.left.value == null){
                this.left.value = element;
                return true;
            }
        return false;
    }

    public boolean setRight(E element) {
        if(this.right != null)
            if(this.right.value == null){
                this.right.value = element;
                return true;
            }
        return false;
    }

    public boolean setUpper(E element) {
        if(this.upper != null)
            if(this.upper.value == null){
                this.upper.value = element;
                return true;
            }
        return false;
    }

    public boolean setLower(E element) {
        if(this.lower != null)
            if(this.lower.getValue() == null){
                this.lower.setValue(element);
                this.setValue(null);
                return true;
            }
            // System.out.println("this.lower is NOT NULL or its value is NOT NULL");
        return false;
    }

    public void linkLeft(QNode<E> node) {
        this.left = node;
    }

    public void linkRight(QNode<E> node) {
        this.right = node;
    }

    public void linkUpper(QNode<E> node) {
        this.upper = node;
    }

    public void linkLower(QNode<E> node) {
        this.lower = node;
    }
 }