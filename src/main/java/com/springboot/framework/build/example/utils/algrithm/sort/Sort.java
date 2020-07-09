package com.springboot.framework.build.example.utils.algrithm.sort;

import org.etsi.uri.x01903.v13.AnyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**************************************************************
 * 创建日期：2020/2/12 17:59
 * 作    者：lixuhong
 * 功能描述：各类排序
 *           1. 插入排序
 *           2. 希尔排序
 *           3. 堆排序
 *           4. 归并排序
 *           5. 快速排序
 *           6. 快速选择排序
 *
 *
 **************************************************************/
public final class Sort {


    /**
     * 插入排序
     * <p>
     * O(N * N)
     * 适合对小规模数据进行排序
     * 对于数列arr，从第二个数开始，在前面的数中比较排序（默认前面的数已经排序过）
     *
     * @param arr
     * @param <AnyType>
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void insertionSort(AnyType[] arr) {

        int j;
        for (int p = 1; p < arr.length; p++) {
            AnyType tmp = arr[p];
            // 在前面的数中排序
            for (j = p; j > 0 && tmp.compareTo(arr[j - 1]) < 0; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }


    /**
     * 希尔排序
     * <p>
     * 适合对大规模数据进行排序
     * 希尔排序的思想是使数组中任意间隔为h的元素都是有序的。这样的数组被称为h有序数组。
     * 换句话说，一个h有序数组就是h个互相独立的有序数组编制在一起组成的一个数组。
     * 在进行排序的时候，如果h很大，我们能够将元素移动到很远的地方，为了实现更小的h有序创造方便，
     * 用这种方式，对于任意以1结尾的h序列，我们都能够将数组排序，这就是希尔排序。
     * <p>
     * 希尔排序是把记录按下标的一定增量分组，对每组使用直接插入排序算法排序；随着增量逐渐减少，每组包含的关键词越来越多，当增量减至1时，整个文件恰被分成一组，算法便终止
     * 增量序列的一个流行的选择是使用Shell建议的序列：hi = [N / 2]和 hk = [h(k+1) / 2]
     * <p>
     * 例：
     * 数组：8 9 1 7 2 3 5 4 6 0 --- 10个数字
     * <1>
     * h = 10/2 = 5:分组并使用插入排序对各组进行排序: [8, 3], [9, 5], [1, 4], [7, 6], [2, 0] --> [3, 8], [5, 9], [1, 4], [6, 7], [0, 2]
     * --> 结果：3 5 1 6 0 8 9 4 7 2
     * <2>
     * h = 5/2 = 2:分组并使用插入排序对各组进行排序: [3, 1, 0, 9, 7], [5, 6, 8, 4, 2] --> [0, 1, 3, 7, 9], [2, 4, 5, 6, 8]
     * --> 结果：0 2 1 4 3 5 7 6 9 8
     * <3>
     * h = 2/2 = 1:分组并使用插入排序对各组进行排序: [0, 2, 1, 4, 3, 5, 7, 6, 9, 8] --> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     * --> 结果：0 1 2 3 4 5 6 7 8 9
     * h=1时：只需要对数列进行简单的微调，不需要大量的移动操作即可完成整个数组的排序
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void shellSort(AnyType[] arr) {

        for (int gap = arr.length / 2; gap > 0; gap /= 2) {

            // 使用插入排序，使按增量分组的各组的数据有序
            int j;
            for (int p = gap; p < arr.length; p++) {
                AnyType tmp = arr[p];
                for (j = p; j >= gap && tmp.compareTo(arr[j - gap]) < 0; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }
        }
    }


    /**
     * 堆排序
     * <p>
     * （类似优先队列）
     *
     * @param arr
     * @param <AnyType>
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void heapSort(AnyType[] arr) {

        // 构建有序堆
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            percolateDown(arr, i, arr.length);
        }
        // 排序
        for (int i = arr.length - 1; i > 0; i--) {
            // 删除最大值
            AnyType tmp = arr[0];
            arr[0] = arr[i];
            arr[i] = tmp;

            percolateDown(arr, 0, i);
        }
    }

    /**
     * 获取左孩子的索引
     *
     * @param i 堆中项的索引
     * @return 返回左孩子的索引
     */
    private static int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * 下滤操作
     * 在deleteMax和buildHeap中使用的heapsort的内部方法。
     *
     * @param arr       是可比较项的数组
     * @param i         需渗透的位置
     * @param n         堆的逻辑大小
     * @param <AnyType>
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void percolateDown(AnyType[] arr, int i, int n) {

        int child;
        AnyType tmp;

        for (tmp = arr[i]; leftChild(i) < n; i = child) {
            child = leftChild(i); // 左孩子下标
            // 获取最小元素位置
            if (child != n - 1 && arr[child].compareTo(arr[child + 1]) < 0) {
                child++;
            }
            // 节点元素小于左孩子元素，即找到新的元素作为根
            if (tmp.compareTo(arr[child]) < 0) {
                arr[i] = arr[child];
            } else {
                // 找到元素插入位置或者已存在
                break;
            }
        }
        arr[i] = tmp;
    }


    /**
     * 归并排序
     * <p>
     * 将需要排序的数前后分成两部分并排序，排序后得到A，B数组
     * 基本的合并算法是取两个输入数组A和B，一个输出数组C，以及三个计数器Actr, Bctr, Cctr,它们初始置于数组开始端
     * A[Actr]和B[Bctr]中较小的数输入C中的下一位置，相关计数器向前推进。当两个输入表有一个用完的时候，另一个表中数据全部输入C
     *
     * @param arr
     * @param <AnyType>
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void mergeSort(AnyType[] arr) {

        AnyType[] tmpArray = (AnyType[]) new Comparable[arr.length];
        mergeSort(arr, tmpArray, 0, arr.length - 1);
    }

    /**
     * 进行递归调用的内部方法
     *
     * @param arr       可比较项的数组
     * @param tmpArray  一个放置合并结果的数组
     * @param left      离开子数组的最左侧索引
     * @param right     在子数组的最右边索引处
     * @param <AnyType>
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void mergeSort(AnyType[] arr, AnyType[] tmpArray, int left, int right) {

        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(arr, tmpArray, left, center); // 左子数组合并排序
            mergeSort(arr, tmpArray, center + 1, right); // 右子数组合并排序
            merge(arr, tmpArray, left, center + 1, right); // 左右子数组合并
        }
    }

    /**
     * 合并子数组的两半部分的内部方法
     *
     * @param arr       可比较项的数组
     * @param tmpArray  一个放置合并结果的数组
     * @param leftPos   子数组的最左侧索引
     * @param rightPos  右子数组开始的索引
     * @param rightEnd  子数组的最右边索引
     * @param <AnyType>
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void merge(AnyType[] arr, AnyType[] tmpArray, int leftPos, int rightPos, int rightEnd) {

        int leftEnd = rightPos - 1;
        int tmpPos = leftPos; // tmpArray的索引
        int numElements = rightEnd - leftPos + 1; // 包含元素数量

        // 合并
        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (arr[leftPos].compareTo(arr[rightPos]) <= 0) {
                tmpArray[tmpPos++] = arr[leftPos++];
            } else {
                tmpArray[tmpPos++] = arr[rightPos++];
            }
        }

        // 复制其余的左半部分
        while (leftPos <= leftEnd) {
            tmpArray[tmpPos++] = arr[leftPos++];
        }

        // 复制其余的右半部分
        while (rightPos <= rightEnd) {
            tmpArray[tmpPos++] = arr[rightPos++];
        }

        // 将tmpArray复制回arr
        for (int i = 0; i < numElements; i++, rightEnd--) {
            arr[rightEnd] = tmpArray[rightEnd];
        }
    }


    /**
     * 经典快速排序
     *
     * @param arr
     * @param <AnyType>
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void quickSort(AnyType[] arr) {

        quickSort(arr, 0, arr.length - 1);
    }

    private static final int CUTOFF = 3;

    /**
     * 交换元素
     *
     * @param arr
     * @param index1
     * @param index2
     * @param <AnyType>
     */
    private static <AnyType> void swapReferences(AnyType[] arr, int index1, int index2) {
        AnyType tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

    /**
     * 三数中值选取法
     * <p>
     * 相比较直接选取数组中值作为枢纽元更有效率
     * 选取数组左、中、右元素，并按从小到大排列在这三个位置
     *
     * @param arr       数组
     * @param left
     * @param right
     * @param <AnyType>
     * @return 返回左，中和右的中值
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType median3(AnyType[] arr, int left, int right) {

        int center = (left + right) / 2; // 中间元素
        if (arr[center].compareTo(arr[left]) < 0) {
            swapReferences(arr, left, center);
        }
        if (arr[right].compareTo(arr[left]) < 0) {
            swapReferences(arr, left, right);
        }
        if (arr[right].compareTo(arr[center]) < 0) {
            swapReferences(arr, center, right);
        }

        // 将枢轴放置在右侧位置-1
        swapReferences(arr, center, right - 1);

        return arr[right - 1];
    }

    /**
     * 内部quicksort方法，可进行递归调用
     * 使用3分位数的中位数和10的临界值
     *
     * @param arr       可比较项的数组
     * @param left      离开子数组的最左侧索引
     * @param right     在子数组的最右边索引处
     * @param <AnyType>
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void quickSort(AnyType[] arr, int left, int right) {

        if (left + CUTOFF <= right) {

            // 枢纽元
            AnyType pivot = median3(arr, left, right);

            // 开始分区
            int i = left, j = right - 1;
            for (; ; ) {
                // 小于枢纽元素
                while (arr[++i].compareTo(pivot) < 0) {
                }
                // 大于枢纽元素
                while (arr[--j].compareTo(pivot) > 0) {
                }
                // 两元素位置比较
                if (i < j) {
                    swapReferences(arr, i, j);
                } else {
                    break; // 需要交换两元素位置
                }
            }

            swapReferences(arr, i, right - 1);   // Restore pivot

            quickSort(arr, left, i - 1);    // Sort small elements
            quickSort(arr, i + 1, right);   // Sort large elements
        } else {
            // 插入排序
            for (int p = left + 1; p <= right; p++) {
                AnyType tmp = arr[p];
                int j;

                for (j = p; j > left && tmp.compareTo(arr[j - 1]) < 0; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[j] = tmp;
            }
        }
    }


    /**
     * 快速选择排序
     * <p>
     * 与快速排序相比，只做一次递归调用
     *
     * @param arr
     * @param <AnyType>
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void quickSelectSort(AnyType[] arr) {
        quickSelectSort(arr, 0, arr.length - 1, arr.length / 2);
    }

    /**
     * 内部quicksort方法，可进行递归调用
     * 使用3分位数的中位数和10的临界值
     *
     * @param arr       可比较项的数组
     * @param left      离开子数组的最左侧索引
     * @param right     在子数组的最右边索引处
     * @param k         整个数组的索引
     * @param <AnyType>
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void quickSelectSort(AnyType[] arr, int left, int right, int k) {

        if (left + CUTOFF <= right) {

            // 枢纽元
            AnyType pivot = median3(arr, left, right);

            // 开始分区
            int i = left, j = right - 1;
            for (; ; ) {
                // 小于枢纽元素
                while (arr[++i].compareTo(pivot) < 0) {
                }
                // 大于枢纽元素
                while (arr[--j].compareTo(pivot) > 0) {
                }
                // 两元素位置比较
                if (i < j) {
                    swapReferences(arr, i, j);
                } else {
                    break; // 需要交换两元素位置
                }
            }

            swapReferences(arr, i, right - 1);   // Restore pivot

            if (k <= i) {
                quickSelectSort(arr, left, i - 1, k);    // Sort small elements
            } else if (k > i + 1){
                quickSelectSort(arr, i + 1, right, k);   // Sort large elements
            }
        } else {
            // 插入排序
            for (int p = left + 1; p <= right; p++) {
                AnyType tmp = arr[p];
                int j;

                for (j = p; j > left && tmp.compareTo(arr[j - 1]) < 0; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[j] = tmp;
            }
        }
    }


    public static void main(String[] args) {

        Integer[] arr = {8, 9, 1, 7, 2, 3, 5, 4, 6, 0};
        quickSelectSort(arr);
        for (Integer i : arr) {
            System.out.print(i);
        }
    }

}
