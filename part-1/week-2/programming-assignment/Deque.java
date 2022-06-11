import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first, last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        this.size += 1;

        if (oldFirst == null) {
            last = first;
            first.next = null;
        } else {
            oldFirst.prev = first;
            first.next = oldFirst;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        this.size += 1;

        if (isEmpty()) {
            first = last;
            last.prev = null;
        } else {
            oldLast.next = last;
            last.prev = oldLast;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();

        Item item = first.item;
        first = first.next;
        this.size -= 1;

        if (isEmpty()) last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) throw new NoSuchElementException();

        Item item = last.item;
        last = last.prev;
        this.size -= 1;

        if (last == null) first = null;
        else last.next = null;
        return item;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur = first;

        public boolean hasNext() {  return cur != null;  }

        public void remove() {  throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        deque.addFirst("is");
        deque.addFirst("this");
        deque.addLast("awesome!");
        StdOut.println(deque.size());

        for (String str : deque) {
            StdOut.println(str);
        }
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        deque.addLast("cool!");
        StdOut.println(deque.removeFirst());
    }
}
