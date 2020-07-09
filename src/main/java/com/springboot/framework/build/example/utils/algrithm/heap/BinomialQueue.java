package com.springboot.framework.build.example.utils.algrithm.heap;

import java.nio.BufferUnderflowException;

/**************************************************************
 * 创建日期：2020/1/18 16:46
 * 作    者：lixuhong
 * 功能描述：二项队列
 **************************************************************/
public class BinomialQueue<AnyType extends Comparable<? super AnyType>> {

    BinomialQueue(){
        theTrees = new Node[ DEFAULT_TREES ];
        makeEmpty( );
    }

    BinomialQueue(AnyType item){
        currentSize = 1;
        theTrees = new Node[ 1 ];
        theTrees[ 0 ] = new Node<>( item, null, null );
    }

    /**
     * 合并
     * @param rhs rhs为空。 rhs必须与此不同
     */
    public void merge(BinomialQueue<AnyType> rhs) {
        if (this == rhs) {
            return;
        }

        currentSize += rhs.currentSize;

        if (currentSize > capacity()) {
            int maxLength = Math.max(theTrees.length, rhs.theTrees.length);
            expandTheTrees(maxLength + 1);
        }

        Node<AnyType> carry = null;
        for (int i = 0, j = 1; j <= currentSize; i++, j *= 2) {
            Node<AnyType> t1 = theTrees[ i ];
            Node<AnyType> t2 = i < rhs.theTrees.length ? rhs.theTrees[ i ] : null;

            int whichCase = t1 == null ? 0 : 1;
            whichCase += t2 == null ? 0 : 2;
            whichCase += carry == null ? 0 : 4;

            switch( whichCase )
            {
                case 0: /* No trees */
                case 1: /* Only this */
                    break;
                case 2: /* Only rhs */
                    theTrees[ i ] = t2;
                    rhs.theTrees[ i ] = null;
                    break;
                case 4: /* Only carry */
                    theTrees[ i ] = carry;
                    carry = null;
                    break;
                case 3: /* this and rhs */
                    carry = combineTrees( t1, t2 );
                    theTrees[ i ] = rhs.theTrees[ i ] = null;
                    break;
                case 5: /* this and carry */
                    carry = combineTrees( t1, carry );
                    theTrees[ i ] = null;
                    break;
                case 6: /* rhs and carry */
                    carry = combineTrees( t2, carry );
                    rhs.theTrees[ i ] = null;
                    break;
                case 7: /* All three */
                    theTrees[ i ] = carry;
                    carry = combineTrees( t1, t2 );
                    rhs.theTrees[ i ] = null;
                    break;
            }
        }

        for (int k = 0; k < rhs.theTrees.length; k++) {
            rhs.theTrees[k] = null;
        }

        rhs.currentSize = 0;
    }

    /**
     * 插入数据
     * 每次插入都创建一个新的二项树
     * @param x
     */
    public void insert(AnyType x) {
        merge(new BinomialQueue<>(x));
    }

    /**
     * 查找最小值
     * @return
     */
    public AnyType findMin(){
        if( isEmpty( ) ) {
            throw new BufferUnderflowException( );
        }

        return theTrees[ findMinIndex( ) ].element;
    }

    /**
     * 删除最小值
     * @return
     */
    public AnyType deleteMin(){

        if (isEmpty()) {
            throw new BufferUnderflowException();
        }

        int minIndex = findMinIndex();
        AnyType minItem = theTrees[minIndex].element;

        Node<AnyType> deleteTree = theTrees[minIndex].leftChild;

        // 构造 H'
        BinomialQueue<AnyType> deleteQueue = new BinomialQueue<>();
        deleteQueue.expandTheTrees(minIndex + 1);
        deleteQueue.currentSize = (1 << minIndex) - 1;

        for (int j = minIndex - 1; j >= 0; j--) {
            deleteQueue.theTrees[j] = deleteTree;
            deleteTree = deleteTree.nextSibling;
            deleteQueue.theTrees[j].nextSibling = null;
        }

        // 构造 H'
        theTrees[minIndex] = null;
        currentSize -= deleteQueue.currentSize + 1;

        merge(deleteQueue);

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

    private static class Node<AnyType> {

        /** 节点数据 */
        AnyType element;
        /** 左孩子 */
        Node<AnyType> leftChild;
        /** 右孩子 */
        Node<AnyType> nextSibling;

        Node(AnyType theElement){
            this(theElement, null, null);
        }

        public Node(AnyType element, Node<AnyType> leftChild, Node<AnyType> nextSibling) {
            this.element = element;
            this.leftChild = leftChild;
            this.nextSibling = nextSibling;
        }
    }

    private static final int DEFAULT_TREES = 1;
    /** 当前队列大小 */
    private int currentSize;
    /** 树根数组 */
    private Node<AnyType>[] theTrees;

    /**
     * 扩展
     * @param newNumTrees
     */
    private void expandTheTrees(int newNumTrees) {

        Node<AnyType> [ ] old = theTrees;
        int oldNumTrees = theTrees.length;

        theTrees = new Node[ newNumTrees ];
        for( int i = 0; i < Math.min( oldNumTrees, newNumTrees ); i++ ) {
            theTrees[ i ] = old[ i ];
        }
        for( int i = oldNumTrees; i < newNumTrees; i++ ) {
            theTrees[ i ] = null;
        }
    }

    /**
     * 合并同样大小的两棵二项树
     * @param t1
     * @param t2
     * @return
     */
    private Node<AnyType> combineTrees(Node<AnyType> t1, Node<AnyType> t2) {
        if (t1.element.compareTo(t2.element) > 0) {
            return combineTrees(t2, t1);
        }

        t2.nextSibling = t1.leftChild;
        t1.nextSibling = t2;
        return t1;
    }

    /**
     * 容量
     * @return
     */
    private int capacity() {
        return (1 << theTrees.length) - 1;
    }

    /**
     * 查找最小值的索引
     * @return
     */
    private int findMinIndex() {

        int i;
        int minIndex;

        for( i = 0; theTrees[ i ] == null; i++ ) {
            ;
        }

        for( minIndex = i; i < theTrees.length; i++ ) {
            if( theTrees[ i ] != null &&
                    theTrees[ i ].element.compareTo( theTrees[ minIndex ].element ) < 0 ) {
                minIndex = i;
            }
        }

        return minIndex;
    }
}
