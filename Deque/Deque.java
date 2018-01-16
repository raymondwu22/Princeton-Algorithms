import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private final Node head;
    private final Node tail;
    private int size;

    private class Node {
        Item value;
        Node next;
        Node prev;

        public Node(Item item) {
            this.value = item;
            this.next = null;
            this.prev = null;
        }
    }

    public Deque() {
        this.head = new Node(null);
        this.tail = new Node(null);
        head.next = tail;
        tail.prev = head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }

    public int size() {
        return  size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The addition cannot be null");
        }

        Node n = new Node(item);
        Node current = head.next;
        n.next = current;
        n.prev = head;
        head.next = n;
        n.next.prev = n;

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The addition cannot be null");
        }

        Node n = new Node(item);
        n.next = tail;
        n.prev = tail.prev;
        n.prev.next = n;
        tail.prev = n;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Deque Is Empty!");
        }

        Node n = head.next;
        head.next = n.next;
        n.next.prev = head;
        n.next = null;
        n.prev = null;
        size--;

        return n.value;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Deque Is Empty!");
        }

        Node n = tail.prev;
        tail.prev = n.prev;
        n.prev.next = tail;
        n.next = null;
        n.prev = null;
        size--;

        return n.value;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>();
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node current = head.next;

        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No such element exception.");
            }

            Item item = (Item) current.value;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No such method.");
        }
    }

    public static void main(String[] args) {
        Deque<Integer> test = new Deque<Integer>();
        test.addFirst(1);
        test.addFirst(2);
        test.addLast(3);

        Iterator<Integer> iterator = test.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next() + " ");
        }
    }
}
