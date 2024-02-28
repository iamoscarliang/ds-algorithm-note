package queue;

public class Main {

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();

        queue.offer("Java");
        queue.offer("Kotlin");
        System.out.println(queue.peek());

        queue.poll();
        System.out.println(queue.peek());

        queue.offer("Python");
        System.out.println(queue.peek());
        System.out.println(queue.size());
    }

}