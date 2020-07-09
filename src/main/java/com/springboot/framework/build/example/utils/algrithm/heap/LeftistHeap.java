package com.springboot.framework.build.example.utils.algrithm.heap;

import java.nio.BufferUnderflowException;

/**************************************************************
 * 创建日期：2020/1/18 14:40
 * 作    者：lixuhong
 * 功能描述：左式堆
 * 具有二叉堆的结构及性质，但是随着数据的增多，左树的深度加深，而右数则不会增加太多
 *
 * 斜堆：在左式堆上做一点小改变
 * 不再判断左右儿子是否满足左式堆结构性质，直接进行交换（除右路径上所有节点的最大者不交换它的左右儿子）
 **************************************************************/
public class LeftistHeap<AnyType extends Comparable<? super AnyType>> {

    public LeftistHeap(){
        root = null;
    }

    /**
     * 合并
     * @param rhs
     */
    public void merge(LeftistHeap<AnyType> rhs) {

        // 避免混淆
        if (this == rhs) {
            return;
        }

        root = merge(root, rhs.root);
        rhs.root = null;
    }

    /**
     * 插入数据
     * @param x
     */
    public void insert(AnyType x) {

        root = merge(new Node<>(x), root);
    }

    /**
     * 查询最小值
     * @return
     */
    public AnyType findMin() {
        if( isEmpty( ) ) {
            throw new BufferUnderflowException( );
        }

        return root.element;
    }

    /**
     * 删除最小值
     * @return
     */
    public AnyType deleteMin() {

        if( isEmpty( ) ) {
            throw new BufferUnderflowException( );
        }

        AnyType minItem = root.element;
        root = merge(root.left, root.right);

        return minItem;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public void makeEnpty(){
        root = null;
    }

    private static class Node<AnyType> {

        /** 节点数据 */
        AnyType element;
        /** 左孩子 */
        Node<AnyType> left;
        /** 右孩子 */
        Node<AnyType> right;
        /** 零路径长*/
        int npl;

        Node(AnyType theElement){
            this(theElement, null, null);
        }

        public Node(AnyType element, Node<AnyType> left, Node<AnyType> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    /** root */
    private Node<AnyType> root;

    /**
     * 合并两个根
     * 处理异常情况并调用递归合doMerge
     * @param h1
     * @param h2
     * @return
     */
    private Node<AnyType> merge(Node<AnyType> h1, Node<AnyType> h2) {

        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }
        if (h1.element.compareTo(h2.element) < 0) {
            return doMerge(h1, h2);
        } else {
            return doMerge(h2, h1);
        }
    }

    /**
     * 合并两个根(实际操作)
     * 假设树不是空的，且h1的根包含最小的项
     * @param h1
     * @param h2
     * @return
     */
    private Node<AnyType> doMerge(Node<AnyType> h1, Node<AnyType> h2) {

        if (h1.left == null) {
            // 单个节点
            h1.left = h2;
        } else {
            h1.right = merge(h1.right, h2);
            if (h1.left.npl < h1.right.npl) {
                swapChildren(h1);
            }
            h1.npl = h1.right.npl + 1;
        }

        return h1;
    }

    /**
     * 交换节点的两个孩子
     * @param t
     * @return
     */
    private Node<AnyType> swapChildren(Node<AnyType> t) {

        Node<AnyType> tmp = t.left;
        t.left = t.right;
        t.right = tmp;

        return t;
    }
}
