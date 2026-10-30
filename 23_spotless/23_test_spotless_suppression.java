// 测试 Spotless 屏蔽规则 - Java
// Spotless 使用 // spotless:off … // spotless:on 注释来屏蔽代码格式化
// 特点：Spotless 是代码格式化工具，屏蔽注释用于跳过格式化检查
// 支持自定义注释（通过配置 spotless.setToggleOff/on 定义）

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.BigInteger;

public class SpotlessSuppressionTest {

    // ============================================================
    // 1. 基本格式：// spotless:off … // spotless:on
    // ============================================================

    // spotless:off
    private int x=1;   // 格式化被屏蔽：等号两侧没有空格
    private int y  =  2;   // 格式化被屏蔽：多余空格不会被修正
    // spotless:on

    private int z = 3; // 格式化正常生效：等号两侧有空格


    // ============================================================
    // 2. 同行屏蔽（注释在代码末尾）
    // ============================================================

    private int a=1; // spotless:off
    // 上一行格式化被屏蔽

    private int b = 2; // spotless:on
    // 格式化恢复


    // ============================================================
    // 3. 多行块级屏蔽
    // ============================================================

    // spotless:off
    public void badlyFormatted(){
        if(true){
            System.out.println("no spaces around if");
            int x=1+2*3;
        String s="no spaces";
        }
    }
    // spotless:on

    // 上面的代码不会被 Spotless 格式化


    // ============================================================
    // 4. 嵌套屏蔽（不推荐，但语法上允许）
    // ============================================================

    // spotless:off
    private int outer=1; // 外层屏蔽
    // spotless:off
    private int inner=2; // 内层屏蔽（冗余但语法上允许）
    // spotless:on
    private int stillOff=3; // 仍在屏蔽中（外层未关闭）
    // spotless:on

    private int normal = 4; // 格式化正常生效


    // ============================================================
    // 5. 不成对出现（只有 off 没有 on）
    // ============================================================

    // spotless:off
    private int unpaired1=1; // 格式化被屏蔽
    private int unpaired2=2; // 格式化被屏蔽
    // 注意：此处没有 spotless:on，格式化屏蔽持续到文件末尾


    // ============================================================
    // 6. 方法中的屏蔽
    // ============================================================

    public void methodWithCustomFormatting() {
        // spotless:off
        if(condition1){
        doSomething( arg1,  arg2 );
        int result=calculate( a,b,c );
        }
        // spotless:on

        // 正常格式化的代码
        if (condition2) {
            doSomething(arg1, arg2);
            int result = calculate(a, b, c);
        }
    }

    private boolean condition1 = true;
    private boolean condition2 = true;
    private String arg1 = "a";
    private String arg2 = "b";
    private int a = 1, b = 2, c = 3;

    private void doSomething(String a, String b) {}
    private int calculate(int a, int b, int c) { return a + b + c; }


    // ============================================================
    // 7. 类初始化块中的屏蔽
    // ============================================================

    {
        // spotless:off
        int init=1;
        String msg="initialization block with custom formatting";
        // spotless:on

        int normal = 2;
        String message = "normal formatting";
    }


    // ============================================================
    // 8. 静态初始化块中的屏蔽
    // ============================================================

    static {
        // spotless:off
        int staticInit=1;
        // spotless:on

        int staticNormal = 2;
    }


    // ============================================================
    // 9. 匿名内部类中的屏蔽
    // ============================================================

    public void anonymousClass() {
        Runnable r = new Runnable() {
            // spotless:off
            @Override public void run(){
                System.out.println("custom formatted");
            }
            // spotless:on

            @Override
            public String toString() {
                return "normal formatted";
            }
        };
    }


    // ============================================================
    // 10. 枚举中的屏蔽
    // ============================================================

    enum Status {
        // spotless:off
        ACTIVE,INACTIVE,PENDING;
        // spotless:on

        // 正常格式化
        UNKNOWN
    }


    // ============================================================
    // 11. 注解中的屏蔽
    // ============================================================

    // spotless:off
    @Deprecated@SuppressFBWarnings("URF_UNREAD_FIELD")
    // spotless:on
    private int annotatedField = 1;


    // ============================================================
    // 12. 泛型中的屏蔽
    // ============================================================

    // spotless:off
    private Map<String,List<Integer>> genericMap=new HashMap<>();
    // spotless:on

    private Map<String, List<Integer>> normalGenericMap = new HashMap<>();


    // ============================================================
    // 13. Lambda 表达式中的屏蔽
    // ============================================================

    public void lambdaExample() {
        // spotless:off
        java.util.function.Function<Integer,Integer> f=x->x+1;
        // spotless:on

        java.util.function.Function<Integer, Integer> normal = x -> x + 1;
    }


    // ============================================================
    // 14. 长行屏蔽（避免自动换行）
    // ============================================================

    // spotless:off
    private static final String VERY_LONG_CONSTANT = "This is a very long string that would normally be wrapped or reformatted by spotless but is suppressed because we want to keep it on a single line for readability in this specific context";
    // spotless:on


    // ============================================================
    // 15. SQL/JSON 字符串屏蔽（保持字符串格式不变）
    // ============================================================

    public String getSqlQuery() {
        // spotless:off
        String sql = "SELECT u.id, u.name, u.email FROM users u WHERE u.status = 'active' AND u.created_at > '2024-01-01' ORDER BY u.name ASC";
        // spotless:on
        return sql;
    }

    public String getJsonTemplate() {
        // spotless:off
        String json = "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"zip\":\"10001\"}}";
        // spotless:on
        return json;
    }


    // ============================================================
    // 16. 表驱动测试中的屏蔽
    // ============================================================

    public void tableDrivenTest() {
        // spotless:off
        Object[][] testCases = {
            {"input1",  "expected1",  "description1"},
            {"input2",  "expected2",  "description2"},
            {"input3",  "expected3",  "description3"},
            {"input4",  "expected4",  "description4"},
            {"input5",  "expected5",  "description5"},
        };
        // spotless:on
    }


    // ============================================================
    // 17. 矩阵/表格数据中的屏蔽
    // ============================================================

    public void matrixData() {
        // spotless:off
        int[][] matrix = {
            {1,  2,  3,  4,  5},
            {6,  7,  8,  9,  10},
            {11, 12, 13, 14, 15},
            {16, 17, 18, 19, 20},
            {21, 22, 23, 24, 25}
        };
        // spotless:on
    }


    // ============================================================
    // 18. 位运算中的屏蔽（保持对齐）
    // ============================================================

    public void bitOperations() {
        // spotless:off
        int flags = 0x0001 | 0x0002 | 0x0004 | 0x0008;
        int mask = 0xFF00 & 0x00FF;
        // spotless:on
    }


    // ============================================================
    // 19. 自定义注释（通过 Spotless 配置）
    // ============================================================

    // Spotless 支持通过配置自定义 toggle 注释
    // 在 build.gradle 或 pom.xml 中配置：
    //
    // Gradle:
    // spotless {
    //     java {
    //         toggleOffOn  // 默认使用 spotless:off / spotless:on
    //         // 或自定义：
    //         // toggleOffOn("format:off", "format:on")
    //         // toggleOffOn("formatter:off", "formatter:on")
    //     }
    // }
    //
    // Maven:
    // <configuration>
    //   <java>
    //     <toggleOffOn>format:off format:on</toggleOffOn>
    //   </java>
    // </configuration>

    // 以下是自定义注释的示例（需要在配置中启用才能生效）

    // format:off
    private int customFormat1=1; // 自定义屏蔽注释（需配置）
    // format:on

    // formatter:off
    private int customFormat2=2; // 另一种自定义屏蔽注释（需配置）
    // formatter:on

    // DISABLE_FORMATTER
    private int customFormat3=3; // 再一种自定义屏蔽注释（需配置）
    // ENABLE_FORMATTER


    // ============================================================
    // 20. 常见使用场景汇总
    // ============================================================

    // 场景1：保持对齐的注释表格
    // spotless:off
    // Name    | Type    | Description
    // --------|---------|------------
    // alpha   | String  | First param
    // beta    | Integer | Second param
    // gamma   | Boolean | Third param
    // spotless:on

    // 场景2：保持特殊缩进（如 ASCII 艺术）
    // spotless:off
    /*
     *   +---+
     *   |   |
     *   +---+
     */
    // spotless:on

    // 场景3：保持生成的代码原样
    // spotless:off
    // AUTO-GENERATED CODE - DO NOT MODIFY
    private int generated1=1;
    private int generated2=2;
    // END AUTO-GENERATED CODE
    // spotless:on

    // 场景4：保持兼容性格式（如旧版 API 序列化格式要求）
    // spotless:off
    public String toLegacyFormat(){return "legacy:"+this.x+","+this.y;}
    // spotless:on


    // ============================================================
    // 21. 正常代码（不屏蔽，会被 Spotless 格式化）
    // ============================================================

    // 以下代码会被 Spotless 格式化修正
    private int unformatted=1; // 会被修正为 unformatted = 1

    public void unformattedMethod(){
        if(true){
            System.out.println("bad formatting");
        }
    }
}
