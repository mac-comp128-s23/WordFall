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

    // public void setValue(E element) {
    //     this.value = element;
    // }

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