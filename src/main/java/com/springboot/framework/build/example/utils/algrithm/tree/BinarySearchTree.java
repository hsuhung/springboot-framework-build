package com.springboot.framework.build.example.utils.algrithm.tree;

import java.nio.BufferUnderflowException;

/**
 * 二叉查找树（搜索树，二叉排序树）
 * 平均深度O(log N)
 *
 * 二叉树:平均深度O(√N)
 *
 * 1. 是二叉树
 * 2. 所有节点的值大于左子树中任意节点的值
 * 3. 所有节点的值小于于右子树中任意节点的值
 *
 *
 * 扩展：伸展树
 * 对于访问不久的节点再次访问很快O(Log N)，但是若每次访问树最深的节点则耗时很长O(M * Log N)
 * 每次访问一个节点都将该节点推送为根节点，对于下次访问很快
 * 节点推送使用平衡树的旋转(该操作为展开树，方便下次访问已访问过的节点)
 * 例：
 * 有平衡树树全都是左儿子：1 2 3 4 5 ... N; N为根节点
 * 访问1，使用平衡树旋转将1置为根，访问2是同样处理
 * 若依次从1访问至N，该平衡树的结构恢复到最开始时的结构，即都是左儿子，切N为根
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {

    /**
     * 二叉查找树节点
     * @param <AnyType>
     */
    private class BinaryNode<AnyType>{

        /** 节点数据 */
        AnyType element;
        /** 节点左子树 */
        BinaryNode<AnyType> left;
        /** 节点右子树 */
        BinaryNode<AnyType> right;

        public BinaryNode(AnyType element) {
            this(element, null, null);
        }

        public BinaryNode(AnyType element, BinaryNode<AnyType> left, BinaryNode<AnyType> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "BinaryNode{" +
                    "element=" + element +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BinarySearchTree{" +
                "root=" + root +
                '}';
    }

    /** 根节点 */
    private BinaryNode<AnyType> root;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * 根节点置空
     */
    public void makeEmpty() {
        this.root = null;
    }

    /**
     * 根节点是否为空
     * @return true：是；false：否
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * 是否存在包含该数据的节点
     * @param x 需要查询的数据
     * @return  true：是；false：否
     */
    public boolean contains(AnyType x){
        return contains(x, root);
    }

    /**
     * 查找值最大节点
     * @return  查找到的节点数据
     */
    public AnyType findMax(){
        // 根节点为空，不能查询
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMax(root).element;
    }

    /**
     * 查找值最小节点
     * @return  查找到的节点数据
     */
    public AnyType findMin(){
        // 根节点为空，不能查询
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMin(root).element;
    }

    /**
     * 插入数据
     * @param x 需要插入的数据
     */
    public void insert(AnyType x){
        root = insert(x, root);
    }

    /**
     * 删除数据
     * @param x 需要删除的数据
     */
    public void remove(AnyType x){
        root = remove(x, root);
    }

    /**
     * 打印树
     */
    public void printTree(){

        if(isEmpty()){
            System.out.println("Empty tree");
        }else{
            printTree(root);
        }
    }

    /**
     * 是否存在包含该数据的节点
     * @param x 需要查询的数据
     * @param t 根节点
     * @return  true：是；false：否
     */
    private boolean contains(AnyType x, BinaryNode<AnyType> t){

        if(t == null){
            return false;
        }

        // 比较大小
        int compareResult = x.compareTo(t.element);

        // 根据二叉查找树特性进行递归搜索
        if(compareResult < 0){
            return contains(x, t.left);
        }else if(compareResult > 0){
            return contains(x, t.right);
        }else {
            return true;
        }
    }

    /**
     * 查找值最大节点
     * 循环写法
     * 二叉查找树中，一直往右节点搜索就可以找到最大的元素
     * @param t 根节点
     * @return  查找到的节点
     */
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t){

        if(t != null){
            while (t.right != null){
                t = t.right;
            }
        }

        return t;
    }

    /**
     * 查找值最小节点
     * 递归写法
     * 二叉查找树中，一直往左节点搜索就可以找到最小的元素
     * @param t 根节点
     * @return  查找到的节点
     */
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t){

        if(t == null){
            return null;
        }else if(t.left == null){
            return t;
        }

        return findMin(t.left);
    }

    /**
     * 插入数据
     * @param x 需要插入的数据
     * @param t 根节点
     * @return  更新后的根节点
     */
    public BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t){

        if(t == null){
            return new BinaryNode<>(x, null, null);
        }

        // 比较大小
        int compareResult = x.compareTo(t.element);

        // 根据二叉查找树特性进行递归搜索
        if(compareResult < 0){
            t.left = insert(x, t.left);
        }else if(compareResult > 0){
            t.right = insert(x, t.right);
        }else {
            // 已存在该数据的节点
        }

        return t;
    }

    /**
     * 删除数据
     * @param x 需要删除的数据
     * @param t 根节点
     * @return  更新后的根节点
     */
    public BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t){

        // 未找到要删除的数据
        if(t == null){
            return t;
        }

        // 比较大小
        int compareResult = x.compareTo(t.element);

        // 根据二叉查找树特性进行递归搜索
        if(compareResult < 0){
            t.left = remove(x, t.left);
        }else if(compareResult > 0){
            t.right = remove(x, t.right);
        }else if(t.left != null && t.right != null){
            // 当前节点存在两个子树()
            t.element = findMin(t.right).element; // 取右子树最小节点替换当前节点数据
            t.right = remove(t.element, t.right); // 删除右子树中替换当前节点数据的节点
        }else {
            // 当前节点存在一个子树或没有子树
            t = t.left != null ? t.left : t.right; // 左节点有值则覆盖到当前节点，否则用右节点覆盖当前节点（无论右节点是否有值）
        }

        return t;
    }

    /**
     * 打印树
     * @param t 根节点
     */
    private void printTree(BinaryNode<AnyType> t){

        if(t != null){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

}
