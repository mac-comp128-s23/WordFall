import edu.macalester.graphics.*;
import java.util.Iterator;

/**
 * Quadruply linked nodes have four links to other nodes. One for the upper, lower, left, and right nodes.
 * These make up the grid which will be used for storing letters in our game.
 */
public class QNode<E> {
    private QNode<E> upper, lower, left, right;
    private E value;
    private GraphicsGroup group;

    public QNode() {
        this.upper = null;
        this.lower = null;
        this.left = null;
        this.right = null;
        this.value = null;
        this.group = null;
    }

    public GraphicsGroup getGraphics() {
        return group;
    }

    public void setGraphics(GraphicsGroup g) {
        group = g;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E element) {
        Iterator it = group.iterator();
        ((GraphicsText)it.next()).setText((String)element);

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
            if(this.lower.value == null){
                this.lower.value = element;
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