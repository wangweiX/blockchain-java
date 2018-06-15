package one.wangwei.blockchain.script;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * 交易脚本的操作符、常量和符号
 * <p>
 * <a href="https://en.bitcoin.it/wiki/Script">
 *
 * @author wangwei
 * @date 2018/06/14
 */
public class ScriptOpCodes {

    /**
     * =========================================
     * 入栈操作符
     * =========================================
     */
    /**
     * 一个字节空串被压入堆栈中
     **/
    public static final int OP_0 = 0x00;
    public static final int OP_FALSE = OP_0;

    /**
     * 下一个字节包含要推入堆栈的字节数。
     **/
    public static final int OP_PUSHDATA1 = 0x4c;
    /**
     * 接下来的两个字节包含按小端顺序推入堆栈的字节数。
     **/
    public static final int OP_PUSHDATA2 = 0x4d;
    /**
     * 接下来的四个字节包含按小端顺序推入堆栈的字节数。
     **/
    public static final int OP_PUSHDATA4 = 0x4e;
    /**
     * 将脚本-1压入堆栈
     **/
    public static final int OP_1NEGATE = 0x4f;
    /**
     * 终止 - 交易无效（除非在未执行的 OP_IF 语句中）
     **/
    public static final int OP_RESERVED = 0x50;
    /**
     * 将脚本1压入堆栈
     **/
    public static final int OP_1 = 0x51;
    public static final int OP_TRUE = OP_1;
    /**
     * 将脚本2压入堆栈
     **/
    public static final int OP_2 = 0x52;
    /**
     * 将脚本3压入堆栈
     **/
    public static final int OP_3 = 0x53;
    /**
     * 将脚本4压入堆栈
     **/
    public static final int OP_4 = 0x54;
    /**
     * 将脚本5压入堆栈
     **/
    public static final int OP_5 = 0x55;
    /**
     * 将脚本6压入堆栈
     **/
    public static final int OP_6 = 0x56;
    /**
     * 将脚本7压入堆栈
     **/
    public static final int OP_7 = 0x57;
    /**
     * 将脚本8压入堆栈
     **/
    public static final int OP_8 = 0x58;
    /**
     * 将脚本9压入堆栈
     **/
    public static final int OP_9 = 0x59;
    /**
     * 将脚本10压入堆栈
     **/
    public static final int OP_10 = 0x5a;
    /**
     * 将脚本11压入堆栈
     **/
    public static final int OP_11 = 0x5b;
    /**
     * 将脚本12压入堆栈
     **/
    public static final int OP_12 = 0x5c;
    /**
     * 将脚本13压入堆栈
     **/
    public static final int OP_13 = 0x5d;
    /**
     * 将脚本14压入堆栈
     **/
    public static final int OP_14 = 0x5e;
    /**
     * 将脚本15压入堆栈
     **/
    public static final int OP_15 = 0x5f;
    /**
     * 将脚本16压入堆栈
     **/
    public static final int OP_16 = 0x60;

    /**
     * =========================================
     * 有条件的流量控制操作
     * =========================================
     */
    /**
     * 无操作
     **/
    public static final int OP_NOP = 0x61;
    /**
     * 终止 - 交易无效（除非在未执行的 OP_IF 语句中）
     **/
    public static final int OP_VER = 0x62;
    /**
     * 如果栈项元素值为0，语句将被执行
     **/
    public static final int OP_IF = 0x63;
    /**
     * 如果栈项元素值不为0，语句将被执行
     **/
    public static final int OP_NOTIF = 0x64;
    /**
     * 终止 - 交易无效
     **/
    public static final int OP_VERIF = 0x65;
    /**
     * 终止 - 交易无效
     **/
    public static final int OP_VERNOTIF = 0x66;
    /**
     * 如果前述的OP_IF或OP_NOTIF或OP_ELSE未被执行，这些语句就会被执行
     **/
    public static final int OP_ELSE = 0x67;
    /**
     * 终止 OP_IF, OP_NOTIF, OP_ELSE 区块
     **/
    public static final int OP_ENDIF = 0x68;
    /**
     * 如果栈项元素值非真，则标记交易无效
     **/
    public static final int OP_VERIFY = 0x69;
    /**
     * 标记交易无效
     **/
    public static final int OP_RETURN = 0x6a;

    /**
     * =========================================
     * 控制堆栈的操作符
     * =========================================
     */
    /**
     * 从主堆栈中取出元素，推入辅堆栈。
     **/
    public static final int OP_TOALTSTACK = 0x6b;
    /**
     * 从辅堆栈中取出元素，推入主堆栈
     **/
    public static final int OP_FROMALTSTACK = 0x6c;
    /**
     * 删除栈顶两个元素
     **/
    public static final int OP_2DROP = 0x6d;
    /**
     * 复制栈顶两个元素
     **/
    public static final int OP_2DUP = 0x6e;
    /**
     * 复制栈顶三个元素
     **/
    public static final int OP_3DUP = 0x6f;
    /**
     * 把栈底的第三、第四个元素拷贝到栈顶
     **/
    public static final int OP_2OVER = 0x70;
    /**
     * 移动第五、第六元素到栈顶
     **/
    public static final int OP_2ROT = 0x71;
    /**
     * 交换最上面两个元素的位置
     **/
    public static final int OP_2SWAP = 0x72;
    /**
     * 如果栈项元素值不为0，复制该元素值
     **/
    public static final int OP_IFDUP = 0x73;
    /**
     * 把堆栈元素的个数压入堆栈
     **/
    public static final int OP_DEPTH = 0x74;
    /**
     * 删除栈顶元素
     **/
    public static final int OP_DROP = 0x75;
    /**
     * 复制栈顶元素
     **/
    public static final int OP_DUP = 0x76;
    /**
     * 删除栈顶的下一个元素
     **/
    public static final int OP_NIP = 0x77;
    /**
     * 复制栈顶的下一个元素到栈顶
     **/
    public static final int OP_OVER = 0x78;
    /**
     * 把堆栈的第n个元素拷贝到栈顶
     **/
    public static final int OP_PICK = 0x79;
    /**
     * 把堆栈的第n个元素移动到栈顶
     **/
    public static final int OP_ROLL = 0x7a;
    /**
     * 翻转栈顶的三个元素
     **/
    public static final int OP_ROT = 0x7b;
    /**
     * 栈顶的三个元素交换
     **/
    public static final int OP_SWAP = 0x7c;
    /**
     * 拷贝栈顶元素并插入到栈顶第二个元素之后
     **/
    public static final int OP_TUCK = 0x7d;

    /**
     * =========================================
     * 字符串操作符
     * =========================================
     */
    /**
     * 连接两个字符串，已禁用
     **/
    public static final int OP_CAT = 0x7e;
    /**
     * 返回字符串的一部分，已禁用
     **/
    public static final int OP_SUBSTR = 0x7f;
    /**
     * 在一个字符串中保留左边指定长度的子串，已禁用
     **/
    public static final int OP_LEFT = 0x80;
    /**
     * 在一个字符串中保留右边指定长度的子串，已禁用
     **/
    public static final int OP_RIGHT = 0x81;
    /**
     * 把栈顶元素的字符串长度压入堆栈
     **/
    public static final int OP_SIZE = 0x82;

    /**
     * =========================================
     * 二进制算术和条件操作符
     * =========================================
     */
    /**
     * 所有输入的位取反，已禁用
     **/
    public static final int OP_INVERT = 0x83;
    /**
     * 对输入的所有位进行布尔与运算，已禁用
     **/
    public static final int OP_AND = 0x84;
    /**
     * 对输入的每一位进行布尔或运算，已禁用
     **/
    public static final int OP_OR = 0x85;
    /**
     * 对输入的每一位进行布尔异或运算，已禁用
     **/
    public static final int OP_XOR = 0x86;
    /**
     * 如果输入的两个数相等，返回1，否则返回0
     **/
    public static final int OP_EQUAL = 0x87;
    /**
     * 与OP_EQUAL一样，如结果为0，之后运行OP_VERIFY
     **/
    public static final int OP_EQUALVERIFY = 0x88;
    /**
     * 终止-无效交易（除非在未执行的OP_IF语句中）
     **/
    public static final int OP_RESERVED1 = 0x89;
    /**
     * 终止-无效交易（除非在未执行的OP_IF语句中）
     **/
    public static final int OP_RESERVED2 = 0x8a;

    /**
     * =========================================
     * 数值操作符
     * =========================================
     */
    /**
     * 输入值加1
     **/
    public static final int OP_1ADD = 0x8b;
    /**
     * 输入值减1
     **/
    public static final int OP_1SUB = 0x8c;
    /**
     * 无效（输入值乘2）
     **/
    public static final int OP_2MUL = 0x8d;
    /**
     * 无效 （输入值除2）
     **/
    public static final int OP_2DIV = 0x8e;
    /**
     * 输入值符号取反
     **/
    public static final int OP_NEGATE = 0x8f;
    /**
     * 输入值符号取正
     **/
    public static final int OP_ABS = 0x90;
    /**
     * 如果输入值为0或1，则输出1或0；否则输出0
     **/
    public static final int OP_NOT = 0x91;
    /**
     * 输入值为0输出0；否则输出1
     **/
    public static final int OP_0NOTEQUAL = 0x92;
    /**
     * 输出输入两项之和
     **/
    public static final int OP_ADD = 0x93;
    /**
     * 输出输入（第二项减去第一项）之差
     **/
    public static final int OP_SUB = 0x94;
    /**
     * 禁用（输出输入两项的积）
     **/
    public static final int OP_MUL = 0x95;
    /**
     * 禁用（输出用第二项除以第一项的倍数）
     **/
    public static final int OP_DIV = 0x96;
    /**
     * 禁用（输出用第二项除以第一项得到的余数）
     **/
    public static final int OP_MOD = 0x97;
    /**
     * 禁用（左移第二项，移动位数为第一项的字节数）
     **/
    public static final int OP_LSHIFT = 0x98;
    /**
     * 禁用（右移第二项，移动位数为第一项的字节数）
     **/
    public static final int OP_RSHIFT = 0x99;
    /**
     * 两项都不会为0，输出1，否则输出0
     **/
    public static final int OP_BOOLAND = 0x9a;
    /**
     * 两项有一个不为0，输出1，否则输出0
     **/
    public static final int OP_BOOLOR = 0x9b;
    /**
     * 两项相等则输出1，否则输出为0
     **/
    public static final int OP_NUMEQUAL = 0x9c;
    /**
     * 和 NUMEQUAL 相同，如结果为0运行OP_VERIFY
     **/
    public static final int OP_NUMEQUALVERIFY = 0x9d;
    /**
     * 如果栈顶两项不是相等数的话，则输出1
     **/
    public static final int OP_NUMNOTEQUAL = 0x9e;
    /**
     * 如果第二项小于栈顶项，则输出1
     **/
    public static final int OP_LESSTHAN = 0x9f;
    /**
     * 如果第二项大于栈顶项，则输出1
     **/
    public static final int OP_GREATERTHAN = 0xa0;
    /**
     * 如果第二项小于或等于第一项，则输出1
     **/
    public static final int OP_LESSTHANOREQUAL = 0xa1;
    /**
     * 如果第二项大于或等于第一项，则输出1
     **/
    public static final int OP_GREATERTHANOREQUAL = 0xa2;
    /**
     * 输出栈顶两项中较小的一项
     **/
    public static final int OP_MIN = 0xa3;
    /**
     * 输出栈顶两项中较大的一项
     **/
    public static final int OP_MAX = 0xa4;
    /**
     * 如果第三项的数值介于前两项之间，则输出1
     **/
    public static final int OP_WITHIN = 0xa5;

    /**
     * =========================================
     * 加密和散列操作符
     * =========================================
     */
    /**
     * 返回栈顶项的 RIPEMD160 哈希值
     **/
    public static final int OP_RIPEMD160 = 0xa6;
    /**
     * 返回栈顶项 SHA1 哈希值
     **/
    public static final int OP_SHA1 = 0xa7;
    /**
     * 返回栈顶项 SHA256 哈希值
     **/
    public static final int OP_SHA256 = 0xa8;
    /**
     * 栈顶项进行两次HASH，先用SHA-256，再用RIPEMD-160
     **/
    public static final int OP_HASH160 = 0xa9;
    /**
     * 栈顶项用SHA-256算法HASH两次
     **/
    public static final int OP_HASH256 = 0xaa;
    /**
     * 标记已进行签名验证的数据
     **/
    public static final int OP_CODESEPARATOR = 0xab;
    /**
     * 交易所用的签名必须是哈希值和公钥的有效签名，如果为真，则返回1
     **/
    public static final int OP_CHECKSIG = 0xac;
    /**
     * 与CHECKSIG一样，但之后运行OP_VERIFY
     **/
    public static final int OP_CHECKSIGVERIFY = 0xad;
    /**
     * 对于每对签名和公钥运行CHECKSIG。所有的签名要与公钥匹配。因为存在BUG，一个未使用的外部值会从堆栈中删除。
     **/
    public static final int OP_CHECKMULTISIG = 0xae;
    /**
     * 与 CHECKMULTISIG 一样，但之后运行OP_VERIFY
     **/
    public static final int OP_CHECKMULTISIGVERIFY = 0xaf;

    // block state
    /**
     * 检查块的锁定时间。 在BIP-65中引入，取代OP_NOP2
     */
    public static final int OP_CHECKLOCKTIMEVERIFY = 0xb1;

    // expansion
    public static final int OP_NOP1 = 0xb0;
    /**
     * Deprecated by BIP 65
     */
    @Deprecated
    public static final int OP_NOP2 = OP_CHECKLOCKTIMEVERIFY;
    public static final int OP_NOP3 = 0xb2;
    public static final int OP_NOP4 = 0xb3;
    public static final int OP_NOP5 = 0xb4;
    public static final int OP_NOP6 = 0xb5;
    public static final int OP_NOP7 = 0xb6;
    public static final int OP_NOP8 = 0xb7;
    public static final int OP_NOP9 = 0xb8;
    public static final int OP_NOP10 = 0xb9;
    public static final int OP_INVALIDOPCODE = 0xff;

    private static final Map<Integer, String> opCodeMap = ImmutableMap.<Integer, String>builder()
            .put(OP_0, "0")
            .put(OP_PUSHDATA1, "PUSHDATA1")
            .put(OP_PUSHDATA2, "PUSHDATA2")
            .put(OP_PUSHDATA4, "PUSHDATA4")
            .put(OP_1NEGATE, "1NEGATE")
            .put(OP_RESERVED, "RESERVED")
            .put(OP_1, "1")
            .put(OP_2, "2")
            .put(OP_3, "3")
            .put(OP_4, "4")
            .put(OP_5, "5")
            .put(OP_6, "6")
            .put(OP_7, "7")
            .put(OP_8, "8")
            .put(OP_9, "9")
            .put(OP_10, "10")
            .put(OP_11, "11")
            .put(OP_12, "12")
            .put(OP_13, "13")
            .put(OP_14, "14")
            .put(OP_15, "15")
            .put(OP_16, "16")
            .put(OP_NOP, "NOP")
            .put(OP_VER, "VER")
            .put(OP_IF, "IF")
            .put(OP_NOTIF, "NOTIF")
            .put(OP_VERIF, "VERIF")
            .put(OP_VERNOTIF, "VERNOTIF")
            .put(OP_ELSE, "ELSE")
            .put(OP_ENDIF, "ENDIF")
            .put(OP_VERIFY, "VERIFY")
            .put(OP_RETURN, "RETURN")
            .put(OP_TOALTSTACK, "TOALTSTACK")
            .put(OP_FROMALTSTACK, "FROMALTSTACK")
            .put(OP_2DROP, "2DROP")
            .put(OP_2DUP, "2DUP")
            .put(OP_3DUP, "3DUP")
            .put(OP_2OVER, "2OVER")
            .put(OP_2ROT, "2ROT")
            .put(OP_2SWAP, "2SWAP")
            .put(OP_IFDUP, "IFDUP")
            .put(OP_DEPTH, "DEPTH")
            .put(OP_DROP, "DROP")
            .put(OP_DUP, "DUP")
            .put(OP_NIP, "NIP")
            .put(OP_OVER, "OVER")
            .put(OP_PICK, "PICK")
            .put(OP_ROLL, "ROLL")
            .put(OP_ROT, "ROT")
            .put(OP_SWAP, "SWAP")
            .put(OP_TUCK, "TUCK")
            .put(OP_CAT, "CAT")
            .put(OP_SUBSTR, "SUBSTR")
            .put(OP_LEFT, "LEFT")
            .put(OP_RIGHT, "RIGHT")
            .put(OP_SIZE, "SIZE")
            .put(OP_INVERT, "INVERT")
            .put(OP_AND, "AND")
            .put(OP_OR, "OR")
            .put(OP_XOR, "XOR")
            .put(OP_EQUAL, "EQUAL")
            .put(OP_EQUALVERIFY, "EQUALVERIFY")
            .put(OP_RESERVED1, "RESERVED1")
            .put(OP_RESERVED2, "RESERVED2")
            .put(OP_1ADD, "1ADD")
            .put(OP_1SUB, "1SUB")
            .put(OP_2MUL, "2MUL")
            .put(OP_2DIV, "2DIV")
            .put(OP_NEGATE, "NEGATE")
            .put(OP_ABS, "ABS")
            .put(OP_NOT, "NOT")
            .put(OP_0NOTEQUAL, "0NOTEQUAL")
            .put(OP_ADD, "ADD")
            .put(OP_SUB, "SUB")
            .put(OP_MUL, "MUL")
            .put(OP_DIV, "DIV")
            .put(OP_MOD, "MOD")
            .put(OP_LSHIFT, "LSHIFT")
            .put(OP_RSHIFT, "RSHIFT")
            .put(OP_BOOLAND, "BOOLAND")
            .put(OP_BOOLOR, "BOOLOR")
            .put(OP_NUMEQUAL, "NUMEQUAL")
            .put(OP_NUMEQUALVERIFY, "NUMEQUALVERIFY")
            .put(OP_NUMNOTEQUAL, "NUMNOTEQUAL")
            .put(OP_LESSTHAN, "LESSTHAN")
            .put(OP_GREATERTHAN, "GREATERTHAN")
            .put(OP_LESSTHANOREQUAL, "LESSTHANOREQUAL")
            .put(OP_GREATERTHANOREQUAL, "GREATERTHANOREQUAL")
            .put(OP_MIN, "MIN")
            .put(OP_MAX, "MAX")
            .put(OP_WITHIN, "WITHIN")
            .put(OP_RIPEMD160, "RIPEMD160")
            .put(OP_SHA1, "SHA1")
            .put(OP_SHA256, "SHA256")
            .put(OP_HASH160, "HASH160")
            .put(OP_HASH256, "HASH256")
            .put(OP_CODESEPARATOR, "CODESEPARATOR")
            .put(OP_CHECKSIG, "CHECKSIG")
            .put(OP_CHECKSIGVERIFY, "CHECKSIGVERIFY")
            .put(OP_CHECKMULTISIG, "CHECKMULTISIG")
            .put(OP_CHECKMULTISIGVERIFY, "CHECKMULTISIGVERIFY")
            .put(OP_NOP1, "NOP1")
            .put(OP_CHECKLOCKTIMEVERIFY, "CHECKLOCKTIMEVERIFY")
            .put(OP_NOP3, "NOP3")
            .put(OP_NOP4, "NOP4")
            .put(OP_NOP5, "NOP5")
            .put(OP_NOP6, "NOP6")
            .put(OP_NOP7, "NOP7")
            .put(OP_NOP8, "NOP8")
            .put(OP_NOP9, "NOP9")
            .put(OP_NOP10, "NOP10").build();

    private static final Map<String, Integer> opCodeNameMap = ImmutableMap.<String, Integer>builder()
            .put("0", OP_0)
            .put("PUSHDATA1", OP_PUSHDATA1)
            .put("PUSHDATA2", OP_PUSHDATA2)
            .put("PUSHDATA4", OP_PUSHDATA4)
            .put("1NEGATE", OP_1NEGATE)
            .put("RESERVED", OP_RESERVED)
            .put("1", OP_1)
            .put("2", OP_2)
            .put("3", OP_3)
            .put("4", OP_4)
            .put("5", OP_5)
            .put("6", OP_6)
            .put("7", OP_7)
            .put("8", OP_8)
            .put("9", OP_9)
            .put("10", OP_10)
            .put("11", OP_11)
            .put("12", OP_12)
            .put("13", OP_13)
            .put("14", OP_14)
            .put("15", OP_15)
            .put("16", OP_16)
            .put("NOP", OP_NOP)
            .put("VER", OP_VER)
            .put("IF", OP_IF)
            .put("NOTIF", OP_NOTIF)
            .put("VERIF", OP_VERIF)
            .put("VERNOTIF", OP_VERNOTIF)
            .put("ELSE", OP_ELSE)
            .put("ENDIF", OP_ENDIF)
            .put("VERIFY", OP_VERIFY)
            .put("RETURN", OP_RETURN)
            .put("TOALTSTACK", OP_TOALTSTACK)
            .put("FROMALTSTACK", OP_FROMALTSTACK)
            .put("2DROP", OP_2DROP)
            .put("2DUP", OP_2DUP)
            .put("3DUP", OP_3DUP)
            .put("2OVER", OP_2OVER)
            .put("2ROT", OP_2ROT)
            .put("2SWAP", OP_2SWAP)
            .put("IFDUP", OP_IFDUP)
            .put("DEPTH", OP_DEPTH)
            .put("DROP", OP_DROP)
            .put("DUP", OP_DUP)
            .put("NIP", OP_NIP)
            .put("OVER", OP_OVER)
            .put("PICK", OP_PICK)
            .put("ROLL", OP_ROLL)
            .put("ROT", OP_ROT)
            .put("SWAP", OP_SWAP)
            .put("TUCK", OP_TUCK)
            .put("CAT", OP_CAT)
            .put("SUBSTR", OP_SUBSTR)
            .put("LEFT", OP_LEFT)
            .put("RIGHT", OP_RIGHT)
            .put("SIZE", OP_SIZE)
            .put("INVERT", OP_INVERT)
            .put("AND", OP_AND)
            .put("OR", OP_OR)
            .put("XOR", OP_XOR)
            .put("EQUAL", OP_EQUAL)
            .put("EQUALVERIFY", OP_EQUALVERIFY)
            .put("RESERVED1", OP_RESERVED1)
            .put("RESERVED2", OP_RESERVED2)
            .put("1ADD", OP_1ADD)
            .put("1SUB", OP_1SUB)
            .put("2MUL", OP_2MUL)
            .put("2DIV", OP_2DIV)
            .put("NEGATE", OP_NEGATE)
            .put("ABS", OP_ABS)
            .put("NOT", OP_NOT)
            .put("0NOTEQUAL", OP_0NOTEQUAL)
            .put("ADD", OP_ADD)
            .put("SUB", OP_SUB)
            .put("MUL", OP_MUL)
            .put("DIV", OP_DIV)
            .put("MOD", OP_MOD)
            .put("LSHIFT", OP_LSHIFT)
            .put("RSHIFT", OP_RSHIFT)
            .put("BOOLAND", OP_BOOLAND)
            .put("BOOLOR", OP_BOOLOR)
            .put("NUMEQUAL", OP_NUMEQUAL)
            .put("NUMEQUALVERIFY", OP_NUMEQUALVERIFY)
            .put("NUMNOTEQUAL", OP_NUMNOTEQUAL)
            .put("LESSTHAN", OP_LESSTHAN)
            .put("GREATERTHAN", OP_GREATERTHAN)
            .put("LESSTHANOREQUAL", OP_LESSTHANOREQUAL)
            .put("GREATERTHANOREQUAL", OP_GREATERTHANOREQUAL)
            .put("MIN", OP_MIN)
            .put("MAX", OP_MAX)
            .put("WITHIN", OP_WITHIN)
            .put("RIPEMD160", OP_RIPEMD160)
            .put("SHA1", OP_SHA1)
            .put("SHA256", OP_SHA256)
            .put("HASH160", OP_HASH160)
            .put("HASH256", OP_HASH256)
            .put("CODESEPARATOR", OP_CODESEPARATOR)
            .put("CHECKSIG", OP_CHECKSIG)
            .put("CHECKSIGVERIFY", OP_CHECKSIGVERIFY)
            .put("CHECKMULTISIG", OP_CHECKMULTISIG)
            .put("CHECKMULTISIGVERIFY", OP_CHECKMULTISIGVERIFY)
            .put("NOP1", OP_NOP1)
            .put("CHECKLOCKTIMEVERIFY", OP_CHECKLOCKTIMEVERIFY)
            .put("NOP2", OP_NOP2)
            .put("NOP3", OP_NOP3)
            .put("NOP4", OP_NOP4)
            .put("NOP5", OP_NOP5)
            .put("NOP6", OP_NOP6)
            .put("NOP7", OP_NOP7)
            .put("NOP8", OP_NOP8)
            .put("NOP9", OP_NOP9)
            .put("NOP10", OP_NOP10).build();

    /**
     * 将操作码转为字符串
     *
     * @param opcode 操作码
     * @return
     */
    public static String getOpCodeName(int opcode) {
        if (opCodeMap.containsKey(opcode)) {
            return opCodeMap.get(opcode);
        }
        return "NON_OP(" + opcode + ")";
    }

    /**
     * 将入栈操作符转为字符串
     *
     * @param opcode
     * @return
     */
    public static String getPushDataName(int opcode) {
        if (opCodeMap.containsKey(opcode)) {
            return opCodeMap.get(opcode);
        }
        return "PUSHDATA(" + opcode + ")";
    }

    /**
     * 将操作码名称转为编号
     *
     * @param opCodeName
     * @return
     */
    public static int getOpCode(String opCodeName) {
        if (opCodeNameMap.containsKey(opCodeName)) {
            return opCodeNameMap.get(opCodeName);
        }
        return OP_INVALIDOPCODE;
    }
}
