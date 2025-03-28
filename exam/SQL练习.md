---
marp: true
size: 16:9
theme: am_blue
paginate: true
headingDivider: [2,3,4]
footer: \ *井明（数据科学与计算机学院）* *数据库系统概论* *2025年3月28日*
---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: ![](../images/logo_transparent.png) -->
<!-- _header: ![](../images/logo-1_transparent.png) -->


# 第2章 关系数据库标准语言SQL

###### “SQL练习”

井明
数据科学与计算机学院
jingming@sdu.edu.cn
2025年3月28日



## 表结构

在下面的题目中，假设有一张 students 表，结构如下：


| student_id | name | age | gender | major         | gpa | enrollment_date |
|------------|------|-----|--------|---------------|-----|-----------------|
| 1          | 张三 | 20  | 男     | 计算机科学    | 3.5 | 2022-09-01      |
| 2          | 李四 | 21  | 女     | 数学          | 3.8 | 2021-09-01      |
| 3          | 王五 | 22  | 男     | 物理          | 2.9 | 2020-09-01      |
| 4          | 赵六 | 20  | 女     | 计算机科学    | 3.7 | 2022-09-01      |
| 5          | 孙七 | 23  | 男     | 计算机科学    | 3.2 | 2019-09-01      |


## 题目 1：查询所有学生的信息

问题：
使用 SELECT 语句查询 students 表中的所有列。

<!-- 答案：

SELECT * FROM students;

讲解：
SELECT * 代表查询表中的所有列，FROM students 指定了查询的表。 -->

## 题目 2：查询所有学生的姓名和专业

问题：
仅查询学生的 name 和 major 列。

<!-- 答案：

SELECT name, major FROM students;

讲解：
只选择需要的列而不是 SELECT *，可以提高查询效率。 -->

## 题目 3：查询计算机科学专业的学生

问题：
查询 major 为 "计算机科学" 的所有学生。

<!-- 答案：

SELECT * FROM students WHERE major = '计算机科学';

讲解：
WHERE 用于过滤数据，这里筛选 major 等于 "计算机科学" 的记录。 -->

## 题目 4：查询 GPA 大于 3.0 且年龄小于 22 岁的学生

问题：
查找 gpa > 3.0 且 age < 22 的学生。

<!-- 答案：

SELECT * FROM students WHERE gpa > 3.0 AND age < 22;

讲解：
	•	AND 逻辑运算符表示同时满足多个条件。
	•	gpa > 3.0 过滤 GPA 大于 3.0 的学生。
	•	age < 22 过滤年龄小于 22 岁的学生。 -->

## 题目 5：查询所有女生的姓名和 GPA，并按 GPA 降序排序

问题：
查询 gender 为 "女" 的学生，并按 gpa 由高到低排序。

<!-- 答案：

SELECT name, gpa FROM students WHERE gender = '女' ORDER BY gpa DESC;

讲解：
	•	WHERE gender = '女' 过滤出女生。
	•	ORDER BY gpa DESC 使 GPA 从高到低排列（DESC 表示降序）。 -->

## 题目 6：查询 GPA 最高的学生

问题：
查找 GPA 最高的学生（可能有多个）。

<!-- 答案：

SELECT * FROM students WHERE gpa = (SELECT MAX(gpa) FROM students);

讲解：
	•	SELECT MAX(gpa) FROM students 获取最高 GPA 值。
	•	WHERE gpa = (...) 过滤出所有 GPA 等于最高值的学生。 -->

## 题目 7：查询不同专业的学生人数

问题：
计算每个 major 下的学生数量，并按数量降序排序。

<!-- 答案：

SELECT major, COUNT(*) AS student_count FROM students GROUP BY major ORDER BY student_count DESC;

讲解：
	•	COUNT(*) 计算每个专业的学生人数。
	•	GROUP BY major 按 major 分组，统计每个专业的数量。
	•	ORDER BY student_count DESC 使数量从高到低排列。 -->

## 题目 8：查询 2020 年之后入学的学生

问题：
查询 enrollment_date 在 "2020-01-01" 之后的学生。

<!-- 答案：

SELECT * FROM students WHERE enrollment_date > '2020-01-01';

讲解：
	•	enrollment_date > '2020-01-01' 选取 2020 年 1 月 1 日之后入学的学生。
	•	日期格式一般采用 YYYY-MM-DD 进行比较。 -->

## 题目 9：查询所有学生的 GPA，并给 GPA 进行评级

问题：
返回所有学生的 name 和 gpa，并根据 gpa 评级（大于 3.5 为 "优秀"，2.5-3.5 为 "良好"，否则为 "及格"）。

> 提示：
> 扩展内容，自学相关语法。这个查询需求应用广泛。
>

<!-- 答案：

SELECT name, gpa,
       CASE 
           WHEN gpa > 3.5 THEN '优秀'
           WHEN gpa BETWEEN 2.5 AND 3.5 THEN '良好'
           ELSE '及格'
       END AS rating
FROM students;

讲解：
	•	CASE 语句用于创建自定义分类。
	•	WHEN gpa > 3.5 THEN '优秀' 指定 GPA 超过 3.5 时评级为 "优秀"。
	•	BETWEEN 2.5 AND 3.5 代表 GPA 在 2.5-3.5 之间，评级为 "良好"。
	•	其他情况默认 "及格"。 -->

## 题目 10：查询每个专业的平均 GPA，且平均 GPA 大于 3.0

问题：
统计每个 major 的平均 GPA，并筛选出 GPA 大于 3.0 的专业。

<!-- 答案：

SELECT major, AVG(gpa) AS avg_gpa FROM students GROUP BY major HAVING avg_gpa > 3.0;

讲解：
	•	AVG(gpa) 计算每个专业的平均 GPA。
	•	GROUP BY major 按 major 分组统计平均值。
	•	HAVING avg_gpa > 3.0 过滤出平均 GPA 超过 3.0 的专业（HAVING 用于聚合查询的筛选）。 -->
