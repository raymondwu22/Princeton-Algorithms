import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] coreArray;
    private int size;

    public RandomizedQueue() {
        this.coreArray = (Item[]) new Object[10];
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The addition cannot be null");
        }

        if (size == coreArray.length) {
            resize(2 * coreArray.length);
        }

        coreArray[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Queue Is Empty!");
        }

        int popIndex = StdRandom.uniform(size);
        Item tempStore = coreArray[popIndex];
        coreArray[popIndex] = coreArray[--size];

        if (size < coreArray.length / 4) {
            resize(coreArray.length / 2);
        }

        return tempStore;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Queue Is Empty!");
        }

        int popIndex = StdRandom.uniform(size);

        return coreArray[popIndex];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private final int[] shuffledArray;
        private int pointer;

        public RandomizedQueueIterator() {
            this.shuffledArray = new int[size];
            this.pointer = 0;

            for (int i = 0; i < shuffledArray.length; i++) {
                shuffledArray[i] = i;
            }
            StdRandom.shuffle(shuffledArray);
        }

        public boolean hasNext() {
            return pointer != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No such element exception.");
            }

            return (Item) coreArray[shuffledArray[pointer++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No such method.");
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = coreArray[i];
        }

        coreArray = copy;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.dequeue();

        Iterator<Integer> iterator = randomizedQueue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next() + " ");
        }
    }
}
