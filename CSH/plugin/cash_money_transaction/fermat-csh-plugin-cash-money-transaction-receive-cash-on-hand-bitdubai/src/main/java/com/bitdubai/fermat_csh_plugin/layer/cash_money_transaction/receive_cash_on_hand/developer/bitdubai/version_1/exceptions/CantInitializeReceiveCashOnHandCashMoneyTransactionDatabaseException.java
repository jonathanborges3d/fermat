package com.bitdubai.fermat_csh_plugin.layer.cash_money_transaction.receive_cash_on_hand.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;
/**
 * The Class <code>package com.bitdubai.fermat_csh_plugin.layer.cash_money_transaction.receive_cash_on_hand.developer.bitdubai.version_1.exceptions.CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException</code>
 * is thrown when an error occurs initializing database
 * <p/>
 *
 * Created by Yordin Alayn - (y.alayn@gmail.com) on 01/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CAN'T INTIALIZE RECEIVE CASH ON HAND CASH MONEY TRANSACTION DATABASE EXCEPTION";

    public CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException(final String message) {
        this(message, null);
    }

    public CantInitializeReceiveCashOnHandCashMoneyTransactionDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}