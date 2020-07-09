package com.springboot.framework.build.example.utils.algrithm.heap;

import java.nio.BufferUnderflowException;
import java.util.Comparator;

/**************************************************************
 * 创建日期：2020/1/18 10:16
 * 作    者：lixuhong
 * 功能描述：优先队列（二叉堆）
 * 堆序性质：
 * 所有的节点元素小于其后裔且最小元素在根处（便于快速查找最小元素）
 *
 * 使用数组代替二叉树
 * 对于数组中任一位置i上的元素，其左儿子在位置2i上，其右儿子在位置2i+1上，其父节点在位置i/2上
 * 因此，一个堆结构由一个数组和一个代表当前堆大小的整数组成
 * 二叉树结构：
 *              A
 *          B      C
 *        D  E    F G
 * 转换为数组结构：0下标元素不放数据，仅用于数据交换使用
 * null A   B   C   D   E   F   G   H   I   J
 * 0    1   2   3   4   5   6   7   8   9   10
 **************************************************************/
public class BinaryHeap<AnyType extends Comparable<? super AnyType>> {

    BinaryHeap() {
        this( DEFAULT_CAPACITY );
    }

    BinaryHeap(int capacity) {
        currentSize = 0;
        array = (AnyType[]) new Comparable[ capacity + 1 ];
    }

    /**
     * 给定一个项目数组，对二进制堆进行约束
     * @param items
     */
    BinaryHeap(AnyType[] items) {

        currentSize = items.length;
        array = (AnyType[]) new Comparator[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (AnyType item : items) {
            array[i++] = item;
        }
        buildHeap();
    }

    /**
     * 插入数据
     * 使用上滤策略：在需要插入数据的位置创建空穴
     * 若数据置入空穴不会破坏堆序结构，则完成
     * 否则，将空穴父节点置入空穴，父节点变更为新的空穴，重复以上操作，知道位置正确
     * @param x
     */
    public void insert(AnyType x) {

        // 达到条件扩展数组
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2 + 1);
        }

        // 上滤（从数组末尾位置向首位置查询即从二叉堆最大元素后插入数据向父节点迈进）
        // hole记录插入数据的正确位置
        // 需要插入的数据小于父节点数据;将父节点放入当前节点
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }

        // 插入数据
        array[hole] = x;
    }

    /**
     * 查找最小值
     */
    public AnyType findMin() {

        if( isEmpty( ) ) {
            throw new BufferUnderflowException( );
        }
        return array[ 1 ];
    }

    /**
     * 删除最小值
     * 使用下滤策略：删除元素使该位置编为空穴，将末尾元素置入空穴
     * 若满足堆序则完成
     * 否则，继续下滤
     */
    public AnyType deleteMin() {

        if (isEmpty()) {
            throw new BufferUnderflowException();
        }

        AnyType minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * 置空
     */
    public void makeEmpty() {
        currentSize = 0;
    }

    /** 堆当前大小 */
    private int currentSize;
    /** 堆数组 */
    private AnyType[] array;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 下滤操作（假设每个节点都有两个儿子）
     * @param hole
     */
    private void percolateDown(int hole) {

        int child;
        AnyType tmp = array[hole]; // 末尾元素

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            // 右孩子比左孩子小
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++;
            }
            // 将子节点元素小于末尾元素
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child];
            } else {
                // 找到末尾元素插入位置
               break;
            }
        }

        array[hole] = tmp;
    }

    /**
     * 建立一个任意的堆顺序属性
     */
    private void buildHeap() {

        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * 扩展二叉堆
     * @param newSize
     */
    private void enlargeArray(int newSize) {
        AnyType [] old = array;
        array = (AnyType []) new Comparable[ newSize ];
        for( int i = 0; i < old.length; i++ ) {
            array[ i ] = old[ i ];
        }
    }
}
