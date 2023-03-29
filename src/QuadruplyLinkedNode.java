import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EventObject;
import java.util.function.Consumer;

import org.w3c.dom.events.Event;

/**Class for the individual nodes
 * S
 */

 public class QuadruplyLinkedNode<E> {
    private QuadruplyLinkedNode<E> upper, lower, left, right;
    private E value;

    public QuadruplyLinkedNode() {
        this.upper = null;
        this.lower = null;
        this.left = null;
        this.right = null;
        this.value = null;
    }

    public QuadruplyLinkedNode<E> getLeft() {
        return left;
    }

    public QuadruplyLinkedNode<E> getRight() {
        return right;
    }

    public QuadruplyLinkedNode<E> getUpper() {
        return upper;
    }

    public QuadruplyLinkedNode<E> getLower() {
        return lower;
    }

    public void setValue(E element) {
        this.value = element;
    }

    public void setLeft(E element) {
        this.left.setValue(element);
    }

    public void setRight(E element) {
        this.right.setValue(element);
    }

    public void setUpper(E element) {
        this.upper.setValue(element);
    }

    public void setLower(E element) {
        this.lower.setValue(element);
    }

    public void linkLeft(QuadruplyLinkedNode<E> node) {
        this.left = node;
    }

    public void linkRight(QuadruplyLinkedNode<E> node) {
        this.right = node;
    }

    public void linkUpper(QuadruplyLinkedNode<E> node) {
        this.upper = node;
    }

    public void linkLower(QuadruplyLinkedNode<E> node) {
        this.lower = node;
    }
 }