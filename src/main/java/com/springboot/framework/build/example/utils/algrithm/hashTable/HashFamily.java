package com.springboot.framework.build.example.utils.algrithm.hashTable;

/**
 * 布谷鸟散列接口
 * @param <AnyType>
 */
public interface HashFamily<AnyType> {

        int hash(AnyType x, int which);
        int getNumberOfFunctions();
        void generateNewFunctions();
}
