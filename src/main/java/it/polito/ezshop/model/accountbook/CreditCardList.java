package it.polito.ezshop.model.accountbook;

import java.util.ArrayList;
import java.util.Iterator;

public class CreditCardList {
    private ArrayList<CreditCard> CreditCards;

    ArrayList<CreditCard> getAllCreditCards () {
        return this.CreditCards;
    }
    boolean addNewCreditCard(String cardNumber, double balance) {
        return CreditCards.add(
                new CreditCard(cardNumber,balance)
        );
    }

    boolean deleteCreditCard(String cardNumber) {
        for (CreditCard c : CreditCards) {
            if(c.getCardNumber().equals(cardNumber))
            {
                CreditCards.remove(c);
                return true;
            }
        }
        return false;
    }
    //savePersistent()

}
