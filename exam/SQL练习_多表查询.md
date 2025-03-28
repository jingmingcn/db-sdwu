---
marp: true
size: 16:9
theme: am_green
paginate: true
headingDivider: [2,3,4]
footer: \ *井明（数据科学与计算机学院）* *数据库系统概论* *2025年3月28日*
---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: ![](../images/logo_transparent.png) -->
<!-- _header: ![](../images/logo-1_transparent.png) -->


# 第2章 关系数据库标准语言SQL

###### “SQL练习之多表查询”

井明
数据科学与计算机学院
jingming@sdu.edu.cn
2025年3月28日

## 介绍

涵盖 内连接 (JOIN)、外连接 (LEFT JOIN)、聚合查询 (GROUP BY + HAVING) 等 SQL 多表查询的常见用法。

### **学生表（students）**
| student_id | name  | age | gender | major      |
|-----------|------|----|--------|----------|
| 1         | 张三  | 20 | 男     | 计算机科学 |
| 2         | 李四  | 21 | 女     | 数学      |
| 3         | 王五  | 22 | 男     | 物理      |
| 4         | 赵六  | 20 | 女     | 计算机科学 |
| 5         | 孙七  | 23 | 男     | 计算机科学 |

### **课程表（courses）**
| course_id | course_name  | teacher  |
|-----------|-------------|----------|
| 101       | 数据库系统  | 王老师   |
| 102       | 计算机网络  | 李老师   |
| 103       | 高等数学    | 张老师   |
| 104       | 机器学习    | 陈老师   |

### **选课表（enrollments）**
| student_id | course_id |
|-----------|-----------|
| 1         | 101       |
| 1         | 102       |
| 2         | 103       |
| 3         | 101       |
| 4         | 104       |
| 5         | 102       |

## 题目 1：查询所有学生及其选修的课程
**问题：**  
查询所有学生的 `name` 及其 `course_name`，包括未选课的学生。

<!-- **答案：**
```sql
SELECT s.name, c.course_name 
FROM students s
LEFT JOIN enrollments e ON s.student_id = e.student_id
LEFT JOIN courses c ON e.course_id = c.course_id;
```

讲解：
	•	使用 LEFT JOIN 确保所有学生都被列出，即使没有选课。
	•	students 表与 enrollments 通过 student_id 关联，再与 courses 通过 course_id 关联。 -->

## 题目 2：查询选修“数据库系统”课程的所有学生

问题：
列出所有选修 "数据库系统" 课程的学生姓名。

<!-- 答案：
```sql
SELECT s.name 
FROM students s
JOIN enrollments e ON s.student_id = e.student_id
JOIN courses c ON e.course_id = c.course_id
WHERE c.course_name = '数据库系统';
```

讲解：
	•	JOIN 连接 students、enrollments 和 courses 表。
	•	WHERE 过滤选修 "数据库系统" 课程的学生。 -->

## 题目 3：查询所有课程及选修该课程的学生人数

问题：
返回 course_name 及其选课学生数量。

<!-- 答案：
```sql
SELECT c.course_name, COUNT(e.student_id) AS student_count
FROM courses c
LEFT JOIN enrollments e ON c.course_id = e.course_id
GROUP BY c.course_name;
```

讲解：
	•	LEFT JOIN 确保所有课程都被列出，即使没有人选修。
	•	COUNT(e.student_id) 统计每门课程的学生数。
	•	GROUP BY 按 course_name 分组。 -->

## 题目 4：查询没有选课的学生

问题：
列出未选修任何课程的学生姓名。

<!-- 答案：
```sql
SELECT s.name 
FROM students s
LEFT JOIN enrollments e ON s.student_id = e.student_id
WHERE e.course_id IS NULL;
```

讲解：
	•	LEFT JOIN 确保所有学生都被列出。
	•	WHERE e.course_id IS NULL 过滤出 enrollments 表中没有匹配项的学生。 -->

## 题目 5：查询每个专业的学生数量

问题：
计算 major（专业）对应的学生人数。

<!-- 答案：
```sql
SELECT major, COUNT(*) AS student_count
FROM students
GROUP BY major;
```

讲解：
	•	COUNT(*) 统计每个专业的学生人数。
	•	GROUP BY major 按 major 进行分组。 -->

## 题目 6：查询每个教师教授的课程及选课人数

问题：
返回 teacher、course_name 及 选课人数。

<!-- 答案：
```sql
SELECT c.teacher, c.course_name, COUNT(e.student_id) AS student_count
FROM courses c
LEFT JOIN enrollments e ON c.course_id = e.course_id
GROUP BY c.teacher, c.course_name;
```

讲解：
	•	统计每门课程的选课人数，并按教师分组。
	•	LEFT JOIN 确保所有教师的课程都被列出，即使无人选修。 -->

## 题目 7：查询学生的选课数量

问题：
列出所有学生的 name 及其选课数量。

<!-- 答案：
```sql
SELECT s.name, COUNT(e.course_id) AS course_count
FROM students s
LEFT JOIN enrollments e ON s.student_id = e.student_id
GROUP BY s.name;
```

讲解：
	•	COUNT(e.course_id) 统计每个学生选修的课程数量。
	•	LEFT JOIN 确保所有学生都列出，即使未选课。 -->

## 题目 8：查询至少选修 2 门课程的学生

问题：
返回选课数 >= 2 的学生姓名。

<!-- 答案：
```sql
SELECT s.name, COUNT(e.course_id) AS course_count
FROM students s
JOIN enrollments e ON s.student_id = e.student_id
GROUP BY s.name
HAVING COUNT(e.course_id) >= 2;
```

讲解：
	•	HAVING COUNT(e.course_id) >= 2 过滤出选课数不少于 2 门的学生。 -->

## 题目 9：查询学生、课程和教师信息

问题：
列出所有选课学生的 name、选修的 course_name 及 teacher。

<!-- 答案：
```sql
SELECT s.name, c.course_name, c.teacher
FROM students s
JOIN enrollments e ON s.student_id = e.student_id
JOIN courses c ON e.course_id = c.course_id;
```

讲解：
	•	JOIN 连接 students、enrollments 和 courses。
	•	返回每位学生的课程及教师。 -->

## 题目 10：查询选修“数据库系统”且专业为“计算机科学”的学生

问题：
查询 major 为 "计算机科学" 且选修 "数据库系统" 课程的学生姓名。

<!-- 答案：
```sql
SELECT s.name 
FROM students s
JOIN enrollments e ON s.student_id = e.student_id
JOIN courses c ON e.course_id = c.course_id
WHERE s.major = '计算机科学' AND c.course_name = '数据库系统';
```

讲解：
	•	WHERE 限制 major 为 "计算机科学" 且 course_name 为 "数据库系统" 的学生。 -->


