# 测试 pylint 屏蔽规则 - Python
# pylint 使用 # pylint: disable= / # pylint: enable= / # pylint: disable-next= 注释来屏蔽警告
# 特点：disable/enable 可以不成对出现，支持文件级屏蔽

import os
import sys
from typing import List, Optional


# ============================================================
# 1. 基本格式：# pylint: disable=rule
# ============================================================

# 标准格式：屏蔽单个规则
# pylint: disable=unused-import
import json  # 不会触发 unused-import 警告

# 屏蔽多个规则（逗号分隔）
# pylint: disable=unused-import, unused-variable
import csv  # 不会触发 unused-import
unused_var = 42  # 不会触发 unused-variable

# 正则匹配格式：^\s*#\s*pylint\s*:\s*(disable|enable)\s*=\s*[\w\-\.,\s]+
# 以下各种空格变体都符合正则

# 冒号后无空格
#pylint:disable=unused-import
import xml.etree.ElementTree  # 不会触发 unused-import

# 多个空格
#  pylint:  disable=unused-import
import hashlib  # 不会触发 unused-import

# disable= 前后空格
# pylint: disable = unused-import
import base64  # 不会触发 unused-import

# 规则名中可包含连字符
# pylint: disable=used-before-assignment
print(some_later_defined_var)  # noqa: 不会触发 used-before-assignment
some_later_defined_var = "defined after use"


# ============================================================
# 2. disable-next：屏蔽下一行
# ============================================================

# pylint: disable-next=unused-variable
next_line_unused = 100  # 不会触发 unused-variable

# pylint: disable-next=unused-variable, pointless-statement
next_line_unused2 = 200; next_line_unused2  # 不会触发 unused-variable 和 pointless-statement

# disable-next 空格变体
#pylint:disable-next=unused-variable
next_line_unused3 = 300

#  pylint:  disable-next  =  unused-variable
next_line_unused4 = 400


# ============================================================
# 3. 同行屏蔽（注释在代码末尾）
# ============================================================

inline_unused = 500  # pylint: disable=unused-variable
inline_unused2 = 600  # pylint: disable=unused-variable, pointless-statement


# ============================================================
# 4. disable/enable 块级屏蔽
# ============================================================

# 开始屏蔽
# pylint: disable=unused-variable
block_var1 = 10  # 不会触发 unused-variable
block_var2 = 20  # 不会触发 unused-variable
block_var3 = 30  # 不会触发 unused-variable
# 结束屏蔽
# pylint: enable=unused-variable
block_var4 = 40  # 会触发 unused-variable（已重新启用）


# ============================================================
# 5. disable/enable 可以不成对出现
# ============================================================

# 只有 disable 没有 enable，后续所有代码都不会触发该规则
# pylint: disable=unused-variable
unpaired_var1 = 70  # 不会触发 unused-variable
unpaired_var2 = 80  # 不会触发 unused-variable
# 注意：此处没有 enable，unused-variable 屏蔽持续到文件末尾


# ============================================================
# 6. 多规则块级屏蔽
# ============================================================

# pylint: disable=unused-variable, pointless-statement, bare-except
multi_block_var1 = 90  # 不会触发 unused-variable
multi_block_var1  # 不会触发 pointless-statement
try:
    risky_operation()
except:  # 不会触发 bare-except
    pass
# pylint: enable=unused-variable, pointless-statement, bare-except


# ============================================================
# 7. 部分规则 enable（只恢复部分规则）
# ============================================================

# pylint: disable=unused-variable, pointless-statement
partial_var1 = 100  # 不会触发 unused-variable
partial_var1  # 不会触发 pointless-statement
# 只恢复 unused-variable，pointless-statement 仍然屏蔽
# pylint: enable=unused-variable
partial_var2 = 110  # 会触发 unused-variable（已恢复）
partial_var1  # 不会触发 pointless-statement（仍然屏蔽）


# ============================================================
# 8. 文件级屏蔽（放在文件顶部）
# ============================================================

# 注意：以下注释应放在文件顶部，这里仅作示例展示
# 文件级屏蔽方式 1：在文件顶部使用 disable
# # pylint: disable=unused-import
# 这样整个文件中的 unused-import 都不会触发

# 文件级屏蔽方式 2：跳过整个文件
# # pylint: skip-file
# 这样整个文件都不会被 pylint 检查


# ============================================================
# 9. 常见 pylint 规则屏蔽示例
# ============================================================

# 屏蔽 broad-except（过宽的异常捕获）
# pylint: disable=broad-except
try:
    result = 1 / 0
except Exception as e:  # 不会触发 broad-except
    print(f"Error: {e}")
# pylint: enable=broad-except

# 屏蔽 too-many-arguments（参数过多）
# pylint: disable=too-many-arguments
def many_params(a, b, c, d, e, f, g, h):  # 不会触发 too-many-arguments
    return sum([a, b, c, d, e, f, g, h])
# pylint: enable=too-many-arguments

# 屏蔽 too-many-locals（局部变量过多）
# pylint: disable=too-many-locals
def many_locals():  # 不会触发 too-many-locals
    v1, v2, v3, v4, v5 = 1, 2, 3, 4, 5
    v6, v7, v8, v9, v10 = 6, 7, 8, 9, 10
    v11, v12, v13, v14, v15 = 11, 12, 13, 14, 15
    return v1 + v2 + v3
# pylint: enable=too-many-locals

# 屏蔽 too-few-public-methods（公有方法过少）
# pylint: disable=too-few-public-methods
class SmallClass:  # 不会触发 too-few-public-methods
    def only_method(self):
        pass
# pylint: enable=too-few-public-methods

# 屏蔽 line-too-long（行过长）
# pylint: disable=line-too-long
very_long_line = "This is a very long line that exceeds the default 100 character limit set by pylint, and it should not trigger the line-too-long warning because we have disabled it."  # noqa
# pylint: enable=line-too-long

# 屏蔽 fixme（TODO/FIXME 注释警告）
# pylint: disable=fixme
# TODO: This is a todo that won't trigger fixme warning
# FIXME: This is a fixme that won't trigger fixme warning
# pylint: enable=fixme

# 屏蔽 missing-docstring（缺少文档字符串）
# pylint: disable=missing-docstring
def no_docstring_func():  # 不会触发 missing-function-docstring
    pass

class NoDocstringClass:  # 不会触发 missing-class-docstring
    pass
# pylint: enable=missing-docstring

# 屏蔽 invalid-name（无效命名）
# pylint: disable=invalid-name
x = 42  # 不会触发 invalid-name（变量名太短）
MyClass = type("MyClass", (), {})  # 不会触发 invalid-name（CamelCase 赋值给变量）
# pylint: enable=invalid-name

# 屏蔽 consider-using-f-string（建议使用 f-string）
# pylint: disable=consider-using-f-string
name = "world"
old_style = "Hello %s" % name  # 不会触发 consider-using-f-string
# pylint: enable=consider-using-f-string

# 屏蔽 global-statement（全局变量声明）
# pylint: disable=global-statement
global_counter = 0
def increment():  # 不会触发 global-statement
    global global_counter
    global_counter += 1
# pylint: enable=global-statement

# 屏蔽 redefined-outer-name（外部名称重定义）
# pylint: disable=redefined-outer-name
def outer():
    name = "outer"  # 不会触发 redefined-outer-name（与模块级 name 重名）
    return name
# pylint: enable=redefined-outer-name


# ============================================================
# 10. 使用 all 特殊值屏蔽所有警告
# ============================================================

# pylint: disable=all
everything_disabled = True  # 不会触发任何 pylint 警告
# pylint: enable=all


# ============================================================
# 11. 类中的屏蔽
# ============================================================

class PylintTestClass:
    """测试 pylint 屏蔽在类中的使用"""

    # pylint: disable=invalid-name
    def test_method(self):
        x = 1  # 不会触发 invalid-name
        y = 2  # 不会触发 invalid-name
        return x + y
    # pylint: enable=invalid-name

    # pylint: disable-next=invalid-name
    def another_method(self):
        z = 3  # 不会触发 invalid-name（仅下一行）
        w = 4  # 会触发 invalid-name（w 太短）
        return z + w


# ============================================================
# 12. 装饰器中的屏蔽
# ============================================================

# pylint: disable=unused-argument
def decorated_func(unused_arg1, unused_arg2):  # 不会触发 unused-argument
    return "result"
# pylint: enable=unused-argument


# ============================================================
# 13. 嵌套屏蔽
# ============================================================

# pylint: disable=unused-variable
outer_nested = 1  # 不会触发
# pylint: disable=pointless-statement
outer_nested  # 不会触发 pointless-statement
# pylint: enable=pointless-statement
outer_nested2 = 2  # 不会触发 unused-variable（外层仍屏蔽）
# pylint: enable=unused-variable


# ============================================================
# 14. 正常代码（不屏蔽，会触发 pylint 警告）
# ============================================================

# 以下代码会被 pylint 检测到问题
unused_normal = 999  # 会触发 unused-variable
short = 1  # 会触发 invalid-name（变量名太短）

def too_many_args_normal(a, b, c, d, e, f, g, h, i, j):  # 会触发 too-many-arguments
    return sum([a, b, c, d, e, f, g, h, i, j])


def risky_operation():
    """模拟一个可能出错的操作"""
    return 1 / 0
