---
marp: true
size: 16:9
theme: am_purple
paginate: true
headingDivider: [2,3,4]
footer: \ *井明（大数据研究院）* *分布式存储与并行计算* *2023 Fall*
---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: ![](images/qlu.png) -->

# Linux 文件系统权限

###### “能力越大，责任越大。”

井明
大数据研究院


## 1. 介绍
- Linux 通过权限管理文件和目录的访问控制。
- 权限决定了谁可以 **读取（read）**、**写入（write）** 或 **执行（execute）** 文件。


## 2. 查看文件权限
- 使用 `ls -l` 查看文件权限：

```bash
ls -l
```
示例输出：

`-rw-r--r--  1 user group  1234 Mar 19 10:00 file.txt`

•	第一个字段（-rw-r--r--）表示文件类型和权限：
•	-（文件）或 d（目录）
•	rwx（所有者）
•	r--（同组用户）
•	r--（其他用户）


## 3. 修改权限：chmod
   
•	使用 chmod 命令更改权限：

```bash
chmod 750 file.txt  # Owner: read/write/execute, Group: read/execute, Others: no access
```

**含义：**
•	所有者：rwx（7）
•	组用户：r-x（5）
•	其他用户：---（0）

## 4. 修改所有权：chown

使用 `chown` 修改文件所有者和用户组：

`chown` 用户名:组名` file.txt`

•	示例：

```bash
chown alice:developers script.sh  # Change owner to Alice and group to developers
```

## 5. 特殊权限

• SUID（设置用户 ID）：`chmod u+s file`

示例（使文件以文件所有者的权限运行）：

```bash
chmod u+s /path/to/file
```
• SGID（设置组 ID）：`chmod g+s directory`

示例（使目录中的新文件继承目录的组）：

```bash
chmod g+s /path/to/directory
```

## 粘滞位（Sticky Bit）

• 粘滞位（Sticky Bit）：`chmod +t directory`

示例（保护目录中的文件不被其他用户删除）：

```bash
chmod +t /path/to/directory
```

• 只有文件所有者或目录所有者可以删除或修改目录中的文件。

示例（保护 /tmp 目录）：

```bash
chmod 1777 /tmp  # Set sticky bit on /tmp directory
```

•	防止用户删除他们不拥有的文件。

## 6. 如何防止其他用户读取文件

<!-- _footer: "" -->

**方法 1：限制文件权限**

```bash
chmod 700 private_file.txt  # Only owner can read, write, and execute
```
•	只有所有者可以访问该文件。

**方法 2：使用 ACL（访问控制列表）**

```bash
setfacl -m u:用户名:--- file.txt  # Deny all access to the specified user
```

•	禁止特定用户访问文件。

**方法 3：加密文件,只有授权用户可以解密并读取文件。**

```bash
gpg --encrypt --recipient "user@example.com" file.txt  # Encrypt file for a specific user
```

### 7. 如何防止数据泄露

**方法 1：禁用外部设备**
•	禁用 USB 设备：

```bash
echo "blacklist usb-storage" | sudo tee /etc/modprobe.d/usb-storage.conf
sudo modprobe -r usb-storage
```

**方法 2：禁用网络访问**
•	使用防火墙阻止所有外部连接：

```bash
sudo ufw deny out to any  # Block all outgoing traffic
```

---

**方法 3：使用安全的挂载选项**
•	修改 /etc/fstab 限制磁盘操作：

```bash
/dev/sda1 /mnt/data ext4 defaults,nosuid,nodev,noexec 0 0
```

**方法 4：监控系统日志**
•	查看访问日志：

```bash
sudo journalctl -xe
```

## 总结
•	使用 chmod、chown 和 ACL 控制访问权限。
•	通过加密文件提升数据安全性。
•	禁用外部设备、限制网络访问、监控日志以防止数据泄露。

## 练习

1. 有两个用户：Alice 和 Bob。Alice 拥有一个文件 file.txt，Bob 不应该读取该文件。你会如何设置文件权限？
答案：
```bash
chmod 700 file.txt # OR
chmod o-r file.txt
```

2. 有两个文件：file1.txt 和 file2.txt。你想让 Alice 可以读取 file1.txt，但不能读取 file2.txt。你会如何设置文件权限？
答案：
```bash
setfacl -m u:alice:r file1.txt
setfacl -m u:alice:- file2.txt
```
---
3. 有三个文件夹，folder1、folder2 和 folder3。你想让 Alice 可以读取 folder1，但不能读取  folder2 和 folder3, Bod 可以读取foloder2, 但不能读取folder1和folder3, 你会如何设置文件夹权限？

---

```bash
#!/bin/bash

# Define users and folders
ALICE="alice"
BOB="bob"
FOLDER1="/path/to/folder1"
FOLDER2="/path/to/folder2"
FOLDER3="/path/to/folder3"

# Ensure the folders exist
mkdir -p "$FOLDER1" "$FOLDER2" "$FOLDER3"

# Set root as the owner of all folders
chown root:root "$FOLDER1" "$FOLDER2" "$FOLDER3"

# Remove all permissions for other users
chmod 700 "$FOLDER1" "$FOLDER2" "$FOLDER3"

# Allow Alice to read folder1
setfacl -m u:$ALICE:rX "$FOLDER1"

# Allow Bob to read folder2
setfacl -m u:$BOB:rX "$FOLDER2"

echo "Permissions have been configured successfully."
```
---

解释：

1.	mkdir -p "\$FOLDER1" "\$FOLDER2" "\$FOLDER3" ensures the folders exist.
2.	chown root:root "\$FOLDER1" "\$FOLDER2" "\$FOLDER3" sets root as the owner.
3.	chmod 700 "\$FOLDER1" "\$FOLDER2" "\$FOLDER3" ensures that only root has access by default.
4.	setfacl -m u:\$ALICE:rX "\$FOLDER1" allows Alice to read folder1 (but not the others).
5.	setfacl -m u:\$BOB:rX "$FOLDER2" allows Bob to read folder2 (but not the others).

This setup ensures that Alice can only access folder1, Bob can only access folder2, and neither can access folder3.

---
<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: "" -->


# Linux setfacl 和 getfacl 命令

###### “更灵活的文件权限管理。”

井明
大数据研究院



## `setfacl` 命令介绍

`setfacl` 是 Linux 中用于设置或修改文件和目录的访问控制列表（ACL）的命令。它允许给特定用户或组赋予额外的权限，超越传统的 `rwx`（读、写、执行）权限模型。



### 基本语法
```bash
setfacl [选项] 规则 文件/目录
```



### 常见选项
| 选项  | 说明 |
|------|------|
| `-m` | 修改 ACL 规则（添加或更新权限）。 |
| `-x` | 删除 ACL 规则。 |
| `-b` | 删除所有 ACL 规则（恢复默认权限）。 |
| `-k` | 删除默认 ACL（仅适用于目录）。 |
| `-d` | 设置默认 ACL（仅适用于目录）。 |
| `-R` | 递归应用到目录及其所有子项。 |
| `--set` | 直接设置完整的 ACL（会覆盖现有 ACL）。 |
| `--restore=file` | 从文件中恢复 ACL 规则。 |



### 规则格式
ACL 规则的基本格式如下：
```
[类型]:[用户或组]:[权限]
```
| 类型 | 说明 |
|------|------|
| `u` | 用户（user） |
| `g` | 组（group） |
| `o` | 其他人（other） |
| `m` | 掩码（mask） |
| `d` | 默认 ACL（仅目录） |

权限使用 `r`（读）、`w`（写）、`x`（执行）。

#### 1. 给特定用户赋予额外权限
```bash
setfacl -m u:user2:rw example.txt
```
解释： 赋予 `user2` 对 `example.txt` 的读写权限。


#### 2. 给特定用户删除权限
```bash
setfacl -x u:user2 example.txt
```
解释： 移除 `user2` 在 `example.txt` 上的 ACL 规则。



#### 3. 给特定组赋予权限
```bash
setfacl -m g:group1:rwx example.txt
```
解释： 赋予 `group1` 读、写、执行权限。



#### 4. 设置目录的默认 ACL
```bash
setfacl -m d:u:user2:rw /data/
```
解释： 让 `user2` 拥有 `/data/` 目录中新创建文件的读写权限。



#### 5. 递归设置 ACL
```bash
setfacl -R -m u:user2:rwx /data/
```
解释： 让 `user2` 拥有 `/data/` 及其子文件的读、写、执行权限。



#### 6. 删除所有 ACL
```bash
setfacl -b example.txt
```
解释： 移除 `example.txt` 上的所有 ACL 规则，恢复传统权限。



#### 7. 备份和恢复 ACL
备份 ACL：
```bash
getfacl example.txt > acl_backup.txt
```
恢复 ACL：
```bash
setfacl --restore=acl_backup.txt
```
解释： 先用 `getfacl` 备份 ACL 到 `acl_backup.txt`，然后使用 `setfacl --restore` 进行恢复。



### ACL 相关命令
| 命令 | 作用 |
|------|------|
| `setfacl` | 设置或修改 ACL 权限 |
| `getfacl` | 获取 ACL 权限 |



### 总结
- `setfacl` 用于管理 ACL 规则，比传统的 `chmod` 更灵活。
- ACL 允许针对特定用户或组赋予额外权限，而不会影响文件所有者或组的默认权限。
- 配合 `getfacl` 进行权限管理、备份和恢复，确保灵活的访问控制。



## `getfacl` 命令介绍

`getfacl` 是 Linux 中用于获取文件或目录的**访问控制列表（ACL）**信息的命令。ACL 允许对文件和目录进行更加精细的权限控制，超越了传统的 `rwx`（读、写、执行）权限模型。



### 基本语法
```bash
getfacl [选项] 文件/目录
```

### 常见选项
| 选项  | 说明 |
|------|------|
| `-d` | 显示默认 ACL（仅适用于目录）。 |
| `-c` | 不输出注释信息（如 `#` 开头的行）。 |
| `-e` | 以 `getfacl` 和 `setfacl` 兼容的格式输出（方便备份和恢复）。 |
| `-R` | 递归获取目录及其子项的 ACL 信息。 |
| `--help` | 显示帮助信息。 |

####  1. 查看单个文件的 ACL
```bash
getfacl example.txt
```
**示例输出：**
```bash
# file: example.txt
# owner: user1
# group: group1
user::rw-
user:user2:r--   # 额外赋予 user2 只读权限
group::r--
mask::r--
other::---
```

---
**解释：**
- `user::rw-` → 文件所有者（`user1`）具有读写权限。
- `user:user2:r--` → `user2` 具有额外的只读权限（这就是 ACL 的作用）。
- `group::r--` → 文件所属组 `group1` 具有读权限。
- `mask::r--` → 限制 ACL 中定义的用户/组的最大权限。
- `other::---` → 其他用户无权限。


### 2. 查看目录的 ACL
```bash
getfacl /home/user1
```

如果目录有默认 ACL（影响新创建的文件），使用 `-d` 选项查看：
```bash
getfacl -d /home/user1
```

### 3. 递归显示 ACL
```bash
getfacl -R /data/
```
这个命令会显示 `/data/` 目录及其所有子文件和子目录的 ACL 权限信息。

## ACL 相关命令
| 命令 | 作用 |
|------|------|
| `setfacl` | 设置或修改 ACL 权限 |
| `getfacl` | 获取 ACL 权限 |

例如，使用 `setfacl` 给 `user2` 赋予 `example.txt` 的写权限：
```bash
setfacl -m u:user2:rw example.txt
```

然后再用 `getfacl` 查看，`user2` 的权限将变为 `rw-`。

## 总结
- `getfacl` 用于查看文件或目录的 ACL 权限。
- ACL 允许给特定用户或组赋予额外的权限，突破传统 `rwx` 权限限制。
- 配合 `setfacl` 命令可以管理 ACL 权限，增强文件访问控制的灵活性。

## 练习

1. 有一个文件 `example.txt`，`user1` 是文件所有者，`group1` 是文件所属组。你想给 `user2` 读写权限，你会如何设置 ACL？
答案：
```bash 
setfacl -m u:user2:rw example.txt
```
2. 有一个目录 `/data/`，你想让 `user2` 拥有该目录中
新创建文件的读写权限，你会如何设置默认 ACL？
答案：
```bash
setfacl -m d:u:user2:rw /data/
```

---

3. 有一个目录 `/data/`，你想让 `user2` 拥有该目录及其所有子文件的读写权限，你会如何设置 ACL？
答案：
```bash
setfacl -R -m u:user2:rw /data/
```
4. 有一个文件 `example.txt`，你想删除所有 ACL 规则，你会如何设置 ACL？
答案：
```bash
setfacl -b example.txt
```
5. 有一个文件 `example.txt`，你想查看其 ACL 信息，你会如何使用 `getfacl` 命令？
答案：
```bash
getfacl example.txt
```

---

<!-- _class: cover_e -->
<!-- _paginate: "" -->
<!-- _footer: "" -->

# Linux File System Permissions
###### “With great power comes great responsibility.”

Ming Jing
Big Data Research Institute

## 1. Introduction to File Permissions
- Every file and directory in Linux has an associated set of permissions.
- Permissions determine who can read, write, or execute the file.
- Controlled using `chmod`, `chown`, and `umask`.

## 2. Understanding Permission Representation
- Permissions are represented in symbolic and numeric format.
- Example:
  ```bash
  ls -l file.txt
  -rw-r--r-- 1 user group 1234 Mar 19 12:00 file.txt
  ```
- Breakdown:
  - `-` : File type (regular file `-`, directory `d`)
  - `rw-` : Owner permissions (read & write)
  - `r--` : Group permissions (read-only)
  - `r--` : Others permissions (read-only)

## 3. Modifying Permissions
- Change permissions using `chmod`:
  ```bash
  chmod 600 file.txt  # Owner: read/write, No access for others
  chmod 700 script.sh # Owner: full access, No access for others
  ```

- Change ownership using `chown`:
  ```bash
  chown user:group file.txt
  ```

- Change default permissions using `umask`:
  ```bash
  umask 077  # Default: No access for group & others
  ```

## 4. Preventing File Reading from Other Users
### Solution 1: Restrict File Permissions
```bash
chmod 700 file.txt  # Only owner can read, write, and execute
```

### Solution 2: Use ACLs (Access Control Lists)
```bash
setfacl -m u:otheruser:- file.txt  # Deny access to specific users
```

### Solution 3: Move Sensitive Files to a Private Directory
```bash
mkdir private && chmod 700 private
mv sensitive_data.txt private/
```

## 5. Preventing Data Leak Out of the System
### Solution 1: Disable USB Ports
```bash
echo "blacklist usb-storage" | sudo tee /etc/modprobe.d/usb-storage.conf
sudo modprobe -r usb-storage
```

### Solution 2: Restrict Network Access
```bash
iptables -A OUTPUT -p tcp --dport 80 -j DROP  # Block HTTP traffic
iptables -A OUTPUT -p tcp --dport 443 -j DROP # Block HTTPS traffic
```

### Solution 3: Use Mandatory Access Control (MAC)
- Configure SELinux or AppArmor:
  ```bash
  sudo apt install apparmor
  sudo aa-enforce /etc/apparmor.d/usr.bin.firefox
  ```

## 6. Summary
- Use `chmod`, `chown`, `umask` to control file access.
- Restrict access with ACLs and private directories.
- Prevent data leaks by disabling USB, restricting network access, and enforcing security policies.


