
## 问题一：已经push的commit如何修改message
注意：在修复历史commit message的时候，请确保当前分支是最新代码，且已经提交了所有本地修改。

### 1. 查看历史记录
```bash
git log
git log --oneline
git log --name-status 
# 指定n为1则可以查看最近一次修改的内容
git log -p -${n}
```
### 2. 确定要修改哪些commit
```bash
# 查看并修改最近的commit
git rebase -i HEAD~2

# 输出：第一列是命令，第二列是cimmitId，第三列的commit的message
pick 9af7f60 this is a message
pick 2b78b3d 这是提交的message
pick f64c914 message
```
### 3. 修改commit的message
- 选择提交的commitId，将前面的pick改为reword
- 在新弹出的页面中输入新的message
- 保存并关闭

### 4. push到远程仓库中
```bash
# 使用-f：表示将目前自己本机的代码库推送到远端，并覆盖
git push -f
git push --force
```
注意：正是因为 -f 参数，会强制将远程仓库覆盖，因此进行操作时一定要“确保当前分支是最新代码，且已经提交了所有本地修改”

### 5. 如果仅是修改最近一次commit的message，没有push
```bash
git commit --amend
```
此时会进入默认vim编辑器，修改注释完毕后保存就好了。

### 模板：

```
[TicketNo:] ${number}
[Description:] ${title}
[Binary Source:] NA
```

--------

## 问题二： commit之后，想撤销commit
> https://blog.csdn.net/w958796636/article/details/53611133

```bash
# 仅仅是撤回commit操作，您写的代码仍然保留
git reset --soft HEAD^

########### 参数说明###########

# 不删除工作空间改动代码，撤销commit，并且撤销git add . 操作
--mixed （默认）
# 举例，两者效果相同
git reset --mixed HEAD^ 
git reset HEAD^ 

--soft  
不删除工作空间改动代码，撤销commit，不撤销git add . 

--hard
删除工作空间改动代码，撤销commit，撤销git add . 
注意完成这个操作后，就恢复到了上一次的commit状态。

git reset "commit ID"
git reset "文件名"

```

- 说明：
   1. HEAD^的意思是上一个版本，也可以写成HEAD~1
   2. 如果你进行了2次commit，想都撤回，可以使用HEAD~2
   3. git无法回退单个文件到某次提交状态。如果非要这样做，需要获取当时提交的内容，然后覆盖现有文件。
   
-------

## 问题三：clean

```bash
# 清除所有未跟踪文件，包括纳入.gitignore中的文件
git clean -dxf 
``` 
------
## 问题四：修改乱码问题
- git add/status 乱码
```bash
git config --global core.quotepath false
```

- git commit/log 乱码
```bash
git config --global i18n.commitencoding utf-8
git config --global i18n.logoutputencoding utf-8
export LESSCHARSET=utf-8
```

- 修改命令行工具的编码
在MING64中右键-Options-Text-Locale
选中 zh_CN 和 GBK


-----
## 问题五：如何释放gitlab的空间
（git提交了二进制大文件之后产生的影响）
> https://www.jianshu.com/p/e23b1b18b62b

> https://www.cnblogs.com/msxh/p/11082246.html

重要参考思路：
> https://www.zhihu.com/question/29769130 

-----

## 问题六：如何查看没有被push的commit信息
```bash
# 简单的查看没有被push的commit数量，使用下面的命令
git status
# 会出现
# Your branch is ahead of 'origin/master' by 3 commits.
# 这样的提示

# 查看这些commit的message信息
git cherry -v

# 查看commit的详细信息

```

-----

## 问题六：git push/fetch的区别，以及如何merge






