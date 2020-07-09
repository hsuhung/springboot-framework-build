package com.springboot.framework.build.example.utils.algrithm.hashTable;

import java.util.LinkedList;
import java.util.List;

/**************************************************************
 * 创建日期：2020/1/17 11:34
 * 作    者：lixuhong
 * 功能描述：分离链接散列表简单实现
 * 插入数据时，计算hash值作为关键字存入数组，需要存储的数据保存到链表种，通过关键字来读取数据
 * 若数组满了，则需要重新扩充
 **************************************************************/
public class SpearateChainingHashTable<AnyType> {

    public class Employee{

        private String name;
        private double salary;
        private int seniority;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public int getSeniority() {
            return seniority;
        }

        public void setSeniority(int seniority) {
            this.seniority = seniority;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

//        @Override
//        public boolean equals(Object rhs) {
//            return rhs instanceof Employee && name.equals(((Employee) rhs).name);
//        }
    }

    public SpearateChainingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * 构造hash表
     * @param size 大小
     */
    public SpearateChainingHashTable(int size){
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<>();
        }
    }

    /**
     * 插入数据
     * @param x 需要插入的数据
     */
    public void insert(AnyType x) {
        List<AnyType> witchList = theLists[hashCode(x)];
        if (!witchList.contains(x)) {
            witchList.add(x);

            // 添加数据后，超出原来列表大小则重新分配
            if (++currentSize > theLists.length) {
                rehash();
            }
        }
    }

    /**
     * 删除数据
     * @param x 需要删除的数据
     */
    public void remove(AnyType x) {
        List<AnyType> witchList = theLists[hashCode(x)];
        if (witchList.contains(x)) {
            witchList.remove(x);
            currentSize--;
        }
    }

    /**
     * 是否包含数据
     * @param x 需要查询的数据
     * @return
     */
    public boolean contains(AnyType x) {
        List<AnyType> witchList = theLists[hashCode(x)];
        return witchList.contains(x);
    }

    /**
     * 使哈希表在逻辑上为空
     */
    public void makeEmpy() {
        for (int i = 0; i < theLists.length; i++) {
            theLists[i].clear();
        }
        currentSize = 0;
    }

    /** 默认hash表大小 */
    private static final int DEFAULT_TABLE_SIZE = 0;
    /** 列表 */
    private List<AnyType>[] theLists;
    /** 当前大小 */
    private int currentSize;

    /**
     * 扩充列表（重新哈希以获取单独的链接哈希表）
     */
    private void rehash() {
        List<AnyType>[] oldList = theLists;

        // 创建两倍大小的表
        theLists = new List[nextPrime(2 * theLists.length)];
        for (int j = 0; j < theLists.length; j++) {
            theLists[j] = new LinkedList<>();
        }

        // 复制表
        for (int i = 0; i < oldList.length; i++) {
            for (AnyType item : oldList[i]) {
                insert(item);
            }
        }
    }

    /**
     * hash值计算(简单计算， 要求表大小为素数)
     * @param x
     * @return
     */
    private int hashCode(AnyType x) {

        int hashVal = x.hashCode();

        hashVal %= theLists.length;
        if (hashVal < 0) {
            hashVal += theLists.length;
        }

        return hashVal;
    }

    /**
     * 查找至少等于n的质数的内部方法
     * @param n 起始编号（必须为正）
     * @return 返回大于或等于n的质数
     */
    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        for (; !isPrime(n); n += 2) {
        }

        return n;
    }

    /**
     * 数字是否为质数(不是有效的算法)
     * @param n 测试的数字
     * @return
     */
    private static boolean isPrime(int n) {

        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return true;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}
