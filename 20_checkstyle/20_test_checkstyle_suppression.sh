#!/bin/bash
# 测试 Checkstyle 屏蔽规则 - Shell
# Checkstyle 在 shell 等非 Java 文件中使用 # // CHECKSTYLE:OFF … # // CHECKSTYLE:ON 注释来屏蔽检查
# 行级：# // SUPPRESS CHECKSTYLE rule / # // SUPPRESS CHECKSTYLE ALL
# 块级：# // CHECKSTYLE:OFF … # // CHECKSTYLE:ON


# ============================================================
# 1. 行级屏蔽：# // SUPPRESS CHECKSTYLE rule
# ============================================================

# 屏蔽单个规则
# // SUPPRESS CHECKSTYLE LineLength
echo "This is a very long line that exceeds the default line length limit and would normally trigger a checkstyle warning in shell scripts"

# 屏蔽多个规则
# // SUPPRESS CHECKSTYLE LineLength, AvoidInlineConditionals
[ -f "/tmp/test" ] && echo "exists" || echo "not exists"  # // SUPPRESS CHECKSTYLE AvoidInlineConditionals


# ============================================================
# 2. 行级屏蔽：# // SUPPRESS CHECKSTYLE ALL
# ============================================================

# // SUPPRESS CHECKSTYLE ALL
very_long_variable_name_with_underscores="this would normally trigger multiple checkstyle warnings but all are suppressed"


# ============================================================
# 3. 同行屏蔽（注释在代码末尾）
# ============================================================

echo "test"  # // SUPPRESS CHECKSTYLE ALL
long_line="very long value that exceeds limits"  # // SUPPRESS CHECKSTYLE LineLength


# ============================================================
# 4. 块级屏蔽：# // CHECKSTYLE:OFF … # // CHECKSTYLE:ON
# ============================================================

# // CHECKSTYLE:OFF LineLength
echo "This is a very long line inside a checkstyle off block that would normally trigger a line length warning"
echo "Another long line inside the checkstyle off block that also exceeds the default line length limit"
# // CHECKSTYLE:ON LineLength

echo "This line is outside the block and would trigger a warning if it exceeds the line length limit configured by checkstyle"


# ============================================================
# 5. 块级屏蔽（不带规则名，屏蔽所有规则）
# ============================================================

# // CHECKSTYLE:OFF
echo "No checkstyle rules are checked in this block"
long_var_name="value"
echo "Another line with no checkstyle enforcement"
# // CHECKSTYLE:ON


# ============================================================
# 6. 多规则块级屏蔽
# ============================================================

# // CHECKSTYLE:OFF LineLength, AvoidInlineConditionals
echo "Long line with inline conditional" && [ 1 -eq 1 ] && echo "true" || echo "false"
echo "Another long line in the multi-rule suppression block"
# // CHECKSTYLE:ON LineLength, AvoidInlineConditionals


# ============================================================
# 7. 嵌套块级屏蔽
# ============================================================

# // CHECKSTYLE:OFF LineLength
echo "Outer block: long line suppressed"
# // CHECKSTYLE:OFF AvoidInlineConditionals
[ -f "/tmp/test" ] && echo "exists" || echo "not exists"
echo "Both rules suppressed in this inner block"
# // CHECKSTYLE:ON AvoidInlineConditionals
echo "Only LineLength suppressed here"
# // CHECKSTYLE:ON LineLength


# ============================================================
# 8. 不成对出现（只有 OFF 没有 ON）
# ============================================================

# // CHECKSTYLE:OFF LineLength
echo "This line is suppressed"
echo "This line is also suppressed"
# 注意：此处没有 CHECKSTYLE:ON，屏蔽持续到文件末尾


# ============================================================
# 9. 函数中的屏蔽
# ============================================================

process_data() {
    # // SUPPRESS CHECKSTYLE ALL
    local result=$(echo "processing data with a very long command that exceeds the line length limit")
    echo "$result"

    # // CHECKSTYLE:OFF LineLength
    local another_result=$(curl -s "https://example.com/api/v1/data?param1=value1&param2=value2&param3=value3")
    echo "$another_result"
    # // CHECKSTYLE:ON LineLength
}


# ============================================================
# 10. 条件语句中的屏蔽
# ============================================================

if [ -f "/etc/config" ]; then
    # // SUPPRESS CHECKSTYLE LineLength
    echo "Configuration file found at /etc/config with a very long message that would normally trigger a warning"
fi


# ============================================================
# 11. 循环中的屏蔽
# ============================================================

# // CHECKSTYLE:OFF LineLength
for file in /path/to/very/long/directory/structure/that/exceeds/normal/limits/*; do
    echo "Processing: $file"
done
# // CHECKSTYLE:ON LineLength


# ============================================================
# 12. 正常代码（不屏蔽，会触发 Checkstyle 检测）
# ============================================================

# 以下代码会被 Checkstyle 检测到问题（如果配置了 shell 脚本检查）
echo "This is a very long line without suppression that would trigger a line length warning in checkstyle"
