// 测试 rustfmt 屏蔽规则 - Rust
// rustfmt 使用 #[rustfmt::skip] 等属性来屏蔽代码格式化
// 特点：rustfmt 是代码格式化工具，屏蔽属性用于跳过格式化检查

// ============================================================
// 0. 文件/模块级屏蔽：#![rustfmt::skip]
// ============================================================

// 注意：#![rustfmt::skip] 应放在文件/模块顶部（crate 级别属性）
// 取消下面一行的注释即可跳过整个文件的格式化：
// #![rustfmt::skip]

// 跳过整个文件/模块指定宏的格式化：
// #![rustfmt::skip::macros(println, eprintln, format)]

// 跳过整个文件/模块指定属性的格式化：
// #![rustfmt::skip::attributes(derive, cfg)]


// ============================================================
// 1. 基本格式：#[rustfmt::skip]（跳过下一行 item 的格式化）
// ============================================================

// 跳过函数的格式化
#[rustfmt::skip]
fn badly_formatted_function(  x:i32,  y:i32  )->i32{x+y}

// 跳过结构体的格式化
#[rustfmt::skip]
struct BadlyFormattedStruct{x:i32,y:i32,z:String}

// 跳过枚举的格式化
#[rustfmt::skip]
enum BadlyFormattedEnum{Variant1,Variant2,Variant3}

// 跳过常量的格式化
#[rustfmt::skip]
const BADLY_FORMATTED_CONST:i32=42;

// 跳过静态变量的格式化
#[rustfmt::skip]
static BADLY_FORMATTED_STATIC:&str="hello";

// 跳过类型别名的格式化
#[rustfmt::skip]
type BadlyFormattedType=Vec<i32>;

// 跳过 trait 的格式化
#[rustfmt::skip]
trait BadlyFormattedTrait{fn method(&self)->i32;}

// 跳过 impl 块的格式化
#[rustfmt::skip]
impl BadlyFormattedTrait for i32{fn method(&self)->i32{*self}}

// 正常格式化的代码（对比）
fn normally_formatted_function(x: i32, y: i32) -> i32 {
    x + y
}

struct NormallyFormattedStruct {
    x: i32,
    y: i32,
    z: String,
}

enum NormallyFormattedEnum {
    Variant1,
    Variant2,
    Variant3,
}


// ============================================================
// 2. #[cfg_attr(any(), rustfmt::skip)]（条件编译风格跳过格式化）
// ============================================================

// cfg_attr 风格：通过条件编译属性间接应用 rustfmt::skip
// any() 始终为真，因此 rustfmt::skip 始终生效
#[cfg_attr(any(), rustfmt::skip)]
fn cfg_skipped_function(  x:i32,  y:i32  )->i32{x+y}

#[cfg_attr(any(), rustfmt::skip)]
struct CfgSkippedStruct{x:i32,y:i32}

#[cfg_attr(any(), rustfmt::skip)]
enum CfgSkippedEnum{A,B,C}

// 也可以使用特定条件
#[cfg_attr(feature = "custom-format", rustfmt::skip)]
fn conditional_skip_function(x: i32, y: i32) -> i32 {
    x + y // 当 feature = "custom-format" 时跳过格式化
}


// ============================================================
// 3. #[rustfmt::skip::macros(...)]（跳过该 item 指定宏的格式化）
// ============================================================

// 跳过函数中指定宏的格式化（println, format 等）
#[rustfmt::skip::macros(println, format)]
fn function_with_macro_skip() {
    // println! 宏调用不会被格式化
    println!("This is a very long string that would normally be reformatted by rustfmt but is skipped because of the macro skip attribute: {}",  "value");
    // format! 宏调用不会被格式化
    let _s = format!("Another long string with {} and {} that would normally be reformatted", "arg1",  "arg2");
    // 其他宏正常格式化
    vec![1, 2, 3, 4, 5];
}

// 跳过结构体中指定宏的格式化
#[rustfmt::skip::macros(vec)]
struct StructWithMacroSkip {
    data: Vec<i32>,
}

impl StructWithMacroSkip {
    fn new() -> Self {
        Self {
            // vec! 宏调用不会被格式化
            data: vec![1,2,3,4,5,6,7,8,9,10],
        }
    }
}

// 跳过多个宏
#[rustfmt::skip::macros(println, eprintln, format, vec)]
fn function_with_multiple_macro_skip() {
    println!("long line with {} and {} and {}",  "a",  "b",  "c");
    eprintln!("error: {} {} {}",  "x",  "y",  "z");
    let _s = format!("formatted: {} {} {}",  1,  2,  3);
    let _v = vec![1,2,3,4,5,6,7,8,9,10];
}

// 跳过自定义宏
macro_rules! custom_debug_macro {
    ($($arg:tt)*) => {
        println!($($arg)*)
    };
}

#[rustfmt::skip::macros(custom_debug_macro)]
fn function_with_custom_macro_skip() {
    custom_debug_macro!("This is a very long custom macro invocation that would normally be reformatted by rustfmt but is skipped: {}","value");
}


// ============================================================
// 4. #[rustfmt::skip::attributes(...)]（跳过该 item 指定属性的格式化）
// ============================================================

// 跳过指定属性（如 derive）的格式化
#[rustfmt::skip::attributes(derive)]
#[derive(Debug,Clone,Copy,PartialEq,Eq,Hash,Default)]
struct StructWithAttributeSkip {
    x: i32,
    y: i32,
}

// 跳过 cfg 属性的格式化
#[rustfmt::skip::attributes(cfg)]
#[cfg(all(unix, target_arch = "x86_64", feature = "multi-thread"))]
fn function_with_cfg_skip() {
    println!("unix x86_64 multi-thread");
}

// 跳过多个属性
#[rustfmt::skip::attributes(derive, cfg)]
#[derive(Debug,Clone,Copy)]
#[cfg(all(feature="feature1",feature="feature2"))]
struct StructWithMultiAttributeSkip {
    value: i32,
}

// 跳过 inline 属性的格式化
#[rustfmt::skip::attributes(inline)]
#[inline(always)]
fn function_with_inline_skip() -> i32 {
    42
}

// 跳过 allow/warn/deny/forbid 属性的格式化
#[rustfmt::skip::attributes(allow)]
#[allow(unused_variables,dead_code,non_snake_case)]
fn function_with_allow_skip() {
    let UnusedVar = 42;
}


// ============================================================
// 5. impl 块中的屏蔽
// ============================================================

#[rustfmt::skip]
impl BadlyFormattedStruct {
    fn new(x:i32,y:i32,z:String)->Self{Self{x,y,z}}
    fn get_x(&self)->i32{self.x}
    fn get_y(&self)->i32{self.y}
    fn get_z(&self)->&str{&self.z}
}

// 跳过 impl 中特定宏的格式化
#[rustfmt::skip::macros(println)]
impl NormallyFormattedStruct {
    fn debug_print(&self) {
        println!("Struct: x={}, y={}, z={}",  self.x,  self.y,  self.z);
    }
}


// ============================================================
// 6. trait 定义中的屏蔽
// ============================================================

#[rustfmt::skip]
trait BadlyFormattedTrait2 {
    fn method1(&self)->i32;
    fn method2(&self,x:i32)->i32;
    fn method3(&self,x:i32,y:i32)->i32;
}

#[rustfmt::skip::macros(println)]
trait DebugTrait {
    fn debug(&self) {
        println!("Debug: {} {} {}",  "a",  "b",  "c");
    }
}


// ============================================================
// 7. 枚举变体中的屏蔽
// ============================================================

#[rustfmt::skip]
enum DetailedEnum {
    Variant1{i32,i32,i32},
    Variant2(String,i32,Vec<i32>),
    Variant3{x:i32,y:i32,z:i32},
}

#[rustfmt::skip::macros(vec)]
enum EnumWithMacroSkip {
    Simple,
    WithData(Vec<i32>),
    WithMacro { data: Vec<i32> },
}

impl EnumWithMacroSkip {
    fn new() -> Self {
        Self::WithMacro {
            data: vec![1,2,3,4,5],
        }
    }
}


// ============================================================
// 8. 模块中的屏蔽
// ============================================================

#[rustfmt::skip]
mod badly_formatted_module {
    pub fn function_in_module(  x:i32  )->i32{x*2}
    pub struct ModuleStruct{pub x:i32,pub y:i32}
}


// ============================================================
// 9. 文件/模块级屏蔽（详细示例）
// ============================================================

// 以下展示文件/模块级屏蔽的完整用法
// 注意：这些属性应放在文件顶部，此处仅作示例

// 跳过整个文件的格式化
// #![rustfmt::skip]

// 跳过整个文件中指定宏的格式化
// #![rustfmt::skip::macros(println, eprintln, format, vec, write)]

// 跳过整个文件中指定属性的格式化
// #![rustfmt::skip::attributes(derive, cfg, allow, inline)]

// 组合使用：跳过文件格式化 + 指定宏 + 指定属性
// #![rustfmt::skip]
// #![rustfmt::skip::macros(println)]
// #![rustfmt::skip::attributes(derive)]


// ============================================================
// 10. 常见使用场景
// ============================================================

// 场景1：保持对齐的表格数据
#[rustfmt::skip]
const LOOKUP_TABLE: [(i32, &str); 5] = [
    (1,  "one"),
    (2,  "two"),
    (3,  "three"),
    (4,  "four"),
    (5,  "five"),
];

// 场景2：保持位运算对齐
#[rustfmt::skip]
const FLAGS: u32 = 0b0000_0001
                 | 0b0000_0010
                 | 0b0000_0100
                 | 0b0000_1000;

// 场景3：保持长字符串原样（不自动换行）
#[rustfmt::skip::macros(println)]
fn long_sql_query() {
    println!("SELECT u.id, u.name, u.email, u.created_at FROM users u WHERE u.status = 'active' AND u.created_at > '2024-01-01' ORDER BY u.name ASC LIMIT 100");
}

// 场景4：保持生成的代码原样
#[rustfmt::skip]
// AUTO-GENERATED CODE - DO NOT MODIFY
mod generated {
    pub fn gen_func_1()->i32{1}
    pub fn gen_func_2()->i32{2}
    pub fn gen_func_3()->i32{3}
    pub struct GenStruct{pub x:i32,pub y:i32}
}

// 场景5：保持兼容性的序列化格式
#[rustfmt::skip]
fn to_legacy_format(x: i32, y: i32) -> String {
    format!("{};{};",x,y)
}

// 场景6：保持测试数据对齐
#[rustfmt::skip]
fn test_cases() -> Vec<(i32, i32, i32)> {
    vec![
        (1,  2,  3),
        (4,  5,  6),
        (7,  8,  9),
        (10, 11, 12),
    ]
}


// ============================================================
// 11. 组合使用示例
// ============================================================

// 同时跳过宏和属性的格式化
#[rustfmt::skip::macros(println, format)]
#[rustfmt::skip::attributes(derive, cfg)]
#[derive(Debug,Clone,Copy)]
#[cfg(all(feature="feature1",feature="feature2"))]
struct CombinedSkipStruct {
    value: i32,
}

impl CombinedSkipStruct {
    fn new(value: i32) -> Self {
        println!("Creating CombinedSkipStruct with value: {}",  value);
        Self { value }
    }
}


// ============================================================
// 12. 正常代码（不屏蔽，会被 rustfmt 格式化）
// ============================================================

// 以下代码会被 rustfmt 格式化修正
fn unformatted_function(  x:i32,  y:i32  )->i32{x+y}

struct UnformattedStruct{x:i32,y:i32,z:String}

enum UnformattedEnum{A,B,C}

const UNFORMATTED_CONST:i32=42;

fn main() {
    println!("rustfmt suppression test - Rust");
}
