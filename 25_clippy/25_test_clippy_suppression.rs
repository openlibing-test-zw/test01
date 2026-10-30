// 测试 Clippy 屏蔽规则 - Rust 99
// Clippy 使用 #[allow(clippy::rule)] 和 #[expect(clippy::rule)] 属性来屏蔽检查
// allow：静默屏蔽指定警告
// expect：屏蔽指定警告，但如果该警告未出现则产生警告（确保屏蔽是必要的）

// ============================================================
// 0. 文件/模块级屏蔽：#![allow(clippy::rule)]
// ============================================================

// 注意：#![allow(...)] 应放在文件/模块顶部（crate 级别属性）
// 取消下面一行的注释即可屏蔽整个文件中指定规则：
// #![allow(clippy::all)]

// 屏蔽整个文件中指定规则：
// #![allow(clippy::unused_unit)]
// #![allow(clippy::redundant_clone)]

// 文件/模块级 expect：
// #![expect(clippy::unused_unit)]  // 如果文件中没有触发 unused_unit，会产生警告

// 屏蔽多个规则：
// #![allow(clippy::unused_unit, clippy::redundant_clone, clippy::map_identity)]


// ============================================================
// 1. 基本格式：#[allow(clippy::rule)]（放在 item 前）
// ============================================================

// 屏蔽函数的 clippy 警告
#[allow(clippy::unused_unit)]
fn returns_unit() -> () {
    // 返回 () 是冗余的，但被屏蔽
}

// 屏蔽结构体的 clippy 警告
#[allow(clippy::struct_excessive_bools)]
struct ManyBools {
    a: bool,
    b: bool,
    c: bool,
    d: bool,
    e: bool,
    f: bool,
    g: bool,
    h: bool,
}

// 屏蔽枚举的 clippy 警告
#[allow(clippy::enum_variant_names)]
enum ColorValues {
    ColorRed,
    ColorGreen,
    ColorBlue,
}

// 屏蔽常量的 clippy 警告
#[allow(clippy::unreadable_literal)]
const HEX_VALUE: u64 = 0xDEADBEEFCAFEBABE;

// 屏蔽静态变量的 clippy 警告
#[allow(clippy::unreadable_literal)]
static STATIC_HEX: u64 = 0xDEADBEEF;

// 屏蔽类型别名
#[allow(clippy::type_complexity)]
type ComplexType = fn(u32, u32, u32, u32, u32) -> Result<Vec<(String, i32)>, Box<dyn std::error::Error>>;

// 屏蔽 trait
#[allow(clippy::missing_safety_doc)]
unsafe trait UnsafeTrait {
    fn unsafe_method(&self);
}

// 屏蔽 impl 块
#[allow(clippy::unused_self)]
impl ManyBools {
    fn unused_self_method(&self) -> i32 {
        42 // self 未使用
    }
}


// ============================================================
// 2. 多规则屏蔽：#[allow(clippy::rule1, clippy::rule2)]
// ============================================================

#[allow(clippy::unused_unit, clippy::redundant_clone)]
fn multiple_rules() -> () {
    let s = String::from("hello");
    let _clone = s.clone(); // 冗余 clone
}

// 也可以省略 clippy:: 前缀（仅限 clippy 规则）
#[allow(unused_unit, redundant_clone)]
fn without_prefix() -> () {
    let s = String::from("hello");
    let _clone = s.clone();
}


// ============================================================
// 3. #[expect(clippy::rule)]（如果没触发该警告会产生警告）
// ============================================================

// expect 与 allow 类似，但如果被屏蔽的警告没有出现，会产生 "unused allow" 警告
// 这确保屏蔽注释是必要的，避免遗留无用的屏蔽

#[expect(clippy::unused_unit)]
fn expect_returns_unit() -> () {
    // 返回 () 是冗余的，被屏蔽
    // 如果移除 -> ()，clippy 不再触发 unused_unit，expect 会产生警告
}

#[expect(clippy::redundant_clone)]
fn expect_redundant_clone() {
    let s = String::from("hello");
    let _clone = s.clone(); // 冗余 clone，被屏蔽
}

// 多规则 expect
#[expect(clippy::unused_unit, clippy::redundant_clone)]
fn expect_multiple_rules() -> () {
    let s = String::from("hello");
    let _clone = s.clone();
}


// ============================================================
// 4. allow vs expect 对比
// ============================================================

// allow：静默屏蔽，即使警告不出现也不会有提示
#[allow(clippy::unused_unit)]
fn allow_example() {
    // 如果此处没有触发 unused_unit，allow 也不会产生任何提示
    // 可能导致遗留无用的屏蔽注释
}

// expect：如果警告不出现会产生警告，确保屏蔽是必要的
#[expect(clippy::unused_unit)]
fn expect_example() -> () {
    // 此处确实触发了 unused_unit，expect 正常工作
    // 如果移除 -> ()，expect 会产生 "unused allow" 警告
}


// ============================================================
// 5. 内联屏蔽（放在语句/表达式前）
// ============================================================

fn inline_suppression() {
    // 屏蔽单条语句
    #[allow(clippy::redundant_clone)]
    let s = String::from("hello");
    let _clone = s.clone();

    // 屏蔽单条表达式
    #[allow(clippy::unnecessary_cast)]
    let _x = 42i32 as i32; // 不必要的类型转换

    // expect 也可以内联
    #[expect(clippy::unnecessary_cast)]
    let _y = 42i32 as i32;
}


// ============================================================
// 6. 常见 Clippy 规则屏蔽示例
// ============================================================

// --- clippy::unused_unit：冗余的 () 返回类型 ---
#[allow(clippy::unused_unit)]
fn unused_unit() -> () {}

// --- clippy::redundant_clone：冗余的 clone ---
#[allow(clippy::redundant_clone)]
fn redundant_clone() {
    let s = String::from("hello");
    let _clone = s.clone();
}

// --- clippy::unnecessary_cast：不必要的类型转换 ---
#[allow(clippy::unnecessary_cast)]
fn unnecessary_cast() {
    let _x = 42i32 as i32;
}

// --- clippy::unreadable_literal：难以阅读的字面量 ---
#[allow(clippy::unreadable_literal)]
fn unreadable_literal() {
    let _hex = 0xDEADBEEF;
    let _big = 1234567890;
}

// --- clippy::enum_variant_names：枚举变体名重复 ---
#[allow(clippy::enum_variant_names)]
enum ColorVariant {
    ColorRed,
    ColorGreen,
    ColorBlue,
}

// --- clippy::struct_excessive_bools：结构体中布尔字段过多 ---
#[allow(clippy::struct_excessive_bools)]
struct ExcessiveBools {
    a: bool,
    b: bool,
    c: bool,
    d: bool,
    e: bool,
    f: bool,
    g: bool,
    h: bool,
}

// --- clippy::too_many_arguments：函数参数过多 ---
#[allow(clippy::too_many_arguments)]
fn too_many_args(a: i32, b: i32, c: i32, d: i32, e: i32, f: i32, g: i32, h: i32) -> i32 {
    a + b + c + d + e + f + g + h
}

// --- clippy::type_complexity：类型过于复杂 ---
#[allow(clippy::type_complexity)]
fn complex_type(x: Option<Result<Vec<(String, i32)>, Box<dyn std::error::Error>>>) -> bool {
    x.is_some()
}

// --- clippy::let_and_return：let 后直接 return ---
#[allow(clippy::let_and_return)]
fn let_and_return(x: i32) -> i32 {
    let y = x + 1;
    y
}

// --- clippy::needless_return：不必要的 return ---
#[allow(clippy::needless_return)]
fn needless_return(x: i32) -> i32 {
    return x;
}

// --- clippy::if_same_then_else：if/else 分支相同 ---
#[allow(clippy::if_same_then_else)]
fn if_same_then_else(x: i32) -> i32 {
    if x > 0 {
        1
    } else {
        1
    }
}

// --- clippy::bool_comparison：布尔值比较 ---
#[allow(clippy::bool_comparison)]
fn bool_comparison(flag: bool) -> bool {
    flag == true
}

// --- clippy::cmp_owned：比较拥有的值 ---
#[allow(clippy::cmp_owned)]
fn cmp_owned(s: String) -> bool {
    s == "hello".to_string()
}

// --- clippy::map_identity：map 恒等变换 ---
#[allow(clippy::map_identity)]
fn map_identity() {
    let v: Vec<i32> = vec![1, 2, 3];
    let _: Vec<i32> = v.iter().map(|x| *x).collect();
}

// --- clippy::clone_on_copy：对 Copy 类型调用 clone ---
#[allow(clippy::clone_on_copy)]
fn clone_on_copy() {
    let x = 42;
    let _y = x.clone();
}

// --- clippy::single_match：单分支 match 应改为 if let ---
#[allow(clippy::single_match)]
fn single_match(opt: Option<i32>) {
    match opt {
        Some(x) => println!("{}", x),
        _ => {}
    }
}

// --- clippy::single_match_else：单分支 match with else ---
#[allow(clippy::single_match_else)]
fn single_match_else(opt: Option<i32>) {
    match opt {
        Some(x) => println!("{}", x),
        _ => println!("none"),
    }
}

// --- clippy::match_ref_pats：match 中使用引用模式 ---
#[allow(clippy::match_ref_pats)]
fn match_ref_pats(x: &Option<i32>) {
    match x {
        &Some(ref v) => println!("{}", v),
        &None => println!("none"),
    }
}

// --- clippy::double_neg：双重否定 ---
#[allow(clippy::double_neg)]
fn double_neg(x: i32) -> i32 {
    --x
}

// --- clippy::modulo_one：对1取模 ---
#[allow(clippy::modulo_one)]
fn modulo_one(x: i32) -> i32 {
    x % 1
}

// --- clippy::zero_ptr：空指针字面量 ---
#[allow(clippy::zero_ptr)]
fn zero_ptr() -> *const i32 {
    0 as *const i32
}

// --- clippy::missing_safety_doc：缺少安全文档 ---
#[allow(clippy::missing_safety_doc)]
unsafe fn unsafe_without_doc() {
    // 缺少 Safety 文档
}

// --- clippy::new_without_default：有 new 但没实现 Default ---
#[allow(clippy::new_without_default)]
struct WithoutDefault {
    value: i32,
}

impl WithoutDefault {
    fn new() -> Self {
        Self { value: 0 }
    }
}

// --- clippy::new_ret_no_self：new 返回类型不是 Self ---
#[allow(clippy::new_ret_no_self)]
struct NewRetNoSelf;

impl NewRetNoSelf {
    fn new() -> Option<Self> {
        Some(Self)
    }
}

// --- clippy::should_implement_trait：应实现标准 trait ---
#[allow(clippy::should_implement_trait)]
struct MyAdder;

impl MyAdder {
    fn add(self, _other: Self) -> Self {
        Self
    }
}

// --- clippy::result_unit_err：Result<T, ()> 应改为 Option<T> ---
#[allow(clippy::result_unit_err)]
fn result_unit_err() -> Result<i32, ()> {
    Ok(42)
}

// --- clippy::ptr_arg：指针参数建议用引用 ---
#[allow(clippy::ptr_arg)]
fn ptr_arg(v: &Vec<i32>) -> i32 {
    v.len() as i32
}

// --- clippy::cast_possible_truncation：类型转换可能截断 ---
#[allow(clippy::cast_possible_truncation)]
fn cast_truncation(x: u64) -> u32 {
    x as u32
}

// --- clippy::cast_sign_loss：类型转换可能丢失符号 ---
#[allow(clippy::cast_sign_loss)]
fn cast_sign_loss(x: i32) -> u32 {
    x as u32
}

// --- clippy::cast_possible_wrap：类型转换可能回绕 ---
#[allow(clippy::cast_possible_wrap)]
fn cast_wrap(x: u32) -> i32 {
    x as i32
}

// --- clippy::integer_arithmetic：整数算术可能溢出 ---
#[allow(clippy::integer_arithmetic)]
fn integer_arithmetic(a: i32, b: i32) -> i32 {
    a + b
}

// --- clippy::indexing_slicing：索引切片可能越界 ---
#[allow(clippy::indexing_slicing)]
fn indexing_slicing(v: &[i32], i: usize) -> i32 {
    v[i]
}

// --- clippy::unwrap_used：使用 unwrap ---
#[allow(clippy::unwrap_used)]
fn unwrap_used(opt: Option<i32>) -> i32 {
    opt.unwrap()
}

// --- clippy::expect_used：使用 expect ---
#[allow(clippy::expect_used)]
fn expect_used(opt: Option<i32>) -> i32 {
    opt.expect("should have value")
}

// --- clippy::panic：使用 panic! ---
#[allow(clippy::panic)]
fn panic_used() {
    panic!("this should never happen");
}

// --- clippy::todo：使用 todo! ---
#[allow(clippy::todo)]
fn todo_used() {
    todo!("implement later");
}

// --- clippy::unimplemented：使用 unimplemented! ---
#[allow(clippy::unimplemented)]
fn unimplemented_used() {
    unimplemented!();
}


// ============================================================
// 7. 屏蔽所有 clippy 警告
// ============================================================

#[allow(clippy::all)]
fn all_clippy_suppressed() -> () {
    let s = String::from("hello");
    let _clone = s.clone();
    let _x = 42i32 as i32;
}

// 也可以使用 clippy::cargo_all、clippy::pedantic、clippy::restriction 等 lint 组
#[allow(clippy::pedantic)]
fn pedantic_suppressed() {
    // 所有 pedantic 级别的警告被屏蔽
}

#[allow(clippy::restriction)]
fn restriction_suppressed() {
    // 所有 restriction 级别的警告被屏蔽
}

#[allow(clippy::nursery)]
fn nursery_suppressed() {
    // 所有 nursery 级别的警告被屏蔽
}


// ============================================================
// 8. warn/deny/forbid 级别（与 allow 对应）
// ============================================================

// allow：允许（屏蔽）
// warn：警告（默认级别）
// deny：拒绝（编译错误）
// forbid：禁止（不可被 allow 覆盖）

#[warn(clippy::unused_unit)]
fn warn_example() -> () {}

#[deny(clippy::unused_unit)]
fn deny_example() -> () {}

// forbid 不可被低优先级的 allow 覆盖
// #[forbid(clippy::unused_unit)]
// fn forbid_example() -> () {}


// ============================================================
// 9. cfg_attr 条件编译中的屏蔽
// ============================================================

#[cfg_attr(debug_assertions, allow(clippy::unwrap_used))]
fn conditional_allow(opt: Option<i32>) -> i32 {
    // 仅在 debug_assertions 启用时屏蔽 unwrap_used
    opt.unwrap()
}

#[cfg_attr(test, allow(clippy::unwrap_used))]
fn test_conditional_allow(opt: Option<i32>) -> i32 {
    // 仅在测试模式下屏蔽 unwrap_used
    opt.unwrap()
}


// ============================================================
// 10. impl 块中的屏蔽
// ============================================================

#[allow(clippy::unused_self)]
impl ManyBools {
    fn method_with_unused_self(&self) -> i32 {
        42
    }

    #[allow(clippy::redundant_clone)]
    fn method_with_redundant_clone(&self) -> String {
        let s = String::from("hello");
        s.clone()
    }
}

#[expect(clippy::unused_self)]
impl ManyBools {
    fn expect_unused_self(&self) -> i32 {
        42
    }
}


// ============================================================
// 11. trait 实现中的屏蔽
// ============================================================

trait MyTrait {
    fn my_method(&self, _x: i32) -> i32;
}

#[allow(clippy::unused_self, clippy::unused_variables)]
impl MyTrait for ManyBools {
    fn my_method(&self, x: i32) -> i32 {
        42 // self 和 x 都未使用
    }
}


// ============================================================
// 12. 枚举中的屏蔽
// ============================================================

#[allow(clippy::enum_variant_names)]
enum PrefixEnum {
    PrefixA,
    PrefixB,
    PrefixC,
}

#[expect(clippy::enum_variant_names)]
enum ExpectPrefixEnum {
    ExpectA,
    ExpectB,
    ExpectC,
}


// ============================================================
// 13. 模块中的屏蔽
// ============================================================

#[allow(clippy::unused_unit)]
mod suppressed_module {
    pub fn module_fn() -> () {}
}

#[expect(clippy::unused_unit)]
mod expect_module {
    pub fn module_fn() -> () {}
}


// ============================================================
// 14. 嵌套/优先级规则
// ============================================================

// 外层 allow，内层可以 deny/forbid 覆盖
#[allow(clippy::unused_unit)]
fn outer_allow() {
    // 此处 unused_unit 被屏蔽

    #[deny(clippy::unused_unit)]
    fn inner_deny() -> () {
        // 此处 unused_unit 会被拒绝（编译错误）
    }
}

// forbid 不可被 allow 覆盖
// #[forbid(clippy::unused_unit)]
// fn forbid_example() {
//     #[allow(clippy::unused_unit)]  // 错误！forbid 不可被 allow 覆盖
//     fn inner_allow() -> () {}
// }


// ============================================================
// 15. 正常代码（不屏蔽，会触发 Clippy 检测）
// ============================================================

// 以下代码会被 Clippy 检测到问题
fn unformatted_return() -> () {
    // 会触发 clippy::unused_unit
}

fn redundant_clone_unsuppressed() {
    let s = String::from("hello");
    let _clone = s.clone(); // 会触发 clippy::redundant_clone
}

fn main() {
    println!("clippy suppression test - Rust");
}
