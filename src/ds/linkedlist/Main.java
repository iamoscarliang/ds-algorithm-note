package ds.linkedlist;

public class Main {

    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();

        linkedList.addLast("Java");
        linkedList.addLast("Kotlin");
        linkedList.addFirst("Python");
        System.out.println(linkedList);

        linkedList.removeLast();
        linkedList.remove("Java");
        System.out.println(linkedList);
    }

}