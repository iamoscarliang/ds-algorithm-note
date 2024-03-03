package sorting;

public class QuickSort {

    public static void sort(int[] array) {
        if (array == null) {
            return;
        }
        quicksort(array, 0, array.length - 1);
    }

    private static void quicksort(int[] array, int low, int high) {
        if (low < high) {
            int splitPoint = partition(array, low, high);
            quicksort(array, low, splitPoint);
            quicksort(array, splitPoint + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[low];
        int i = low - 1;
        int j = high + 1;
        while (true) {
            do {
                i++;
            } while (array[i] < pivot);

            do {
                j--;
            } while (array[j] > pivot);

            if (i < j) {
                swap(array, i, j);
            } else {
                return j;
            }
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = {17, 5, 12, 2, 1, 9, 10};
        sort(array);
        System.out.println(java.util.Arrays.toString(array));
    }

}