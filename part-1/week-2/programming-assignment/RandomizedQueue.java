import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int curSize;

    private void resizeArr(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < curSize; ++i) {
            copy[i] = arr[i];
        }
        arr = copy;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
        curSize = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return curSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return curSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (curSize == arr.length) {
            resizeArr(2 * arr.length);
        }
        arr[curSize++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        // swap the removed element with the last element in the array
        int removedId = StdRandom.uniform(curSize);
        Item item = arr[removedId];
        arr[removedId] = arr[--curSize];
        arr[curSize] = null;

        if (curSize > 0 && curSize == arr.length / 4) resizeArr(arr.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return arr[StdRandom.uniform(curSize)];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] arrCopied; // handle parallel iterators and nested iterators
        private int size = curSize;

        public RandomizedQueueIterator() {
            size = curSize;
            arrCopied = (Item[]) new Object[curSize];
            for (int i = 0; i < curSize; ++i) {
                arrCopied[i] = arr[i];
            }
        }

        public boolean hasNext() {  return size > 0;  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            int id = StdRandom.uniform(size);
            Item item = arrCopied[id];
            size -= 1;
            // swap the element at 'id' and the last element
            arrCopied[id] = arrCopied[size];
            arrCopied[size] = item;

            return item;
        }

        public void remove() {  throw new UnsupportedOperationException();  }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        StdOut.println(queue.isEmpty());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        StdOut.println("===== Test size() =====");
        StdOut.println(queue.size());
        StdOut.println("===== Test sample() =====");
        StdOut.println(queue.sample());
        StdOut.println("===== Test iterator() =====");
        for (int num : queue) {
            StdOut.println(num);
        }
        StdOut.println("===== Test dequeue() =====");
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());

        for (int num : queue) {
            StdOut.println(num);
        }

        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        // TODO: remove till the end
    }

}