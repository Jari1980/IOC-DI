package se.lexicon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lexicon.dao.TransactionDao;
import se.lexicon.dao.WalletDao;
import se.lexicon.exception.WalletNotFoundException;
import se.lexicon.model.CryptoCurrency;
import se.lexicon.model.Transaction;
import se.lexicon.model.Wallet;
import se.lexicon.service.TransactionManagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component // Making a Bean
public class TransactionManagementImpl implements TransactionManagement {

    //Dependencies
    private final WalletDao walletDao;
    private final TransactionDao transactionDao;

    //Dependency inject
    @Autowired
    public TransactionManagementImpl(WalletDao walletDao, TransactionDao transactionDao) {
        this.walletDao = walletDao;
        this.transactionDao = transactionDao;
    }

    //1. Check for Null
    //2. Check wallet ID exists (call Dao)
    //3. Valid amount?
    //4. Call deposit method from wallet class
    //5. Create transaction record
    //6. Save the transaction to history (dao)
    @Override
    public Transaction createDepositTransaction(String walletId, CryptoCurrency cryptoCurrency, BigDecimal amount, String description) {

        if (walletId == null || cryptoCurrency == null || amount == null || description == null)
            throw new IllegalArgumentException("Transaction params were not valid");

        Optional<Wallet> optionalWallet = walletDao.findWallet(walletId);
        if (optionalWallet.isEmpty()) throw new WalletNotFoundException("Wallet not Found");

        Wallet wallet = optionalWallet.get();
        wallet.deposit(cryptoCurrency, amount);

        Transaction transaction = new Transaction("DEPOSIT", amount, walletId, cryptoCurrency.getName(), description);

        return transactionDao.createTransaction(transaction);

    }

    @Override
    public Transaction createWithdrawalTransaction(String walletId, CryptoCurrency cryptoCurrency, BigDecimal amount, String description) {

        if(walletId == null || cryptoCurrency == null || amount == null || description == null)
            throw new IllegalArgumentException("Transaction params were not valid");

        Optional<Wallet> optionalWallet = walletDao.findWallet(walletId);
        if(optionalWallet.isEmpty()) throw new WalletNotFoundException("Wallet not Found");

        Wallet wallet = optionalWallet.get();
        if(wallet.getBalance(cryptoCurrency).compareTo(amount) < 0) throw new RuntimeException("Insufficient Balance");
        wallet.withdrawal(cryptoCurrency, amount);

        Transaction transaction = new Transaction("WITHDRAWAL", amount, walletId, cryptoCurrency.getName(), description);

        return transactionDao.createTransaction(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByWalletId(String WalletId) {

        if(WalletId == null || WalletId.isEmpty()) throw new IllegalArgumentException("Transaction params were not valid");

        return transactionDao.findTransactionsByWalletId(WalletId);
    }
}
