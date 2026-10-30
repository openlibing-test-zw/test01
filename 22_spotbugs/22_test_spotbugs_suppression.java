// 测试 SpotBugs 屏蔽规则 - Java
// SpotBugs 使用 @SuppressFBWarnings 注解来屏蔽检查
// 格式：
//   @SuppressFBWarnings("rule")                           - 屏蔽单个规则
//   @SuppressFBWarnings(value = {"rule1", "rule2"})       - 屏蔽多个规则
//   @SuppressFBWarnings(value = "rule", justification = "explanation")  - 带解释
//   @SuppressFBWarnings(value = "rule", justification = "explanation", matchType = SuppressMatchType.EXACT)  - 精确匹配

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// ============================================================
// 1. 基本格式：@SuppressFBWarnings("rule")（屏蔽单个规则）
// ============================================================

class BasicSuppression {

    // 屏蔽 EI_EXPOSE_REP2（构造器暴露内部引用）
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public BasicSuppression(Date date) {
        this.internalDate = date;
    }

    private Date internalDate;

    // 屏蔽 EI_EXPOSE_REP（getter 暴露内部引用）
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getDate() {
        return this.internalDate;
    }
}


// ============================================================
// 2. 多规则屏蔽：@SuppressFBWarnings(value = {"rule1", "rule2"})
// ============================================================

class MultiRuleSuppression {

    // 屏蔽多个规则
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
    public MultiRuleSuppression(Date date) {
        this.internalDate = date;
    }

    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RV_RETURN_VALUE_IGNORED"})
    public Date getDateAndIgnore() {
        this.internalDate.getTime(); // 返回值被忽略
        return this.internalDate;
    }

    private Date internalDate;
}


// ============================================================
// 3. 带解释说明：@SuppressFBWarnings(value = "rule", justification = "explanation")
// ============================================================

class JustificationSuppression {

    // 带解释的屏蔽
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Date对象在此类中仅用于只读访问，不会被外部修改"
    )
    public JustificationSuppression(Date date) {
        this.internalDate = date;
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "调用方承诺不修改返回的Date对象，此设计为性能优化"
    )
    public Date getDate() {
        return this.internalDate;
    }

    private Date internalDate;
}


// ============================================================
// 4. 多规则 + 解释说明
// ============================================================

class MultiRuleWithJustification {

    @SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "Date对象在此类中是安全的，调用方不会修改"
    )
    public MultiRuleWithJustification(Date date) {
        this.internalDate = date;
    }

    @SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "RV_RETURN_VALUE_IGNORED"},
        justification = "返回Date引用是故意的，忽略getTime返回值不影响逻辑"
    )
    public Date getDate() {
        this.internalDate.getTime();
        return this.internalDate;
    }

    private Date internalDate;
}


// ============================================================
// 5. 精确匹配：@SuppressFBWarnings(value = "rule", justification = "explanation", matchType = SuppressMatchType.EXACT)
// ============================================================

class ExactMatchSuppression {

    // 精确匹配模式：只屏蔽完全匹配的规则
    // 默认情况下 SpotBugs 使用模糊匹配（BEST_MATCH），可能匹配到相近的规则
    // 使用 EXACT 可以确保只屏蔽指定的精确规则
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "精确匹配EI_EXPOSE_REP2，不会误屏蔽其他规则",
        matchType = SuppressMatchType.EXACT
    )
    public ExactMatchSuppression(Date date) {
        this.internalDate = date;
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "精确匹配EI_EXPOSE_REP，避免模糊匹配到其他EI规则",
        matchType = SuppressMatchType.EXACT
    )
    public Date getDate() {
        return this.internalDate;
    }

    private Date internalDate;
}


// ============================================================
// 6. 多规则 + 解释 + 精确匹配
// ============================================================

class FullFeaturedSuppression {

    @SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "Date对象在此类中是安全的，使用精确匹配避免误屏蔽",
        matchType = SuppressMatchType.EXACT
    )
    public FullFeaturedSuppression(Date date) {
        this.internalDate = date;
    }

    @SuppressFBWarnings(
        value = {"RV_RETURN_VALUE_IGNORED", "DM_NUMBER_CTOR"},
        justification = "忽略返回值是故意的，使用BigInteger.ONE代替构造器",
        matchType = SuppressMatchType.EXACT
    )
    public void method() {
        this.internalDate.getTime(); // RV_RETURN_VALUE_IGNORED
        BigInteger bi = new BigInteger("1"); // DM_NUMBER_CTOR
    }

    private Date internalDate;
}


// ============================================================
// 7. 类级别屏蔽
// ============================================================

@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
class ClassLevelSuppression {

    public ClassLevelSuppression(Date date) {
        this.internalDate = date;
    }

    // 类级别屏蔽对整个类生效
    public ClassLevelSuppression(Date date, String name) {
        this.internalDate = date;
        this.name = name;
    }

    private Date internalDate;
    private String name;
}


// ============================================================
// 8. 类级别全屏蔽（不推荐，仅作示例）
// ============================================================

@SuppressFBWarnings
class ClassLevelAllSuppression {

    // 整个类的所有 SpotBugs 警告都被屏蔽
    public ClassLevelAllSuppression(Date date) {
        this.internalDate = date;
    }

    public Date getDate() {
        return this.internalDate;
    }

    private Date internalDate;
}


// ============================================================
// 9. 方法级别屏蔽
// ============================================================

class MethodLevelSuppression {

    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
    public void ignoreReturnValue() {
        String s = "hello";
        s.toUpperCase(); // 返回值被忽略，不会触发 RV_RETURN_VALUE_IGNORED
    }

    @SuppressFBWarnings("DM_NUMBER_CTOR")
    public void useNumberConstructor() {
        Integer i = new Integer(42); // 不会触发 DM_NUMBER_CTOR
    }

    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK")
    public void redundantNullCheck(Object obj) {
        if (obj != null) { // 不会触发 RCN_REDUNDANT_NULLCHECK
            System.out.println(obj.toString());
        }
    }

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH", justification = "参数在调用前已校验非空")
    public void nullPath(String param) {
        System.out.println(param.length()); // 不会触发 NP_NULL_ON_SOME_PATH
    }
}


// ============================================================
// 10. 字段级别屏蔽
// ============================================================

class FieldLevelSuppression {

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    private int unreadField = 42; // 不会触发 URF_UNREAD_FIELD

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    private int unwrittenField; // 不会触发 UWF_UNWRITTEN_FIELD

    @SuppressFBWarnings(value = {"URF_UNREAD_FIELD", "UWF_UNWRITTEN_FIELD"}, justification = "通过反射访问")
    private int reflectionField; // 不会触发 URF_UNREAD_FIELD 和 UWF_UNWRITTEN_FIELD
}


// ============================================================
// 11. 构造器级别屏蔽
// ============================================================

class ConstructorSuppression {

    @SuppressFBWarnings("DM_NUMBER_CTOR")
    public ConstructorSuppression() {
        Integer i = new Integer(0); // 不会触发 DM_NUMBER_CTOR
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "数组引用在此类中是安全的"
    )
    public ConstructorSuppression(int[] data) {
        this.data = data;
    }

    private int[] data;
}


// ============================================================
// 12. 常见 SpotBugs 规则屏蔽示例
// ============================================================

class CommonSpotBugsRules {

    // --- EI_EXPOSE_REP2：构造器暴露内部可变引用 ---
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CommonSpotBugsRules(Date date) {
        this.date = date;
    }
    private Date date;

    // --- EI_EXPOSE_REP：getter 暴露内部可变引用 ---
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getDate() {
        return date;
    }

    // --- RV_RETURN_VALUE_IGNORED：忽略返回值 ---
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
    public void ignoreReturn() {
        "hello".toUpperCase(); // 返回值被忽略
    }

    // --- RV_RETURN_VALUE_IGNORED_BAD_PRACTICE：忽略重要返回值 ---
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
    public void ignoreImportantReturn() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value"); // 忽略 put 的返回值（旧值）
    }

    // --- DM_NUMBER_CTOR：使用包装类构造器而非valueOf ---
    @SuppressFBWarnings("DM_NUMBER_CTOR")
    public void numberConstructor() {
        Integer i = new Integer(42);
        Long l = new Long(42L);
        Double d = new Double(3.14);
    }

    // --- RCN_REDUNDANT_NULLCHECK：冗余的空检查 ---
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK")
    public void redundantNullCheck(String s) {
        if (s != null && s.length() > 0) { // s 已在调用前确保非空
            System.out.println(s);
        }
    }

    // --- NP_NULL_ON_SOME_PATH：可能为空的路径 ---
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public void nullPath() {
        String s = getString();
        System.out.println(s.length()); // s 可能为空
    }

    // --- NP_NULL_ON_SOME_PATH_EXCEPTION：异常路径上可能为空 ---
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_EXCEPTION")
    public void nullPathException() {
        String s = getString();
        System.out.println(s.length());
    }

    // --- NP_NONNULL_PARAM_VIOLATION：非空参数违反 ---
    @SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
    public void nonNullViolation() {
        String s = null;
        System.out.println(s.compareTo("other")); // 传空给非空参数
    }

    // --- NP_TOSTRING_COULD_RETURN_NULL：toString 可能返回空 ---
    @SuppressFBWarnings("NP_TOSTRING_COULD_RETURN_NULL")
    public void toStringNull() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return null; // toString 返回空
            }
        };
        String s = obj.toString();
        System.out.println(s.length());
    }

    // --- URF_UNREAD_FIELD：未读取的字段 ---
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    private int unreadField = 42;

    // --- UWF_UNWRITTEN_FIELD：未写入的字段 ---
    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    private int unwrittenField;

    // --- SS_SHOULD_BE_STATIC：应改为静态字段 ---
    @SuppressFBWarnings("SS_SHOULD_BE_STATIC")
    private int shouldBeStatic = 42; // 应该是 static final

    // --- DMI_INVOKING_TOSTRING_ON_ARRAY：对数组调用toString ---
    @SuppressFBWarnings("DMI_INVOKING_TOSTRING_ON_ARRAY")
    public void arrayToString() {
        int[] arr = {1, 2, 3};
        System.out.println(arr.toString()); // 对数组调用toString
    }

    // --- DMI_INVOKING_TOSTRING_ON_ANONYMOUS_ARRAY：对匿名数组调用toString ---
    @SuppressFBWarnings("DMI_INVOKING_TOSTRING_ON_ANONYMOUS_ARRAY")
    public void anonymousArrayToString() {
        System.out.println(new int[]{1, 2, 3}.toString());
    }

    // --- BC_VACUOUS_INSTANCEOF：无效的instanceof ---
    @SuppressFBWarnings("BC_VACUOUS_INSTANCEOF")
    public void vacuousInstanceof() {
        String s = "hello";
        if (s instanceof String) { // 始终为真
            System.out.println("always true");
        }
    }

    // --- BC_IMPOSSIBLE_INSTANCEOF：不可能的instanceof ---
    @SuppressFBWarnings("BC_IMPOSSIBLE_INSTANCEOF")
    public void impossibleInstanceof() {
        Object o = "hello";
        if (o instanceof Integer) { // 始终为假
            System.out.println("never");
        }
    }

    // --- RC_REF_COMPARISON：引用比较（应该用equals）---
    @SuppressFBWarnings("RC_REF_COMPARISON")
    public void refComparison() {
        Integer a = 128;
        Integer b = 128;
        if (a == b) { // 引用比较，应该用equals
            System.out.println("same");
        }
    }

    // --- EC_UNRELATED_TYPES：不相关类型比较 ---
    @SuppressFBWarnings("EC_UNRELATED_TYPES")
    public void unrelatedComparison() {
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        if (set.equals(list)) { // 不相关类型比较
            System.out.println("equal");
        }
    }

    // --- SE_BAD_FIELD：序列化类中的非序列化字段 ---
    @SuppressFBWarnings("SE_BAD_FIELD")
    static class SerializableWithBadField implements Serializable {
        private Thread nonSerializableField; // 非序列化字段
    }

    // --- SE_NO_SERIALVERSIONID：缺少 serialVersionUID ---
    @SuppressFBWarnings("SE_NO_SERIALVERSIONID")
    static class NoSerialVersionUID implements Serializable {
        private String name;
    }

    // --- SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE：非常量SQL字符串 ---
    @SuppressFBWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    public void sqlInjection(String userInput) throws SQLException {
        Connection conn = null;
        Statement stmt = conn.createStatement();
        stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");
    }

    // --- OBL_UNSATISFIED_OBLIGATION：未满足的清理义务 ---
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
    public void unsatisfiedObligation() throws IOException {
        InputStream is = new FileInputStream("test.txt");
        // 未关闭 InputStream
    }

    // --- OS_OPEN_STREAM：打开的流未关闭 ---
    @SuppressFBWarnings("OS_OPEN_STREAM")
    public void openStream() throws IOException {
        InputStream is = new FileInputStream("test.txt");
        // 未关闭 InputStream
    }

    // --- DLS_DEAD_LOCAL_STORE：无用的局部变量赋值 ---
    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    public void deadLocalStore() {
        int x = 42; // 赋值后未使用
    }

    // --- IL_INFINITE_LOOP：无限循环 ---
    @SuppressFBWarnings("IL_INFINITE_LOOP")
    public void infiniteLoop() {
        while (true) {
            // 无限循环
        }
    }

    // --- SWL_SLEEP_WITH_LOCK_HELD：持锁时调用sleep ---
    @SuppressFBWarnings("SWL_SLEEP_WITH_LOCK_HELD")
    public synchronized void sleepWithLock() throws InterruptedException {
        Thread.sleep(1000); // 持锁时sleep
    }

    // --- JLM_JSR166_UTILCONCURRENT_MONITORENTER：对并发类使用synchronized ---
    @SuppressFBWarnings("JLM_JSR166_UTILCONCURRENT_MONITORENTER")
    public void monitorOnConcurrent() {
        java.util.concurrent.ConcurrentHashMap<String, String> map = new java.util.concurrent.ConcurrentHashMap<>();
        synchronized (map) { // 对ConcurrentHashMap使用synchronized
            map.put("key", "value");
        }
    }

    private String getString() {
        return Math.random() > 0.5 ? "hello" : null;
    }
}


// ============================================================
// 13. 枚举中的屏蔽
// ============================================================

enum ColorSuppression {

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    RED("#FF0000"),
    GREEN("#00FF00"),
    BLUE("#0000FF");

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    private final String hexCode;

    ColorSuppression(String hexCode) {
        this.hexCode = hexCode;
    }
}


// ============================================================
// 14. 内部类中的屏蔽
// ============================================================

class OuterWithInner {

    @SuppressFBWarnings("SE_NO_SERIALVERSIONID")
    static class InnerSerializable implements Serializable {
        private String name;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    class InnerWithDate {
        private Date innerDate;
        public InnerWithDate(Date d) {
            this.innerDate = d;
        }
    }
}


// ============================================================
// 15. 接口实现中的屏蔽
// ============================================================

interface DataProcessor {
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    void process(String data);
}

class DataProcessorImpl implements DataProcessor {
    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public void process(String data) {
        System.out.println(data.length()); // data 可能为空
    }
}


// ============================================================
// 16. 抽象类中的屏蔽
// ============================================================

abstract class AbstractProcessor {

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public void process(String data) {
        System.out.println(data.length());
    }

    @SuppressFBWarnings(value = "RV_RETURN_VALUE_IGNORED", justification = "模板方法模式，子类决定是否使用返回值")
    public abstract int compute();
}


// ============================================================
// 17. 注解属性中的屏蔽
// ============================================================

@interface CustomAnnotation {
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    String value() default "";
}


// ============================================================
// 18. SuppressMatchType 对比示例
// ============================================================

class MatchTypeComparison {

    // 默认匹配（BEST_MATCH）：模糊匹配，可能匹配到相近的规则
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "默认模糊匹配，可能也屏蔽EI_EXPOSE_REP2"
    )
    public Date getDefaultMatch() {
        return this.date;
    }

    // 精确匹配（EXACT）：只屏蔽完全匹配的规则
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "精确匹配，只屏蔽EI_EXPOSE_REP，不会误屏蔽EI_EXPOSE_REP2",
        matchType = SuppressMatchType.EXACT
    )
    public Date getExactMatch() {
        return this.date;
    }

    private Date date = new Date();
}


// ============================================================
// 19. 正常代码（不屏蔽，会触发 SpotBugs 检测）
// ============================================================

class NoSuppression {

    public NoSuppression(Date date) {
        this.date = date; // 会触发 EI_EXPOSE_REP2
    }

    public Date getDate() {
        return date; // 会触发 EI_EXPOSE_REP
    }

    public void ignoreReturn() {
        "hello".toUpperCase(); // 会触发 RV_RETURN_VALUE_IGNORED
    }

    public void numberCtor() {
        Integer i = new Integer(42); // 会触发 DM_NUMBER_CTOR
    }

    private Date date;
}
