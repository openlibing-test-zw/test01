# 测试 Bandit 屏蔽规则 - Python
# Bandit 使用 # nosec 注释来屏蔽安全检查警告
# 格式：# nosec（屏蔽所有）或 # nosec rule1, rule2（屏蔽指定规则）

import os
import subprocess
import pickle
import hashlib
import random
import socket
import ssl
import tempfile
import yaml
import marshal
import shelve
import http.client
import xml.etree.ElementTree as ET
from subprocess import Popen, PIPE


# ============================================================
# 1. 基本格式：# nosec（屏蔽该行所有 Bandit 警告）
# ============================================================

# 标准格式
os.system("echo hello")  # nosec

# 注释符号后无空格
os.system("ls -la")  #nosec

# 多个空格
os.system("rm -rf /tmp/test")  #  nosec


# ============================================================
# 2. 指定规则屏蔽：# nosec rule1, rule2
# ============================================================

# 屏蔽单个规则 B602（subprocess_popen_with_shell_equals_true）
subprocess.Popen("ls -la", shell=True)  # nosec B602

# 屏蔽单个规则 B605（any_other_function_with_shell_equals_true）
os.popen("cat /etc/passwd")  # nosec B605

# 屏蔽多个规则（逗号分隔）
os.system("echo " + "hello")  # nosec B602, B605

# 规则名之间有空格
os.system("whoami")  # nosec B602,  B605


# ============================================================
# 3. 同行屏蔽（注释在代码末尾，最常用方式）
# ============================================================

# 命令注入
os.system("echo test")  # nosec
subprocess.call("ls", shell=True)  # nosec
subprocess.Popen("cat file.txt", shell=True)  # nosec B602
os.popen("ls -la")  # nosec B605

# SQL 注入（虽然 Bandit 主要检测命令注入，但也会检测格式化字符串）
query = "SELECT * FROM users WHERE id = %s" % "1"  # nosec B608


# ============================================================
# 4. 上一行屏蔽（# nosec 放在代码上一行）
# ============================================================

# nosec
os.system("echo previous line")

# nosec B602
subprocess.Popen("ls", shell=True)


# ============================================================
# 5. 常见 Bandit 规则屏蔽示例
# ============================================================

# --- B101: assert 语句（测试中常用） ---
# nosec B101
assert True, "This should always pass"

# --- B311: random 模块（非加密安全） ---
random.randint(1, 100)  # nosec B311
random.choice([1, 2, 3])  # nosec B311

# --- B301-B303: pickle 反序列化 ---
data = b"\x80\x04\x95\x05\x00\x00\x00\x00\x00\x00\x00\x8c\x01a\x94."
pickle.loads(data)  # nosec B301

# --- B312: telnetlib ---
# http.client 本身不触发，但某些旧库会
# telnetlib 已在 Python 3.13 移除，此处仅作示例

# --- B313-B320: xml 解析 ---
tree = ET.parse("untrusted.xml")  # nosec B314

# --- B404: subprocess 模块导入 ---
# import subprocess 本身可能触发 B404，但通常在文件顶部
# 此处仅作注释说明

# --- B506: yaml.load 不安全 ---
yaml.load("key: value", Loader=yaml.FullLoader)  # nosec B506

# --- B602: subprocess_popen_with_shell_equals_true ---
Popen("echo test", shell=True, stdout=PIPE)  # nosec B602

# --- B603: subprocess_without_shell_equals_true ---
# 不使用 shell=True 的 subprocess 调用通常安全，但某些场景仍需屏蔽
subprocess.call(["ls", "-la"])  # nosec B603

# --- B605: any_other_function_with_shell_equals_true ---
os.popen("echo hello")  # nosec B605

# --- B606: start_process_with_no_path ---
# nosec B606
os.execvp("ls", ["ls", "-la"])

# --- B607: start_process_with_partial_path ---
subprocess.Popen(["ping", "localhost"])  # nosec B607

# --- B608: hardcoded_sql_expressions ---
sql = "SELECT * FROM users WHERE name = '%s'" % "admin"  # nosec B608

# --- B701: jinja2_autoescape_false ---
# 需要安装 jinja2，此处仅作注释说明
# template = jinja2.Environment(autoescape=False)  # nosec B701

# --- B702: use_of_mako_templates ---
# 需要安装 mako，此处仅作注释说明

# --- B703: django_mark_safe ---
# 需要安装 django，此处仅作注释说明


# ============================================================
# 6. 多个安全问题的代码行
# ============================================================

# 一行代码可能触发多个 Bandit 规则
cmd = "ls " + "-la"  # nosec B606, B607
os.system(cmd)  # nosec B605


# ============================================================
# 7. 函数中的屏蔽
# ============================================================

def run_command(user_input):
    """运行系统命令"""
    # nosec B602
    result = subprocess.Popen(user_input, shell=True, stdout=PIPE, stderr=PIPE)
    return result.communicate()

def execute_safe_command():
    """执行安全命令"""
    os.system("echo 'safe command'")  # nosec B605

def load_config(config_str):
    """加载 YAML 配置"""
    return yaml.load(config_str, Loader=yaml.FullLoader)  # nosec B506


# ============================================================
# 8. 类中的屏蔽
# ============================================================

class CommandExecutor:
    """命令执行器"""

    def __init__(self, command):
        self.command = command

    def execute(self):
        # nosec B602
        return subprocess.Popen(self.command, shell=True, stdout=PIPE)

    def safe_execute(self):
        os.system("echo safe")  # nosec B605


# ============================================================
# 9. 上下文管理器中的屏蔽
# ============================================================

def socket_example():
    """Socket 示例"""
    # nosec B104  (hardcoded_bind_all_interfaces)
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("0.0.0.0", 8080))  # nosec B104
    s.listen(5)
    return s


# ============================================================
# 10. SSL/TLS 相关屏蔽
# ============================================================

def ssl_example():
    """SSL 示例"""
    context = ssl.create_default_context()
    # nosec B323  (slowness_of_md5)
    context.check_hostname = False  # nosec B323
    context.verify_mode = ssl.CERT_NONE  # nosec B323
    return context


# ============================================================
# 11. 临时文件相关屏蔽
# ============================================================

def temp_file_example():
    """临时文件示例"""
    # nosec B108  (hardcoded_tmp_directory)
    tmp = tempfile.NamedTemporaryFile(dir="/tmp")  # nosec B108
    return tmp


# ============================================================
# 12. hashlib 相关屏蔽
# ============================================================

def hash_example():
    """哈希示例"""
    # nosec B324  (hashlib_new_insecure_functions)
    h = hashlib.md5()  # nosec B303, B324
    h.update(b"data")
    return h.hexdigest()

    # nosec B303  (md5_usage)
    h2 = hashlib.sha1()  # nosec B303
    h2.update(b"data")
    return h2.hexdigest()


# ============================================================
# 13. marshal/shelve 相关屏蔽
# ============================================================

def marshal_example():
    """marshal 示例"""
    data = b"\x00\x00\x00\x00"
    marshal.loads(data)  # nosec B302

def shelve_example():
    """shelve 示例"""
    db = shelve.open("test.db")  # nosec B301


# ============================================================
# 14. 多行代码块屏蔽（每行都需要 # nosec）
# ============================================================

# Bandit 是行级屏蔽，每行都需要单独添加 # nosec
os.system("echo line1")  # nosec
os.system("echo line2")  # nosec
os.system("echo line3")  # nosec


# ============================================================
# 15. 文件级屏蔽（在文件顶部使用）
# ============================================================

# 注意：以下注释应放在文件顶部，这里仅作示例
# 在文件顶部添加 # nosec 不会屏蔽整个文件
# Bandit 没有文件级屏蔽功能，必须逐行添加 # nosec
# 但可以通过 Bandit 配置文件（.bandit 或 pyproject.toml）的 skips 来实现


# ============================================================
# 16. 正常代码（不屏蔽，会触发 Bandit 检测）
# ============================================================

# 以下代码会被 Bandit 检测到安全问题
os.system("echo unsafe")
subprocess.Popen("ls -la", shell=True)
pickle.loads(b"\x80\x04\x95\x05\x00\x00\x00\x00\x00\x00\x00\x8c\x01a\x94.")
random.randint(1, 100)
hashlib.md5(b"data")
