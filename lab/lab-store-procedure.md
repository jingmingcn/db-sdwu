---
marp: true
size: 16:9
theme: am_purple
paginate: true
headingDivider: [2,3,4]
footer: \ *井明（数据科学与计算机学院）* *数据库系统概论* *2025年春*
---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: ![](../images/logo_transparent.png) -->
<!-- _header: ![](../images/logo-1_transparent.png) -->

# 🧪 实验四：使用 MySQL 存储过程实现工资调整功能


井明
数据科学与计算机学院
jingming@sdu.edu.cn
2025年春

## 🎯 一、实验目的
1.	掌握存储过程的定义与使用方法；
2.	理解存储过程中的参数传递（IN / OUT）；
3.	熟悉控制结构（IF、SET、SELECT INTO等）；
4.	掌握如何将业务逻辑封装为存储过程以便复用。

## 📋 二、实验内容
1.	创建员工表 employees，包含员工姓名、工资和部门；
2.	编写一个名为 adjust_salary 的存储过程，功能如下：
•	传入员工姓名（IN 参数）和涨薪幅度（IN 参数）；
•	如果该员工存在，则更新其工资；
•	如果不存在，则输出错误提示；
•	并通过 OUT 参数返回更新后的工资（或 NULL）；
3.	调用该过程并验证结果。



## 🧱 三、表结构定义

```sql
CREATE TABLE employees (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  salary DECIMAL(10, 2),
  department VARCHAR(50)
);
```

## 🧪 四、插入测试数据

```sql
INSERT INTO employees (name, salary, department)
VALUES
  ('Alice', 8000.00, 'IT'),
  ('Bob', 6500.00, 'HR'),
  ('Charlie', 7000.00, 'Sales');

```

## 🧾 五、实验任务

创建一个名为 adjust_salary 的存储过程，包含：

•	IN emp_name VARCHAR(50)：传入员工姓名；
•	IN delta DECIMAL(10,2)：传入加薪幅度；
•	OUT new_salary DECIMAL(10,2)：输出新工资；
•	内部流程：如果员工存在，则调整其工资，否则将 new_salary 设置为 NULL；

## ✅ 六、参考答案（存储过程定义）

```sql
DELIMITER //

CREATE PROCEDURE adjust_salary(
  IN emp_name VARCHAR(50),
  IN delta DECIMAL(10,2),
  OUT new_salary DECIMAL(10,2)
)
BEGIN
  DECLARE emp_exists INT DEFAULT 0;

  -- 检查员工是否存在
  SELECT COUNT(*) INTO emp_exists
  FROM employees
  WHERE name = emp_name;

  IF emp_exists > 0 THEN
    -- 更新工资
    UPDATE employees
    SET salary = salary + delta
    WHERE name = emp_name;

    -- 返回更新后工资
    SELECT salary INTO new_salary
    FROM employees
    WHERE name = emp_name;
  ELSE
    -- 员工不存在
    SET new_salary = NULL;
  END IF;
END;
//

DELIMITER ;

```

## 🧪 七、调用存储过程并查看结果

```sql
-- 定义一个用户变量接收结果
CALL adjust_salary('Alice', 500.00, @result);
SELECT @result AS new_salary; -- 期望结果：8500.00

-- 调用不存在员工
CALL adjust_salary('David', 300.00, @res2);
SELECT @res2 AS result_for_david; -- 期望结果：NULL

```


## 🧱 八、扩展任务（选做）

•	增加参数：限制加薪幅度不得超过2000；
•	为所有“IT”部门员工加薪，使用游标；
•	加入日志记录表，记录每次加薪操作；
•	将错误信息返回字符串形式，如“用户不存在”。


## 📎 九、实验小结建议

•	存储过程如何实现逻辑复用？
•	参数使用 IN 和 OUT 有何不同？
•	如果你要实现“年终统一加薪”，可以用什么方式？
