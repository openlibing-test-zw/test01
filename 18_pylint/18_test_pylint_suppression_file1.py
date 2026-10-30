# pylint: disable=unused-import

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
import xml.etree.ElementTree  # 不会触发 unused-import

# 多个空格
import hashlib  # 不会触发 unused-import

# disable= 前后空格
import base64  # 不会触发 unused-import

# 规则名中可包含连字符
# pylint: disable=used-before-assignment
print(some_later_defined_var)  # noqa: 不会触发 used-before-assignment
some_later_defined_var = "defined after use"
