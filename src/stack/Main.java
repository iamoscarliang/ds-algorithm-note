package stack;

public class Main {

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();

        stack.push("Java");
        stack.push("Kotlin");
        System.out.println(stack.peek());

        stack.pop();
        System.out.println(stack.peek());

        stack.push("Python");
        System.out.println(stack.peek());
        System.out.println(stack.size());
    }

}
