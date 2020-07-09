package com.springboot.framework.build.example.utils.valid;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 身份证号码校验
 * 大陆身份证号码验证
 * 15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * 18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 */
public class IDCardValidUtils {

    // 每位加权因子
    private static int[] POWER = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    private enum IDCardEnum{
        /**
         *  省，直辖市代码表
         */
        北京(11), 天津(12), 河北(13), 山西(14), 内蒙古(15), 辽宁(21), 吉林(22), 黑龙江(23), 上海(31), 江苏(32), 浙江(33), 安徽(34), 福建(35), 江西(36),
        山东(37), 河南(41), 湖北(42), 湖南(43), 广东(44), 广西(45), 海南(46), 重庆(50), 四川(51), 贵州(52), 云南(53), 西藏(54), 陕西(61), 甘肃(62),
        青海(63), 宁夏(64), 新疆(65), 台湾(71), 香港(81), 澳门(82),;

        private int code;

        IDCardEnum(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


    /**
     * 身份证号码校验
     * @param idCard
     * @return true:合法
     */
    public static boolean isValidatedIDcard(String idCard){

        // 如果是15位身份证，先转成18位
        if(idCard.length() == 15){
            idCard = convertIdcarBy15bit(idCard);
        }

        return isValidate18Idcard(idCard);
    }

    /**
     * 判断18位身份证的合法性
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     *
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * 1.前1、2位数字表示：所在省份的代码；
     * 2.第3、4位数字表示：所在城市的代码；
     * 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日；
     * 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     *
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * @param idCard
     * @return
     */
    private static boolean isValidate18Idcard(String idCard){
        // 非18位为假
        if (StringUtils.isBlank(idCard) || idCard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idCard17 = idCard.substring(0, 17);

        // 获取第18位
        String lastNumber = idCard.substring(17, 18).toLowerCase();

        // 是否都为数字
        if (!idCard17.matches("^[0-9]*$")) {
            return false;
        }

        char[] c = idCard17.toCharArray();
        // 将字符数组转为整型数组
        int[] bit = converCharToInt(c);
        // 将身份证的每位和对应位的加权因子相乘之后，再得到和值
        int sum17 = getPowerSum(bit);

        // 获取和值与11取模得到余数进行校验
        String checkCode = getCheckCodeBySum(sum17);
        // 将身份证的第18位与算出来的校码进行匹配，不相等就是非法身份证
        if (null == checkCode || !lastNumber.equalsIgnoreCase(checkCode)) {
            return false;
        }

        return true;
    }


    /**
     * 15位身份证转成18位
     * @param idCard
     * @return
     */
    private static String convertIdcarBy15bit(String idCard){
        String idCard17 = null;
        // 非15位身份证
        if(StringUtils.isBlank(idCard) || idCard.length() != 15){
            return null;
        }
        if(idCard.matches("^[0-9]*$")){
            //获取出生日期
            String birthday = idCard.substring(6, 12);
            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("yyyMMdd").parse(birthday);
            } catch (ParseException e) {
                System.out.println("--- 身份证出生日期转换失败 ---");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthdate);
            String year = String.valueOf(calendar.get(Calendar.YEAR));

            idCard17 = idCard.substring(0, 6) + year + idCard.substring(8);

            char[] c = idCard17.toCharArray();

            //将字符数组转为整数数组
            int[] bit = converCharToInt(c);
            int sum17 = getPowerSum(bit);

            // 获取和值与11取模得到余数进行校验
            String checkCode = getCheckCodeBySum(sum17);
            // 获取不到校验位
            if(checkCode == null){
                return null;
            }
            // 将前17位与第18位校验码拼接
            idCard17 += checkCode;
        }else {
            // 包含非数字
            return null;
        }

        return idCard17;
    }

    /**
     * 将字符数组转为整型数组
     * @param c
     * @return
     */
    private static int[] converCharToInt(char[] c){

        int[] bit = new int[c.length];
        int k = 0;
        for (char temp: c){
            bit[k++] = Integer.parseInt(String.valueOf(temp));
        }

        return bit;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     * @param bit
     * @return
     */
    private static int getPowerSum(int[] bit){
        int sum = 0;
        if(POWER.length != bit.length){
            return sum;
        }
        for(int i = 0; i < bit.length; i++){
            // sum += bit[i] * POWER[i];等效下面循环
            for(int j = 0; j < POWER.length; j++){
                if(i == j){
                    sum += bit[i] * POWER[j];
                }
            }
        }

        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     * @param sum17
     * @return
     */
    private static String getCheckCodeBySum(int sum17){

        String checkCode = null;
        switch (sum17 % 11){
            case 10:
                checkCode = "2"; break;
            case 9:
                checkCode = "3"; break;
            case 8:
                checkCode = "4"; break;
            case 7:
                checkCode = "5"; break;
            case 6:
                checkCode = "6"; break;
            case 5:
                checkCode = "7"; break;
            case 4:
                checkCode = "8"; break;
            case 3:
                checkCode = "9"; break;
            case 2:
                checkCode = "x"; break;
            case 1:
                checkCode = "0"; break;
            case 0:
                checkCode = "1"; break;
        }

        return checkCode;
    }

}
