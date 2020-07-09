package com.springboot.framework.build.example.utils.algrithm.tree;

/**
 * B树:平衡多路查找树（查找路径不只两个）
 * 1. 排序方式：所有节点关键字是按递增次序排列，并遵循左小右大原则；
 * 2. 子节点数：非叶节点的子节点数>1，且<=M ，且M>=2，空树除外（注：M阶代表一个树节点最多有多少个查找路径，M=M路,当M=2则是2叉树,M=3则是3叉）；
 * 3. 关键字数：枝节点的关键字数量大于等于ceil(m/2)-1个且小于等于M-1个（注：ceil()是个朝正无穷方向取整的函数 如ceil(1.1)结果为2);
 * 4. 所有叶子节点均在同一层、叶子节点除了包含了关键字和关键字记录的指针外也有指向其子节点的指针只不过其指针地址都为null对应下图最后一层节点的空格子;
 *
 * 即：
 * 树中每个结点最多含有m个孩子（m>=2）；
 * 除根结点和叶子结点外，其它每个结点至少有[ceil(m / 2)]个孩子（其中ceil(x)是一个取上限的函数）；
 * 若根结点不是叶子结点，则至少有2个孩子（特殊情况：没有孩子的根结点，即根结点为叶子结点，整棵树只有一个根节点）；
 * 所有叶子结点都出现在同一层(最底层)，叶子结点为外部结点，保存内容，即key和value。
 * 其他结点为内部结点，保存索引，即key和next。
 */
public class BTree<AnyType extends Comparable<? super AnyType>> {

}
