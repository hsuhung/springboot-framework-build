package com.springboot.framework.build.example.utils.valid;

/**
 * 银行卡卡号校验
 * Luhm 校验算法获得校验位(模10算法)
 * 1.从卡号的最后一位数字开始，逆向将奇数位（1，3，5 等等相加）
 * 2.从卡号最后一位数字开始，逆向将偶数位数字，先乘以2，如果乘积为两位数，将个位数字相加，即将其减去9，再求和。
 * 3.将奇数位总和加上偶数位总和
 * 4.用10减去所得结果整除的余数，即为卡号最后一位
 */
public class BankcardValidUtils {

    /**
     * 银行卡卡号校验
     * @param cardNo
     * @return true:合法
     */
    public static boolean isValidatedBankcard(String cardNo){

        char bit = getBankcardCheckCode(cardNo.substring(0, cardNo.length() - 1));
        if(bit == 'N'){
            return false;
        }

        return cardNo.charAt(cardNo.length() - 1) == bit;
    }


    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位(模10算法)
     * @param nonCheckCodeCardId
     * @return
     */
    private static char getBankcardCheckCode(String nonCheckCodeCardId){

        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0){
            return 'N';
        }

        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }

        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

}
