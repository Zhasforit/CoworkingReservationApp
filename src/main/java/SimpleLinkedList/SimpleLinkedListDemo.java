package SimpleLinkedList;

public class SimpleLinkedListDemo {
    public static void main(String[] args) {
        SimpleLinkedList<String> list = new SimpleLinkedList<String>();

        list.add("Apple");
        list.add("Banana");
        list.add(1, "Orange");

        System.out.println(list); // [Apple, Orange, Banana]
        System.out.println("Size: " + list.size()); // 3

        System.out.println("Element at index 1: " + list.get(1)); // Orange

        list.remove(0);
        System.out.println("After removal: " + list); // [Orange, Banana]
    }
}
