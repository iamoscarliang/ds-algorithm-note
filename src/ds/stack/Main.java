package ds.stack;

public class Main {

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();

        stack.push("Java");
        stack.push("Kotlin");
        System.out.println(stack);

        stack.pop();
        System.out.println(stack);

        stack.push("Python");
        System.out.println(stack);
        System.out.println(stack.peek());
    }

}
