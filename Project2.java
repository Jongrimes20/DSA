import java.util.*;

public class Project2 {
    /*
     * Results:
     * 
For a random array of length 1000
Mergesort time: 2 ms
Quicksort time: 3 ms
Mergesort is faster.

For a random array of length 10000
Mergesort time: 4 ms
Quicksort time: 81 ms
Mergesort is faster.

For a random array of length 100000
Mergesort time: 132 ms
Quicksort time: 7896 ms
Mergesort is faster.

For an almost sorted array of length 1000
Swaps = 10
Mergesort time: 0 ms
Quicksort time: 0 ms
Mergesort and Quicksort are equally efficient.

For an almost sorted array of length 10000
Swaps = 100
Mergesort time: 8 ms
Quicksort time: 47 ms
Mergesort is faster.

For an almost sorted array of length 100000
Swaps = 1000
Mergesort time: 94 ms
Quicksort time: 3613 ms
Mergesort is faster.

CONCLUSION: Mergesort is the quicker and more efficent algorithm
     */
    public static void main(String[] args) {
        randomTester(1000, 10000, 100000);
        almostSortedTester(1000, 10000, 100000);

        System.out.println("FOR EXTRA CREDIT");
        int[][] points = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}, {8, 8}, {9, 9}, {10, 10}, {11, 11}, {12, 12}, {13, 13}, {14, 14}};
        List<List<Integer>> collinearPoints = findCollinearPoints(points);
        System.out.println(collinearPoints);
    }

    //Helper funcs
    private static void randomTester(int l1, int l2, int l3) {
        long t1, t2, mergeTime, quickTime;
        int length = 0;
        Integer[] arr1 = {};
        Integer[] arr2 = {};

        for(int i = 1; i <= 3; i ++) {
            if (i==1) {
                length = l1;
            }
            if (i==2) {
                length = l2;
            }
            if (i ==3) {
                length = l3;
            }
            arr1 = generateRandomArray(length);
            arr2 = arr1;

            t1 = System.currentTimeMillis();
            mergesort(arr1);
            t2 = System.currentTimeMillis();
            mergeTime = t2-t1;

            t1 = System.currentTimeMillis();
            Quicksort(arr1);
            t2 = System.currentTimeMillis();
            quickTime = t2-t1;

            System.out.println("For a random array of length " + length);
            System.out.printf("Mergesort time: %d ms\n", mergeTime);
            System.out.printf("Quicksort time: %d ms\n", quickTime);

            if (mergeTime < quickTime) {
                System.out.println("Mergesort is faster.\n");
            } else if (quickTime < mergeTime) {
                System.out.println("Quicksort is faster.\n");
            } else {
                System.out.println("Mergesort and Quicksort are equally efficient.\n");
            }
        }
    }

    private static void almostSortedTester(int l1, int l2, int l3) {
        long t1, t2, mergeTime, quickTime;
        int length = 0;
        Integer[] arr1 = {};
        Integer[] arr2 = {};

        for(int i = 1; i <= 3; i ++) {
            if (i==1) {
                length = l1;
            }
            if (i==2) {
                length = l2;
            }
            if (i ==3) {
                length = l3;
            }

            arr1 = createAlmostSortedArray(length, length/100);
            arr2 = arr1;

            t1 = System.currentTimeMillis();
            mergesort(arr1);
            t2 = System.currentTimeMillis();
            mergeTime = t2-t1;

            t1 = System.currentTimeMillis();
            Quicksort(arr1);
            t2 = System.currentTimeMillis();
            quickTime = t2-t1;

            System.out.println("For an almost sorted array of length " + length);
            System.out.println("Swaps = " + length/100);
            System.out.printf("Mergesort time: %d ms\n", mergeTime);
            System.out.printf("Quicksort time: %d ms\n", quickTime);

            if (mergeTime < quickTime) {
                System.out.println("Mergesort is faster.\n");
            } else if (quickTime < mergeTime) {
                System.out.println("Quicksort is faster.\n");
            } else {
                System.out.println("Mergesort and Quicksort are equally efficient.\n");
            }
        }
    }

    private static Integer[] generateRandomArray(int size) {
        Random rand = new Random();
        Integer[] arr = new Integer[size];
        
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        return arr;
    }

    private static Integer[] createAlmostSortedArray(int size, int maxSwaps) {
        Random rand = new Random();
        Integer[] arr = new Integer[size];
        
        // create a sorted array
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        
        // perform random swaps
        for (int i = 0; i < maxSwaps; i++) {
            int index1 = rand.nextInt(size);
            int index2 = rand.nextInt(size);
            
            int temp = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = temp;
        }
        
        return arr;
    }

    private static long measureSortingTime(Integer[] arr, Function<Integer[], Integer[]> sortingFunction) {
        long t1 = System.currentTimeMillis();
        sortingFunction.apply(arr);
        long t2 = System.currentTimeMillis();
        return t2 - t1;
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

    //EXTRA CREDIT
    public static List<List<Integer>> findCollinearPoints(int[][] points) {
        List<List<Integer>> collinearPoints = new ArrayList<List<Integer>>();
        
        // Sort the points by their x-coordinates
        Arrays.sort(points, Comparator.comparingInt(a -> a[0]));
        
        // Loop through each set of three points
        for (int i = 0; i < points.length - 2; i++) {
            int[] p1 = points[i];
            for (int j = i + 1; j < points.length - 1; j++) {
                int[] p2 = points[j];
                double slope = (double) (p2[1] - p1[1]) / (double) (p2[0] - p1[0]);
                List<Integer> currentCollinearPoints = new ArrayList<Integer>();
                currentCollinearPoints.add(i);
                currentCollinearPoints.add(j);
                for (int k = j + 1; k < points.length; k++) {
                    int[] p3 = points[k];
                    double currentSlope = (double) (p3[1] - p1[1]) / (double) (p3[0] - p1[0]);
                    if (currentSlope == slope) {
                        currentCollinearPoints.add(k);
                    }
                }
                if (currentCollinearPoints.size() >= 4) {
                    collinearPoints.add(currentCollinearPoints);
                }
            }
        }
        
        return collinearPoints;
    }
}
