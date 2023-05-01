import edu.macalester.graphics.*;
import java.util.Iterator;

/**
 * Quadruply linked nodes have four links to other nodes. One for the upper, lower, left, and right nodes.
 * These make up the grid which will be used for storing letters in our game.
 */
public class QNode<E> {
    private QNode<E> upper, lower, left, right;
    private E value;
    private int points;
    private GraphicsText text;
    private GraphicsText pointText;
    private Rectangle rect;

    public QNode() {
        this.upper = null;
        this.lower = null;
        this.left = null;
        this.right = null;
        this.value = null;
        this.points = 0;
        this.text = new GraphicsText();
        this.pointText = new GraphicsText();
        this.rect = new Rectangle(0, 0, 0, 0);
    }

    /**
     * Returns the GraphicsGroup of a Rectangle and GraphicsText that visually represent the group
     * @return
     */
    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(rect);
        group.add(text);
        group.add(pointText);
        return group;
    }

    /**
     * Updates the graphical representation of this node
     * @param g
     */
    public void setGraphics(GraphicsGroup g) {
        Iterator<GraphicsObject> it = g.iterator();
        // The rectangle is the first object in the GraphicsGroup passed from Game.drawGrid().
        if(it.hasNext()) {
            rect = (Rectangle) it.next();
        }
        if(it.hasNext()) {
            text = (GraphicsText) it.next();
        }
        if(it.hasNext()) {
            if(it.next().toString().equals("0")){
                pointText = new GraphicsText("");
            }else{
                pointText = (GraphicsText) it.next();
            }
        }
    }

    public E getValue() {
        return value;
    }

    public void setValue(E element, int p) {
        if(element == null) {
            this.text.setText("");
        } else {
            this.text.setText((String)element);
        }
        this.value = element;
        if(p == 0){
            this.pointText.setText("");
        } else {
            this.pointText.setText("" + p);
        }
        this.points = p;
    }

    public int getPoints(){
        return points;
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

    public boolean setLeft(E element, int p) {
        if(this.left != null)
            if(this.left.getValue() == null){
                this.left.setValue(element, p);
                this.setValue(null, 0);
                return true;
            }
        return false;
    }

    public boolean setRight(E element, int p) {
        if(this.right != null)
            if(this.right.getValue() == null){
                this.right.setValue(element, p);
                this.setValue(null, 0);
                return true;
            }
        return false;
    }

    public boolean setUpper(E element, int p) {
        if(this.upper != null)
            if(this.upper.getValue() == null){
                this.upper.setValue(element, p);
                this.setValue(null, 0);
                return true;
            }
        return false;
    }

    public boolean setLower(E element, int p) {
        if(this.lower != null)
            if(this.lower.getValue() == null){
                this.lower.setValue(element, p);
                this.setValue(null, 0);
                return true;
            }
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