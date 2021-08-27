
public class Operand {
    static char sign = '+';
    static long operInt;
    static float operFloat;
    static long resultInt;
    static float resultFloat;

    public char getSign() {
        return sign;
    }
    public void setSign(char sign) {
        this.sign = sign;
    }
    public long getOper() {
        return operInt;
    }
    public void addDigit(long oper) {
        this.operInt = this.operInt * 10 + oper;
    }
    public void minusDigit() {
        this.operInt = this.operInt / 10;
    }
    public void resetoper() {
        operInt = 0;
        sign = '+';
    }
    public static long getResultInt() {
        return resultInt;
    }
    public static float getResultFloat() {
        return resultFloat;
    }
    public void plusFunc() {
        resultInt += operInt;
    }
    public void minusFunc() {
        resultInt -= operInt;
    }
    public void multFunc() {
        resultInt *= operInt;
    }
    public void divFunc() {
        resultInt /= operInt;
    }
    public void apply() {
        if (sign == '+') {
            plusFunc();
        } else if (sign == '-') {
            minusFunc();
        } else if (sign == '*') {
            multFunc();
        } else if (sign == '/') {
            divFunc();
        }
    }
    public void fullReset() {
        resetoper();
        resultInt = 0;
        resultFloat = 0;
    }
    public void changeSign() {
        operInt = (-1) * operInt;
    }
    public static void setOperInt(long operInt) {
        Operand.operInt = operInt;
    }
}
