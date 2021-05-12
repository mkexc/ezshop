package it.polito.ezshop.model.accountbook;

public class CreditCard {
    private String cardNumber;
    private double balance;

    public CreditCard(String cardNumber, double balance) {
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

    public boolean validateWithLuhn() {
        return false;
    }

    public Integer computeOperation() {
        return null;
    }

    public static boolean luhnValidation(String creditCard)
    {
        int dimension = creditCard.length();
        boolean isSecond = false;
        int temp,sum = 0;
        for(int i = dimension-1; i>=0; --i)
        {
            if(isSecond)
            {
                temp=Character.getNumericValue((creditCard.charAt(i)))*2;
                sum += temp / 10;
                sum += temp % 10;
            }
            isSecond=!isSecond;
        }
        return (sum%10 == 0);
    }

}
