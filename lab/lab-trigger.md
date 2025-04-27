---
marp: true
size: 16:9
theme: am_green
paginate: true
headingDivider: [2,3,4]
footer: \ *井明（数据科学与计算机学院）* *数据库系统概论* *2025年春*
---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: ![](../images/logo_transparent.png) -->
<!-- _header: ![](../images/logo-1_transparent.png) -->

# 🧪 实验三：使用 MySQL 触发器实现数据自动审计


井明
数据科学与计算机学院
jingming@sdu.edu.cn
2025年春


## 🎯 一、实验目的

1.	掌握触发器的基本语法及创建方法。
2.	能够在数据变更操作（如INSERT、UPDATE、DELETE）中触发逻辑。
3.	实践使用 NEW 和 OLD 伪记录变量。
4.	实现对关键表的修改行为记录，达到审计追踪目的。


## 📋 二、实验内容

1.	创建一个订单表 orders 和一个日志表 order_logs。
2.	编写一个触发器：当 orders 表中记录发生更新时，自动将旧数据、新数据、操作时间、操作人等信息写入 order_logs。
3.	插入初始订单数据。
4.	修改一条订单记录，验证触发器是否自动记录日志。


## 🧱 三、表结构定义

1. orders 表（订单信息）

```sql
CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(100),
  quantity INT,
  price DECIMAL(10, 2)
);
```

----

2. order_logs 表（日志记录）

```sql
CREATE TABLE order_logs (
  log_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  old_quantity INT,
  new_quantity INT,
  old_price DECIMAL(10, 2),
  new_price DECIMAL(10, 2),
  updated_at DATETIME,
  updated_by VARCHAR(50)
);
```


## 🧪 四、实验任务

1. 向 orders 表中插入测试数据：

```sql
INSERT INTO orders (product_name, quantity, price)
VALUES ('Laptop', 10, 5999.99);
```

2. 创建一个 BEFORE UPDATE 类型的触发器，实现如下逻辑：

-	每当 orders 表的记录被更新
-	将更新前后的数量、价格、时间等信息写入 order_logs
-	模拟记录操作用户 @current_user



## 💡 五、参考触发器代码

```sql
-- 设置模拟操作人
SET @current_user = 'admin_user';

DELIMITER //

CREATE TRIGGER trg_order_update
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
  INSERT INTO order_logs (
    order_id,
    old_quantity,
    new_quantity,
    old_price,
    new_price,
    updated_at,
    updated_by
  )
  VALUES (
    OLD.id,
    OLD.quantity,
    NEW.quantity,
    OLD.price,
    NEW.price,
    NOW(),
    @current_user
  );
END;
//

DELIMITER ;

```


## 🧪 六、测试触发器

```sql
-- 修改订单
UPDATE orders SET quantity = 15, price = 5799.99 WHERE id = 1;

-- 查看日志表
SELECT * FROM order_logs;

```

## 🧱 七、实验扩展（选做）

1.	修改触发器为 AFTER UPDATE 并比较效果。
2.	扩展记录客户 IP（通过 @client_ip 模拟）。
3.	为 DELETE 操作创建独立触发器，记录被删除的数据。


## 📎 八、实验总结建议

•	本次实验你遇到了哪些问题？如何解决的？
•	触发器和存储过程的区别与联系？
•	如果业务需要“回滚更新”，是否可通过触发器实现？
