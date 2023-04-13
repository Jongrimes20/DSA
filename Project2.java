import java.util.Arrays;

public class Project2 {
    /*
     * Conclsion: With these implementations and using an Integer array both Mergesort and Quicksort
     * are equally efficent
     */
    public static void main(String[] args) {
        long t1, t2;

        Integer[] arr = {5, 2, 1, 6, 3, 7, 10, 12, 34, 23, 4, 97, 43 , 54, 34, 53,24, 52, 75, 100, 454, 34, 422,564, 345, 565, 965, 294, 6323, 3234, 543, 434, 423, 324, 32, 354};
        Integer[] arr2 = arr;

        t1 = System.currentTimeMillis();
        arr = mergesort(arr);
        t2 = System.currentTimeMillis();
        System.out.println(Arrays.toString(arr));
        System.out.println(t2-t1);

        t1 = System.currentTimeMillis();
        arr2 = Quicksort(arr2);
        t2 = System.currentTimeMillis();
        System.out.println(Arrays.toString(arr2));
        System.out.println(t2-t1);
    }

    //MERGESORT 
    /*
     * Mergesort Driver
     * @param a - array to be sorted
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType[] mergesort(AnyType[] a) {
        AnyType[] tempArray = Arrays.copyOf(a, a.length);
        mergesort(a, tempArray, 0, a.length - 1);
        return a;
    }

    /*
     * Mergesort
     * @param a - array of compareable items
     * @param tempArr - array to hold merged results
     * @param left - L-most index 
     * @param right - R-most index
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void mergesort(AnyType[] a, AnyType[] tempArr, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergesort(a, tempArr, left, center);
            mergesort(a, tempArr, center+1, right);
            merge(a, tempArr, left, center+1, right);
        }
    }

     /*
     * Merge method
     * @param a - an array of compareable items
     * @param tempArr - array to store merged results
     * @param lPos - left most index
     * @param rPos - start of second half
     * @param rEnd - right most index
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void merge(AnyType[] a, AnyType[] tempArr, int lPos, int rPos, int rEnd) {
        int lEnd = rPos - 1;
        int tempPos = lPos;
        int numElements = rEnd - lPos + 1;

        while (lPos <= lEnd && rPos <= rEnd) {
            if(a[lPos].compareTo(a[rPos]) <= 0) {
                tempArr[tempPos++] = a[lPos++];
            }
            else {
                tempArr[tempPos++] = a[rPos++];
            }
        }

        while (lPos <= lEnd) {
            tempArr[tempPos++] = a[lPos++];
        }

        while(rPos <= rEnd) {
            tempArr[tempPos++] = a[rPos++];
        }

        for(int i=0; i < numElements; i++, rEnd--) {
            a[rEnd] = tempArr[rEnd];
        }
    }

    //QUICKSORT
    /*
     * Quicksort
     * @param a - an array of comparible items
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType[] Quicksort(AnyType[] a) {
        Quicksort(a, 0, a.length-1);
        return a;
    }

    private static <AnyType> void swapReferences(AnyType[] a, int i, int j) {
        AnyType temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /*
     * Median-of-Three partitioning
     * Returns median of left, center, and right
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType median3(AnyType[] a, int left, int right) {

        int center = (left + right) / 2;
        if (a[center].compareTo(a[left]) < 0) {
            swapReferences(a, left, center);
        }
        if (a[right].compareTo(a[left]) < 0) {
            swapReferences(a, left, right);
        }
        if (a[right].compareTo(a[center]) < 0) {
            swapReferences(a, center, right);
        }

        //Place pivot at right - 1
        swapReferences(a, center, right - 1);
        return a[right - 1];
    }

    /*
     * Quicksort Algorithm
     * Uses median of three and a cutoff of 15
     * @param a - array of compareable item
     * @param left - left most index
     * @param right - right most index
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void Quicksort(AnyType[] a, int left, int right) {
        if (left + 15 <= right) {
            AnyType v = median3(a, left, right);

            //Begin Partitioning
            int i = left, j = right - 1;
            for ( ; ; ) {
                while(a[++i].compareTo(v) < 0) {}
                while(a[--j].compareTo(v) > 0) {}

                if (i<j) {
                    swapReferences(a, i, j);
                }else{ break; }
            }

            swapReferences(a, i, right-1); //restore pivot

            Quicksort(a, left, i-1);
            Quicksort(a, i+1, right);
        } else {
            insertionSort(a);
        }
    }

    //insertion sort
    private static <AnyType extends Comparable<? super AnyType>>
    void insertionSort(AnyType[] a) {
        int j;

        for (int p = 1; p < a.length; p++) {
            AnyType tmp = a[p];
            for (j=p; j > 0 && tmp.compareTo(a[j-1]) < 0; j--) {
                a[j] = a[j-1];
            }
            a[j] = tmp;
        }
    }
}
