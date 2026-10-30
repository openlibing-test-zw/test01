// 测试 Checkstyle 屏蔽规则 - Java
// Checkstyle 使用 SUPPRESS CHECKSTYLE 和 CHECKSTYLE:OFF/ON 注释来屏蔽检查
// 行级：// SUPPRESS CHECKSTYLE rule / // SUPPRESS CHECKSTYLE ALL
// 块级：// CHECKSTYLE:OFF … // CHECKSTYLE:ON

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.BigInteger;

public class CheckstyleSuppressionTest {

    // ============================================================
    // 1. 行级屏蔽：// SUPPRESS CHECKSTYLE rule（屏蔽指定规则）
    // ============================================================

    // 屏蔽单个规则：MemberName（成员变量命名不符合规范）
    // SUPPRESS CHECKSTYLE MemberName
    private int x = 1;

    // 屏蔽单个规则：MagicNumber（魔术数字）
    // SUPPRESS CHECKSTYLE MagicNumber
    private static final int TIMEOUT = 30;

    // 屏蔽单个规则：LineLength（行过长）
    // SUPPRESS CHECKSTYLE LineLength
    private static final String VERY_LONG_CONSTANT_NAME = "This is a very long string value that exceeds the default line length limit set by checkstyle configuration";

    // 屏蔽多个规则（逗号分隔）
    // SUPPRESS CHECKSTYLE MemberName, MagicNumber
    private int y = 42;


    // ============================================================
    // 2. 行级屏蔽：// SUPPRESS CHECKSTYLE ALL（屏蔽所有规则）
    // ============================================================

    // SUPPRESS CHECKSTYLE ALL
    private int z = 999; // 命名不规范 + 魔术数字，全部屏蔽

    // SUPPRESS CHECKSTYLE ALL
    public void method_with_underscores() { // 命名不规范，屏蔽所有
        int _temp = 0; // 命名不规范
    }


    // ============================================================
    // 3. 同行屏蔽（注释在代码末尾）
    // ============================================================

    private int a = 100; // SUPPRESS CHECKSTYLE MagicNumber
    private int b = 200; // SUPPRESS CHECKSTYLE MemberName, MagicNumber
    private int c = 300; // SUPPRESS CHECKSTYLE ALL


    // ============================================================
    // 4. 块级屏蔽：// CHECKSTYLE:OFF … // CHECKSTYLE:ON（屏蔽指定规则）
    // ============================================================

    // CHECKSTYLE:OFF MemberName
    private int d = 1; // 不会触发 MemberName
    private int e = 2; // 不会触发 MemberName
    private int f = 3; // 不会触发 MemberName
    // CHECKSTYLE:ON MemberName

    private int g = 4; // 会触发 MemberName（已恢复检查）


    // ============================================================
    // 5. 块级屏蔽：// CHECKSTYLE:OFF（不带规则名，屏蔽所有规则）
    // ============================================================

    // CHECKSTYLE:OFF
    private int h = 100; // 不会触发任何规则
    private int i = 200; // 不会触发任何规则
    public void BAD_METHOD_NAME() { // 不会触发任何规则
        int _unused = 0;
    }
    // CHECKSTYLE:ON


    // ============================================================
    // 6. 多规则块级屏蔽
    // ============================================================

    // CHECKSTYLE:OFF MemberName, MagicNumber
    private int j = 42; // 不会触发 MemberName 和 MagicNumber
    private int k = 99; // 不会触发 MemberName 和 MagicNumber
    // CHECKSTYLE:ON MemberName, MagicNumber


    // ============================================================
    // 7. 嵌套块级屏蔽
    // ============================================================

    // CHECKSTYLE:OFF MemberName
    private int l = 1; // 不会触发 MemberName
    // CHECKSTYLE:OFF MagicNumber
    private int m = 100; // 不会触发 MemberName 和 MagicNumber
    // CHECKSTYLE:ON MagicNumber
    private int n = 200; // 不会触发 MemberName（外层仍屏蔽），会触发 MagicNumber（已恢复）
    // CHECKSTYLE:ON MemberName


    // ============================================================
    // 8. 不成对出现（只有 OFF 没有 ON）
    // ============================================================

    // CHECKSTYLE:OFF MemberName
    private int o = 1; // 不会触发 MemberName
    private int p = 2; // 不会触发 MemberName
    // 注意：此处没有 CHECKSTYLE:ON，MemberName 屏蔽持续到文件末尾


    // ============================================================
    // 9. 常见 Checkstyle 规则屏蔽示例
    // ============================================================

    // --- JavadocMethod（缺少方法 Javadoc）---
    // SUPPRESS CHECKSTYLE JavadocMethod
    public void noJavadocMethod() {
        // 没有 Javadoc 也不会触发警告
    }

    // --- JavadocType（缺少类 Javadoc）---
    // SUPPRESS CHECKSTYLE JavadocType
    class InnerClass {
        // 没有 Javadoc 也不会触发警告
    }

    // --- MethodName（方法命名不规范）---
    // SUPPRESS CHECKSTYLE MethodName
    public void XML_parser() {
        // 方法名不符合 camelCase 规范
    }

    // --- ParameterName（参数命名不规范）---
    // SUPPRESS CHECKSTYLE ParameterName
    public void method(int _param) {
        // 参数名不符合规范
    }

    // --- LocalVariableName（局部变量命名不规范）---
    // SUPPRESS CHECKSTYLE LocalVariableName
    public void localVar() {
        int _local = 0;
    }

    // --- AvoidStarImport（星号导入）---
    // SUPPRESS CHECKSTYLE AvoidStarImport
    // import java.util.*;  // 星号导入（此处仅注释说明）

    // --- EmptyBlock（空代码块）---
    // SUPPRESS CHECKSTYLE EmptyBlock
    public void emptyBlock() {
        try {
            // 正常代码
        } catch (Exception e) { // 空的 catch 块
        }
    }

    // --- EmptyCatchBlock（空 catch 块）---
    // SUPPRESS CHECKSTYLE EmptyCatchBlock
    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            // 空 catch 块
        }
    }

    // --- CyclomaticComplexity（圈复杂度过高）---
    // CHECKSTYLE:OFF CyclomaticComplexity
    public void complexMethod(int a, int b, int c) {
        if (a > 0) {
            if (b > 0) {
                if (c > 0) {
                    System.out.println("all positive");
                } else {
                    System.out.println("c not positive");
                }
            } else {
                if (c > 0) {
                    System.out.println("b not positive");
                } else {
                    System.out.println("b and c not positive");
                }
            }
        } else {
            if (b > 0) {
                if (c > 0) {
                    System.out.println("a not positive");
                } else {
                    System.out.println("a and c not positive");
                }
            } else {
                System.out.println("none positive");
            }
        }
    }
    // CHECKSTYLE:ON CyclomaticComplexity

    // --- MethodLength（方法过长）---
    // CHECKSTYLE:OFF MethodLength
    public void veryLongMethod() {
        System.out.println("line 1");
        System.out.println("line 2");
        System.out.println("line 3");
        System.out.println("line 4");
        System.out.println("line 5");
        System.out.println("line 6");
        System.out.println("line 7");
        System.out.println("line 8");
        System.out.println("line 9");
        System.out.println("line 10");
    }
    // CHECKSTYLE:ON MethodLength

    // --- NestedForDepth（嵌套 for 循环过深）---
    // CHECKSTYLE:OFF NestedForDepth
    public void deeplyNested() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    System.out.println(i + "," + j + "," + k);
                }
            }
        }
    }
    // CHECKSTYLE:ON NestedForDepth

    // --- IllegalCatch（非法捕获异常类型）---
    // SUPPRESS CHECKSTYLE IllegalCatch
    public void illegalCatch() {
        try {
            riskyOperation();
        } catch (RuntimeException e) { // 捕获 RuntimeException 可能不合适
            e.printStackTrace();
        }
    }

    // --- ReturnCount（返回语句过多）---
    // SUPPRESS CHECKSTYLE ReturnCount
    public int multipleReturns(int x) {
        if (x == 1) return 1;
        if (x == 2) return 2;
        if (x == 3) return 3;
        if (x == 4) return 4;
        return 0;
    }

    // --- ExecutableStatementCount（可执行语句过多）---
    // CHECKSTYLE:OFF ExecutableStatementCount
    public void manyStatements() {
        System.out.println("stmt1");
        System.out.println("stmt2");
        System.out.println("stmt3");
        System.out.println("stmt4");
        System.out.println("stmt5");
        System.out.println("stmt6");
        System.out.println("stmt7");
        System.out.println("stmt8");
        System.out.println("stmt9");
        System.out.println("stmt10");
    }
    // CHECKSTYLE:ON ExecutableStatementCount

    // --- AnonInnerLength（匿名内部类过长）---
    // SUPPRESS CHECKSTYLE AnonInnerLength
    Runnable longAnon = new Runnable() {
        @Override
        public void run() {
            System.out.println("line1");
            System.out.println("line2");
            System.out.println("line3");
            System.out.println("line4");
            System.out.println("line5");
            System.out.println("line6");
            System.out.println("line7");
            System.out.println("line8");
            System.out.println("line9");
            System.out.println("line10");
        }
    };

    // --- TrailingComment（行末注释）---
    // SUPPRESS CHECKSTYLE TrailingComment
    private int value = 0; // trailing comment

    // --- MultipleVariableDeclarations（多变量声明）---
    // SUPPRESS CHECKSTYLE MultipleVariableDeclarations
    private int x1, y1, z1; // 多个变量在同一行声明


    // ============================================================
    // 10. 自定义注释屏蔽（通过 checkstyle.xml 配置 SuppressionCommentFilter）
    // ============================================================

    // 自定义屏蔽注释需要在 checkstyle.xml 中配置 SuppressionCommentFilter
    // 常见自定义格式：

    // @SuppressWarnings("checkstyle:MemberName")
    private int custom_suppress = 1;

    // @SuppressWarnings("checkstyle:all")
    private int custom_all = 2;

    // CHECKSTYLE:IGNORE MemberName
    private int custom_ignore = 3;

    // CHECKSTYLE:IGNORE
    private int custom_ignore_all = 4;

    // 注意：以上自定义注释需要在 checkstyle.xml 中配置对应的 check 和 filter 才能生效
    // 例如：
    // <module name="SuppressionCommentFilter">
    //   <property name="offCommentFormat" value="CHECKSTYLE\:IGNORE"/>
    //   <property name="onCommentFormat" value="CHECKSTYLE\:RESUME"/>
    // </module>


    // ============================================================
    // 11. SuppressWithNearbyCommentFilter（附近注释屏蔽）
    // ============================================================

    // 通过 SuppressWithNearbyCommentFilter 配置，可以使用附近注释屏蔽
    // 需要在 checkstyle.xml 中配置：
    // <module name="SuppressWithNearbyCommentFilter">
    //   <property name="commentFormat" value="SUPPRESS CHECKSTYLE (\w+)"/>
    //   <property name="checkFormat" value="$1"/>
    //   <property name="influenceFormat" value="0"/>
    // </module>

    // SUPPRESS CHECKSTYLE MemberName influence 1
    private int nearby_var = 1;


    // ============================================================
    // 12. 正常代码（不屏蔽，会触发 Checkstyle 检测）
    // ============================================================

    // 以下代码会被 Checkstyle 检测到问题
    private int badName = 1; // 会触发 MemberName
    private static final int MAGIC = 42; // 会触发 MagicNumber

    public void BadMethodName() { // 会触发 MethodName
        int _badLocal = 0; // 会触发 LocalVariableName
    }

    private void riskyOperation() {
        // 模拟有风险的操作
    }
}
