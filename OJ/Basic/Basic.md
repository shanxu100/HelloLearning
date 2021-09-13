# Basic


## 1、Dijkstra算法
单源最短路径算法：用于计算一个节点到其他所有节点的最短路径
### 1.1 问题
求解单元点的最短路径问题：给定带权有向图G和源点v，求v到G中其他顶点的最短路径
限制条件：图G中不存在负权值的边
### 1.2 步骤
[《最短路径—Dijkstra算法和Floyd算法》](https://www.cnblogs.com/biyeymyhjob/archive/2012/07/31/2615833.html)


## 2、Floyd算法
Floyd-Warshall算法（Floyd-Warshall algorithm）是解决任意两点间的最短路径的一种算法，可以正确处理有向图或负权的最短路径问题，同时也被用于计算有向图的传递闭包。Floyd-Warshall算法的时间复杂度为O(N3)，空间复杂度为O(N2)



## 2、最小生成树算法
即搜索到的边子集所构成的树中，不但包括了连通图里的所有顶点(Vertex ,graph theory)，且其所有边的权值之和亦为最小

## 3、Prim算法
加点法：每次加入距离“当前树的任意节点”最近的点   
[《最小生成树-Prim算法和Kruskal算法》](https://www.cnblogs.com/biyeymyhjob/archive/2012/07/30/2615542.html)

Prim算法与Dijkstra算法的区别：
Dijkstra算法在加入一个点后，需要更新其余“未引入点的集合”中个点的距离（更新后的距离即为这些点到出发点的最小距离）

## 4、Kruskal算法

