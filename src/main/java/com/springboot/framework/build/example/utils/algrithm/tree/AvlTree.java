package com.springboot.framework.build.example.utils.algrithm.tree;

import java.nio.BufferUnderflowException;

/**
 * 平衡二叉树（二叉查询树的实现基础上添加平衡树的操作）
 * 保证树须深度为 O(log N)
 *
 * 1. 是二叉查询树
 * 2. 左子树和右子树的深度之差(平衡因子)的绝对值不超过1，且它的左子树和右子树都是一颗平衡二叉树。
 *
 * 删除、插入操作使用旋转的方式平衡二叉树
 * 导致二叉树不平衡的情况及采用选装方式：
 * 1. 对节点的 左儿子 的 左子树 插入或删除：单旋转（右旋转）
 * 2. 对节点的 左儿子 的 右子树 插入或删除：双旋转（对左子树左旋转，再对二叉树右旋转）
 * 3. 对节点的 右儿子 的 左子树 插入或删除：双旋转（对右子树左旋转，再对二叉树左旋转）
 * 4. 对节点的 右儿子 的 右子树 插入或删除：单旋转（左旋转）
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>> {

    /**
     * 平衡树节点
     * @param <AnyType>
     */
    private static class AvlNode<AnyType>{

        /** 节点数据 */
        AnyType element;
        /** 节点左子树 */
        AvlNode<AnyType> left;
        /** 节点右子树 */
        AvlNode<AnyType> right;
        /** 节点高度 */
        int height;

        public AvlNode(AnyType element) {
            this(element, null, null);
        }

        public AvlNode(AnyType element, AvlNode<AnyType> left, AvlNode<AnyType> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.height = 0;
        }

        @Override
        public String toString() {
            return "AvlNode{" +
                    "element=" + element +
                    ", height=" + height +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AvlTree{" +
                "root=" + root +
                '}';
    }

    /**
     * 获取节点高度
     * @param node
     * @return
     */
    private int height(AvlNode<AnyType> node){
        return node == null ? -1 : node.height;
    }

    /** 根节点 */
    private AvlNode<AnyType> root;

    public AvlTree() {
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
    private boolean contains(AnyType x, AvlNode<AnyType> t){

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
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t){

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
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t){

        if(t == null){
            return null;
        }else if(t.left == null){
            return t;
        }

        return findMin(t.left);
    }



    /**
     * 插入数据
     * @param x 要插入的数据
     * @param t 子树的根节点
     * @return  返回子树的新根
     */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t){

        if(t == null){
            return new AvlNode<AnyType>(x, null, null);
        }

        // 比较大小
        int compareResult = x.compareTo(t.element);

        // 根据二叉查找树特性进行递归搜索
        if(compareResult < 0){
            t.left = insert(x, t.left);
        }else if(compareResult > 0){
            t.right = insert(x, t.right);
        }else{
            // 已存在该数据的节点
        }

        return balance(t);
    }

    /**
     * 删除数据
     * @param x 需要删除的数据
     * @param t 根节点
     * @return  更新后的根节点
     */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t){

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

        return balance(t);
    }

    /**
     * 打印树
     * @param t 根节点
     */
    private void printTree(AvlNode<AnyType> t){
        if(t != null){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    // 平衡因子
    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * 平衡二叉树
     * @param t
     * @return
     */
    private AvlNode<AnyType> balance(AvlNode<AnyType> t){

        if(t == null){
            return t;
        }

        if(height(t.left) - height((t.right)) > ALLOWED_IMBALANCE){
            // 左子树插入数据
            if(height(t.left.left) - height(t.left.right) >= ALLOWED_IMBALANCE){
                // 对节点的 左儿子 的 左子树 插入或删除：单旋转（右旋转）
                t = rotateWithLeftChild(t);
            }else {
                // 对节点的 左儿子 的 右子树 插入或删除：双旋转（对左子树左旋转，再对二叉树右旋转）
                t = doubleWithLeftChild(t);
            }
        }else if(height(t.right) - height(t.left) > ALLOWED_IMBALANCE){
            // 右子树插入数据
            if(height(t.right.left) - height(t.right.right) > ALLOWED_IMBALANCE){
                // 对节点的 右儿子 的 左子树 插入或删除：双旋转（对右子树左旋转，再对二叉树左旋转）
                t = doubleWithRightChild(t);
            }else{
                // 对节点的 右儿子 的 右子树 插入或删除：单旋转（左旋转）
                t = rotateWithRightChild(t);
            }
        }

        // 节点高度值更新
        t.height = Math.max(height(t.left), height(t.right)) + 1;

        return t;
    }

    /**
     * 单旋转-右旋转(node ->L ->L)
     * 对节点的 左儿子 的 左子树 插入或删除
     * 1. 将当前节点t的左孩子提升为新节点nt
     * 2. 将当前节点t降为新节点nt的右孩子（新节点nt的右孩子调整为当前节点t的左孩子）
     * @param t 节点
     * @return  更新后的根
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> t){

        AvlNode<AnyType> nt = t.left;
        t.left = nt.right;
        nt.right = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        nt.height = Math.max(height(nt.left), t.height) + 1;

        return nt;
    }

    /**
     * 单旋转-左旋转(node ->R ->R)
     * 对节点的 右儿子 的 右子树 插入或删除
     * 1. 将当前节点t的右孩子提升为新节点nt
     * 2. 将当前节点t降为新节点nt的左孩子（新节点nt的左孩子调整为当前节点t的右孩子）
     * @param t 节点
     * @return  更新后的根
     */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> t){

        AvlNode<AnyType> nt = t.right;
        t.right = nt.left;
        nt.left = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        nt.height = Math.max(height(nt.left), t.height) + 1;

        return nt;
    }

    /**
     * 双旋转-左旋转->右旋转
     * 对节点的 左儿子 的 右子树 插入或删除
     * 1. 对当前节点的左孩子进行左旋转，转换成单旋转-右旋转格式(node ->R ->R)
     * 2. 再进行单旋转-右旋转操作
     * @param t 节点
     * @return 更新后的根
     */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> t){

        t.left = rotateWithRightChild(t.left);
        return rotateWithLeftChild(t);
    }

    /**
     * 双旋转-右旋转->左旋转
     * 对节点的 右儿子 的 左子树 插入或删除
     * 1. 对当前节点的右孩子进行左旋转，转换成单旋转-左旋转格式(node ->L ->L)
     * 2. 再进行单旋转-左旋转操作
     * @param t 节点
     * @return 更新后的根
     */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> t){

        t.right = rotateWithLeftChild(t.right);
        return rotateWithRightChild(t);
    }
}
