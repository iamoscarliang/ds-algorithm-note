package arraylist;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>(2);

        arrayList.add("Java");
        arrayList.add("Kotlin");
        System.out.println(arrayList);

        arrayList.removeAt(0);
        System.out.println(arrayList);

        arrayList.add("Python");
        arrayList.add("C++");
        System.out.println(arrayList);
        System.out.println(arrayList.get(1));
    }

}