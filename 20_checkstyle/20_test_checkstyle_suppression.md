# 测试 Checkstyle 屏蔽规则 - Markdown

> Checkstyle 在 Markdown 等非代码文件中使用 `# // CHECKSTYLE:OFF` … `# // CHECKSTYLE:ON` 注释来屏蔽检查
> 注意：Markdown 中的 HTML 注释 `<!-- -->` 也可用于屏蔽

## 1. 行级屏蔽：# // SUPPRESS CHECKSTYLE rule

# // SUPPRESS CHECKSTYLE LineLength
This is a very long line of text in a markdown file that would normally trigger a line length check if checkstyle is configured to check markdown files for line length violations.

# // SUPPRESS CHECKSTYLE ALL
Another_line_with_issues="value"

## 2. 块级屏蔽：# // CHECKSTYLE:OFF … # // CHECKSTYLE:ON

# // CHECKSTYLE:OFF LineLength
This is a very long line inside a checkstyle off block that would normally trigger a line length warning in markdown files. The suppression allows us to write long lines without triggering warnings.
Another long line inside the suppression block that also exceeds the default line length limit.
# // CHECKSTYLE:ON LineLength

This line is outside the block and would trigger a warning if it exceeds the configured line length limit for markdown files.

## 3. 块级屏蔽（不带规则名，屏蔽所有规则）

# // CHECKSTYLE:OFF
No checkstyle rules are checked in this block.
Long lines, bad formatting, and other issues are all suppressed here.
# // CHECKSTYLE:ON

## 4. 多规则块级屏蔽

# // CHECKSTYLE:OFF LineLength, AvoidInlineConditionals
Multiple rules are suppressed in this block.
This is a very long line that would normally trigger LineLength.
# // CHECKSTYLE:ON LineLength, AvoidInlineConditionals

## 5. HTML 注释格式屏蔽（Markdown 特有）

<!-- CHECKSTYLE:OFF LineLength -->
This is a very long line inside an HTML comment style checkstyle off block that would normally trigger a line length warning.
<!-- CHECKSTYLE:ON LineLength -->

<!-- CHECKSTYLE:OFF -->
All checkstyle rules are suppressed in this HTML comment style block.
<!-- CHECKSTYLE:ON -->

## 6. 表格中的屏蔽

# // CHECKSTYLE:OFF LineLength
| Header 1 | Header 2 | Header 3 | This is a very long header that would normally trigger a line length warning in checkstyle |
|----------|----------|----------|-------------------------------------------------------------------------------------------|
| Data 1   | Data 2   | Data 3   | This is a very long cell value that would also trigger a line length warning             |
# // CHECKSTYLE:ON LineLength

## 7. 代码块中的屏蔽

```java
// CHECKSTYLE:OFF MemberName
private int x = 1;
private int y = 2;
// CHECKSTYLE:ON MemberName
```

```python
# // CHECKSTYLE:OFF LineLength
very_long_variable_name = "This is a very long string value that exceeds the default line length limit"
# // CHECKSTYLE:ON LineLength
```

## 8. 链接中的屏蔽

# // SUPPRESS CHECKSTYLE LineLength
[This is a very long link text that points to a URL](https://example.com/very/long/path/to/a/resource/that/would/normally/trigger/a/line/length/warning)

## 9. 不成对出现（只有 OFF 没有 ON）

# // CHECKSTYLE:OFF LineLength
This line is suppressed.
This line is also suppressed.
注意：此处没有 CHECKSTYLE:ON，屏蔽持续到文件末尾。

## 10. 正常内容（不屏蔽，会触发 Checkstyle 检测）

This is a very long line without any suppression comment that would normally trigger a line length warning if checkstyle is configured to check markdown files for line length violations.
