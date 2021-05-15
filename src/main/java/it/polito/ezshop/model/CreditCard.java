package it.polito.ezshop.model;

public class CreditCard {
    private String cardNumber;
    private double balance;

    public CreditCard(String cardNumber, double balance) {
        super();
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static boolean validateWithLuhn(String creditCard) {
        int dimension = creditCard.length();
        boolean isOdd = true;
        int temp, sum = 0;
        for (int i = dimension - 2; i >= 0; --i) {
            temp = Character.getNumericValue((creditCard.charAt(i)));
            if (isOdd) {
                temp =  temp * 2;
                //sum += temp / 10;
                if (temp>9)
                    temp-=9;
            }
            isOdd = !isOdd;
            sum += temp;
        }
        return ((sum % 10) == creditCard.charAt(dimension-1)-'0');
    }


}
