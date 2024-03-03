package ds.arraylist;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>(2);

        arrayList.add("Java");
        arrayList.add("Kotlin");
        System.out.println(arrayList);

        arrayList.set(1, "Python");
        arrayList.removeAt(0);
        System.out.println(arrayList);
    }

}