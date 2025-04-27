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


# 📋 作业二：关系代数练习题

井明
数据科学与计算机学院
jingming@sdu.edu.cn
2025年春

## 🧠 练习题主题一：高校教务管理数据库

✨ 数据库模式如下：

Instructor(IID, IName, Dept)
Course(CID, CName, Dept)
Teaches(IID, CID, Semester, Year)

### 题目1：查询所有教师的姓名（Instructor 表）

✅ 参考答案：

$\Pi_{\text{IName}}(\text{Instructor})$

### 题目2：查询所有开设课程所属的系别（Dept）

✅ 参考答案：

$\Pi_{\text{Dept}}(\text{Course})$


### 题目3：查询2024年开设的所有课程编号

✅ 参考答案：

$\Pi_{\text{CID}}(\sigma_{\text{Year} = 2024}(\text{Teaches}))$


### 题目4：查询2023年第一学期开设的课程编号和授课教师编号

✅ 参考答案：

$\Pi_{\text{CID}, \text{IID}}(\sigma_{\text{Year} = 2023 \wedge \text{Semester} = 'Spring'}(\text{Teaches}))$

### 题目5：查询所有在“CS”系授课的教师编号

✅ 参考答案：

$\Pi_{\text{IID}}(\sigma_{\text{Dept} = 'CS'}(\text{Instructor}))$

### 题目6：找出所有开设过课程的教师编号

✅ 参考答案：

$\Pi_{\text{IID}}(\text{Teaches})$


### 题目7：找出至少讲授过一门“CS”系课程的教师姓名

✅ 参考答案：

$\Pi_{\text{IName}}((\text{Instructor} \bowtie \text{Teaches}) \bowtie \sigma_{\text{Dept} = 'CS'}(\text{Course}))$


### 题目8：查询所有教师所授课程的名称和年份

✅ 参考答案：

$\Pi_{\text{IName}, \text{CName}, \text{Year}}((\text{Instructor} \bowtie \text{Teaches}) \bowtie \text{Course})$



### 题目9：找出没有讲授过任何课程的教师姓名

✅ 参考答案：

$\Pi_{\text{IName}}(\text{Instructor}) - \Pi_{\text{IName}}(\text{Instructor} \bowtie \text{Teaches})$

### 题目10：查询教师“张三”（IName = ‘张三’）教授过的所有课程名称

✅ 参考答案：

$\Pi_{\text{CName}}(\sigma_{\text{IName} = '张三'}(\text{Instructor}) \bowtie \text{Teaches} \bowtie \text{Course})$


## 🏥 练习题主题二：医院病人管理系统

💾 数据库模式如下：

Patient(PID, PName, Gender, Age)
Doctor(DID, DName, Department)
Visit(PID, DID, VisitDate, Diagnosis)



### 题目1：查询所有病人的姓名

✅ 参考答案：

$\Pi_{\text{PName}}(\text{Patient})$



### 题目2：查询所有就诊记录中的日期

✅ 参考答案：

$\Pi_{\text{VisitDate}}(\text{Visit})$



### 题目3：查询2024年后就诊的病人编号和诊断结果

✅ 参考答案：

$\Pi_{\text{PID}, \text{Diagnosis}}(\sigma_{\text{VisitDate} > '2024-01-01'}(\text{Visit}))$



### 题目4：查询“内科”医生编号

✅ 参考答案：

$\Pi_{\text{DID}}(\sigma_{\text{Department} = '内科'}(\text{Doctor}))$



### 题目5：查询每位医生的姓名和其接诊过的病人编号

✅ 参考答案：

$\Pi_{\text{DName}, \text{PID}}(\text{Doctor} \bowtie \text{Visit})$



### 题目6：找出所有没有就诊记录的病人姓名

✅ 参考答案：

$\Pi_{\text{PName}}(\text{Patient}) - \Pi_{\text{PName}}(\text{Patient} \bowtie \text{Visit})$



### 题目7：找出接诊过“张伟”医生的所有病人姓名

✅ 参考答案：

$\Pi_{\text{PName}}(\sigma_{\text{DName} = '张伟'}(\text{Doctor}) \bowtie \text{Visit} \bowtie \text{Patient})$



### 题目8：找出诊断中包含“感冒”的所有就诊记录

✅ 参考答案：

$\sigma_{\text{Diagnosis} \text{ LIKE } '\%感冒\%'}(\text{Visit})$



### 题目9：找出曾与所有“内科”医生就诊过的病人编号

✅ 参考答案：

$\Pi_{\text{PID}}(\text{Visit}) \div \Pi_{\text{DID}}(\sigma_{\text{Department} = '内科'}(\text{Doctor}))$



### 题目10：按科室统计每个科室医生接诊的总次数

✅ 参考答案：

$\gamma_{\text{Department}; \ \text{count}(PID) \rightarrow \text{VisitCount}}((\text{Doctor} \bowtie \text{Visit}))$



