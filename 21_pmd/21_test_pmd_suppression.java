// 测试 PMD 屏蔽规则 - Java
// PMD 使用 @SuppressWarnings("PMD") 注解和 // NOPMD 注释来屏蔽检查
// 注解：@SuppressWarnings("PMD")（屏蔽所有）或 @SuppressWarnings("PMD.rule")（屏蔽指定规则）
// 注释：// NOPMD - explanation（本行屏蔽，NOPMD 字符串可通过 CLI 参数 -suppressmarker 自定义）

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.BigInteger;

// ============================================================
// 1. 注解屏蔽：@SuppressWarnings("PMD")（屏蔽类/方法中所有 PMD 警告）
// ============================================================
public class PMDSuppressionTest {

    // ============================================================
    // 1.1 @SuppressWarnings("PMD") 作用于类级别
    // ============================================================

    // 以下所有成员都不会触发 PMD 警告（因为类级别屏蔽了所有 PMD 规则）
    private int x = 11;
    private int y = 2;

    public void methodWithIssues() {
        int unused = 42; // 不会触发 UnusedLocalVariable
        if (true) { // 不会触发 UnnecessaryIfStatement
            System.out.println("always true");
        }
    }
}


// ============================================================
// 2. 注解屏蔽：@SuppressWarnings("PMD.rule")（屏蔽指定规则）
// ============================================================
@SuppressWarnings("PMD.ShortVariable")
class A { // 不会触发 ShortClassName
    // 只屏蔽了 ShortClassName，其他规则仍然生效
}


@SuppressWarnings("PMD.ShortVariable")
class ShortVarClass {
    // 只屏蔽 ShortVariable
    @SuppressWarnings("PMD.ShortVariable")
    private int a = 12; // 不会触发 ShortVariable

    private int b = 2; // 会触发 ShortVariable（没有注解）
}


// ============================================================
// 3. 注解屏蔽：@SuppressWarnings("PMD.rule1", "PMD.rule2")（多个规则）
// ============================================================

@SuppressWarnings({"PMD.ShortVariable", "PMD.ShortClassName"})
class B {
    private int c = 3; // 不会触发 ShortVariable
}


@SuppressWarnings("PMD.ShortVariable")
@SuppressWarnings("PMD.ShortClassName")
class C {
    private int d = 4; // 不会触发 ShortVariable（多个 @SuppressWarnings）
}


// ============================================================
// 4. 注解屏蔽：只识别含 "PMD" 的注解值（避免误识别 Java 原生注解）
// ============================================================

@SuppressWarnings("unchecked") // Java 原生注解，PMD 不会识别为屏蔽
class NativeAnnotationClass {
    @SuppressWarnings("unchecked")
    public List getRawList() {
        return new ArrayList();
    }
}

@SuppressWarnings("PMD.UnnecessaryCast")
class PmdAnnotationClass {
    @SuppressWarnings("PMD.UnnecessaryCast")
    public String getString() {
        return (String) "hello"; // PMD 不会报告 UnnecessaryCast
    }
}

// 混合使用：Java 原生 + PMD
@SuppressWarnings({"unchecked", "PMD.UnnecessaryCast"})
class MixedAnnotationClass {
    @SuppressWarnings({"unchecked", "PMD.UnnecessaryCast"})
    public String getMixed() {
        return (String) "hello";
    }
}


// ============================================================
// 5. 注解屏蔽：方法级别
// ============================================================

class MethodLevelSuppression {

    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void unusedLocal() {
        int unused = 42; // 不会触发 UnusedLocalVariable
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public void unusedParam(String unusedParam) { // 不会触发 UnusedFormalParameter
        System.out.println("method called");
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) { // 不会触发 EmptyCatchBlock
            // 空 catch 块
        }
    }

    @SuppressWarnings("PMD.UnusedLocalVariable, PMD.ShortVariable")
    public void multipleRules() {
        int a = 1; // 不会触发 UnusedLocalVariable 和 ShortVariable
    }
}


// ============================================================
// 6. 注解屏蔽：字段级别
// ============================================================

class FieldLevelSuppression {

    @SuppressWarnings("PMD.ShortVariable")
    private int x = 1; // 不会触发 ShortVariable

    @SuppressWarnings("PMD.UnusedPrivateField")
    private int unusedField = 42; // 不会触发 UnusedPrivateField

    @SuppressWarnings("PMD.ShortVariable, PMD.MagicNumber")
    private int y = 100; // 不会触发 ShortVariable 和 MagicNumber
}


// ============================================================
// 7. 注解屏蔽：构造器级别
// ============================================================

class ConstructorSuppression {

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public ConstructorSuppression(String unusedParam) { // 不会触发 UnusedFormalParameter
    }

    @SuppressWarnings("PMD.ShortVariable")
    public ConstructorSuppression(int a) { // 不会触发 ShortVariable
    }
}


// ============================================================
// 8. 注解屏蔽：枚举级别
// ============================================================

@SuppressWarnings("PMD.ShortClassName")
enum E { // 不会触发 ShortClassName
    @SuppressWarnings("PMD.ShortVariable")
    A, // 不会触发 ShortVariable
    B,
    C
}


// ============================================================
// 9. 注解屏蔽：内部类级别
// ============================================================

class OuterClass {

    @SuppressWarnings("PMD.ShortClassName")
    class InnerA { // 不会触发 ShortClassName
    }

    @SuppressWarnings("PMD.ShortVariable")
    class InnerB {
        private int a = 1; // 不会触发 ShortVariable
    }
}


// ============================================================
// 10. 注释屏蔽：// NOPMD（本行屏蔽所有 PMD 规则）
// ============================================================

class NopmdSuppression {

    private int a = 1; // NOPMD - 短变量名，已知问题

    public void method() {
        int unused = 42; // NOPMD - 临时变量
    }

    public void emptyIf() {
        if (true) { // NOPMD - 条件始终为真
            System.out.println("always true");
        }
    }
}


// ============================================================
// 11. 注释屏蔽：// NOPMD - explanation（带解释说明）
// ============================================================

class NopmdWithExplanation {

    private int x = 1; // NOPMD - 短变量名用于数学计算，符合惯例

    public void complexLogic() {
        int a = 1; // NOPMD - 临时计数器变量
        int b = 2; // NOPMD - 临时计数器变量
        System.out.println(a + b);
    }

    public void emptyLoop() {
        for (int i = 0; i < 10; i++) { // NOPMD - 故意为空的循环，用于等待
        }
    }
}


// ============================================================
// 12. 注释屏蔽：// NOPMD 空格变体
// ============================================================

class NopmdVariants {

    private int a = 1; //NOPMD - 无空格
    private int b = 2; //  NOPMD - 多空格
    private int c = 3; //NOPMD
    private int d = 4; // NOPMD
}


// ============================================================
// 13. NOPMD 字符串可通过 CLI 参数自定义
// ============================================================

// PMD CLI 支持通过 -suppressmarker 参数自定义屏蔽标记
// 默认为 NOPMD，可以改为其他字符串
// 例如：pmd -suppressmarker CUSTOMMARKER ...
// 此时 // CUSTOMMARKER 将被识别为屏蔽标记
// 以下示例展示自定义标记（需要在 CLI 中配置才能生效）

class CustomMarker {
    // 假设 CLI 配置了 -suppressmarker SUPPRESSME
    private int x = 1; // SUPPRESSME - 自定义屏蔽标记（需 CLI 配置）
    private int y = 2; // NOPMD - 默认标记（如果 CLI 改了标记，这个将不再生效）
}


// ============================================================
// 14. 常见 PMD 规则屏蔽示例
// ============================================================

class CommonPmdRules {

    // --- ShortVariable（变量名过短）---
    @SuppressWarnings("PMD.ShortVariable")
    public void shortVar() {
        int a = 1; // 不会触发 ShortVariable
        int b = 2; // 不会触发 ShortVariable
    }

    // --- LongVariable（变量名过长）---
    @SuppressWarnings("PMD.LongVariable")
    public void longVar() {
        int thisIsAVeryLongVariableNameThatExceedsTheDefaultLimit = 1; // 不会触发 LongVariable
    }

    // --- ShortMethodName（方法名过短）---
    @SuppressWarnings("PMD.ShortMethodName")
    public void go() { // 不会触发 ShortMethodName
    }

    // --- ShortClassName（类名过短）---
    // 已在上方示例中展示

    // --- UnusedLocalVariable（未使用的局部变量）---
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void unusedLocal() {
        int unused = 42; // 不会触发 UnusedLocalVariable
    }

    // --- UnusedFormalParameter（未使用的方法参数）---
    @SuppressWarnings("PMD.UnusedFormalParameter")
    public void unusedParam(String param) { // 不会触发 UnusedFormalParameter
    }

    // --- UnusedPrivateField（未使用的私有字段）---
    @SuppressWarnings("PMD.UnusedPrivateField")
    private int unusedField = 42; // 不会触发 UnusedPrivateField

    // --- UnusedPrivateMethod（未使用的私有方法）---
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void unusedMethod() { // 不会触发 UnusedPrivateMethod
    }

    // --- EmptyCatchBlock（空 catch 块）---
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) { // 不会触发 EmptyCatchBlock
        }
    }

    // --- EmptyIfStmt（空 if 语句）---
    @SuppressWarnings("PMD.EmptyIfStmt")
    public void emptyIf() {
        int x = 1;
        if (x == 1) { // 不会触发 EmptyIfStmt
        }
    }

    // --- EmptyWhileStmt（空 while 语句）---
    @SuppressWarnings("PMD.EmptyWhileStmt")
    public void emptyWhile() {
        while (true) { // NOPMD - 故意为空
        }
    }

    // --- EmptyMethodInAbstractClassShouldBeAbstract（抽象类空方法应为抽象方法）---
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    abstract class AbstractBase {
        public void emptyMethod() { // 不会触发该规则
        }
    }

    // --- AvoidInstantiatingObjectsInLoops（循环中实例化对象）---
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void instantiateInLoop() {
        for (int i = 0; i < 10; i++) {
            String s = new String("hello"); // 不会触发 AvoidInstantiatingObjectsInLoops
        }
    }

    // --- AvoidLiteralsInIfCondition（if 条件中使用字面量）---
    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public void literalInIf() {
        int x = 10;
        if (x == 10) { // 不会触发 AvoidLiteralsInIfCondition
            System.out.println("ten");
        }
    }

    // --- DataflowAnomalyAnalysis（数据流异常分析）---
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void dataflowAnomaly() {
        int x; // UR（未定义引用）异常
        x = 42;
        System.out.println(x);
    }

    // --- DefaultPackage（默认包访问）---
    @SuppressWarnings("PMD.DefaultPackage")
    int defaultAccessField = 1; // 不会触发 DefaultPackage

    // --- LawOfDemeter（迪米特法则）---
    @SuppressWarnings("PMD.LawOfDemeter")
    public void demeterViolation() {
        String result = "hello".toUpperCase().substring(0, 3).concat("!"); // 不会触发 LawOfDemeter
    }

    // --- TooManyMethods（方法过多）---
    @SuppressWarnings("PMD.TooManyMethods")
    public void method1() {}
    public void method2() {}
    public void method3() {}
    public void method4() {}
    public void method5() {}
    public void method6() {}
    public void method7() {}
    public void method8() {}
    public void method9() {}
    public void method10() {}

    // --- CyclomaticComplexity（圈复杂度过高）---
    @SuppressWarnings("PMD.CyclomaticComplexity")
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

    // --- NcssCount（NCSS 计数过高）---
    @SuppressWarnings("PMD.NcssCount")
    public void longMethod() {
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
        System.out.println("line 11");
        System.out.println("line 12");
        System.out.println("line 13");
        System.out.println("line 14");
        System.out.println("line 15");
    }

    // --- AvoidCatchingGenericException（捕获泛型异常）---
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void catchGeneric() {
        try {
            riskyOperation();
        } catch (Exception e) { // 不会触发 AvoidCatchingGenericException
            e.printStackTrace();
        }
    }

    // --- AvoidThrowingRawExceptionTypes（抛出原始异常类型）---
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public void throwRaw() throws Exception { // 不会触发 AvoidThrowingRawExceptionTypes
        throw new Exception("raw exception");
    }

    // --- SystemPrintln（使用 System.out.println）---
    @SuppressWarnings("PMD.SystemPrintln")
    public void usePrintln() {
        System.out.println("debug output"); // 不会触发 SystemPrintln
    }

    // --- AvoidDollarSigns（变量名中使用美元符号）---
    @SuppressWarnings("PMD.AvoidDollarSigns")
    private int $dollarVar = 1; // 不会触发 AvoidDollarSigns

    // --- AvoidUsingNativeCode（使用 native 方法）---
    @SuppressWarnings("PMD.AvoidUsingNativeCode")
    public native void nativeMethod(); // 不会触发 AvoidUsingNativeCode

    // --- BeanMembersShouldSerialize（Bean 成员应可序列化）---
    @SuppressWarnings("PMD.BeanMembersShouldSerialize")
    private Thread nonSerializable; // 不会触发 BeanMembersShouldSerialize

    // --- CallSuperInConstructor（构造器中调用 super）---
    @SuppressWarnings("PMD.CallSuperInConstructor")
    class NoSuperConstructor {
        public NoSuperConstructor() { // 不会触发 CallSuperInConstructor
        }
    }

    // --- OnlyOneReturn（方法只有一个 return）---
    @SuppressWarnings("PMD.OnlyOneReturn")
    public int multipleReturns(int x) {
        if (x > 0) return 1; // 不会触发 OnlyOneReturn
        return 0;
    }

    // --- ConfusingTernary（令人困惑的三元运算）---
    @SuppressWarnings("PMD.ConfusingTernary")
    public String confusingTernary(int x) {
        if (x == 0) return "zero"; // 不会触发 ConfusingTernary
        return "non-zero";
    }

    // --- AvoidLosingOverridingInEnum（枚举中丢失覆盖）---
    // 此规则较特殊，此处仅作注释说明

    // --- SingularField（单字段类）---
    @SuppressWarnings("PMD.SingularField")
    private int singleField = 1; // 不会触发 SingularField

    // --- ImmutableField（不可变字段）---
    @SuppressWarnings("PMD.ImmutableField")
    private int mutableField; // 不会触发 ImmutableField

    private void riskyOperation() {
        // 模拟有风险的操作
    }
}


// ============================================================
// 15. 注解与 NOPMD 混合使用
// ============================================================

class MixedSuppression {

    @SuppressWarnings("PMD.ShortVariable")
    private int a = 1; // 注解屏蔽 ShortVariable

    private int b = 2; // NOPMD - 注释屏蔽所有规则

    @SuppressWarnings("PMD.ShortVariable")
    public void method() {
        int c = 3; // 注解屏蔽 ShortVariable
        int d = 4; // NOPMD - 注释屏蔽所有规则
    }
}


// ============================================================
// 16. 继承中的屏蔽
// ============================================================

class BaseClass {
    @SuppressWarnings("PMD.UnusedFormalParameter")
    public void baseMethod(String unused) { // 不会触发 UnusedFormalParameter
    }
}

class DerivedClass extends BaseClass {
    @SuppressWarnings("PMD.UnusedFormalParameter")
    @Override
    public void baseMethod(String unused) { // 子类也需要单独屏蔽
    }
}


// ============================================================
// 17. 接口中的屏蔽
// ============================================================

@SuppressWarnings("PMD.ShortClassName")
interface I { // 不会触发 ShortClassName
    @SuppressWarnings("PMD.ShortVariable")
    void method(int a); // 不会触发 ShortVariable
}


// ============================================================
// 18. Lambda 中的屏蔽
// ============================================================

class LambdaSuppression {
    public void lambdaExample() {
        @SuppressWarnings("PMD.ShortVariable")
        java.util.function.Function<Integer, Integer> f = x -> x + 1; // 不会触发 ShortVariable
    }
}


// ============================================================
// 19. 正常代码（不屏蔽，会触发 PMD 检测）
// ============================================================

class NoSuppression {
    private int a = 1; // 会触发 ShortVariable

    public void unusedLocal() {
        int unused = 42; // 会触发 UnusedLocalVariable
    }

    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) { // 会触发 EmptyCatchBlock
        }
    }

    public void systemPrintln() {
        System.out.println("debug"); // 会触发 SystemPrintln
    }
}
