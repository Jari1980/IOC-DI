package se.lexicon;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import se.lexicon.config.AppConfig;
import se.lexicon.model.CryptoCurrency;
import se.lexicon.model.Transaction;
import se.lexicon.model.Wallet;
import se.lexicon.service.TransactionManagement;
import se.lexicon.service.WalletManagement;
import se.lexicon.service.impl.TransactionManagementImpl;
import se.lexicon.service.impl.WalletManagementImpl;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        //Setup Application
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        //Services
        WalletManagement walletManagement = context.getBean(WalletManagementImpl.class);
        TransactionManagement management = context.getBean(TransactionManagementImpl.class);


        Wallet jari_wallet = walletManagement.create("Jari's wallet");


        Transaction transactionBTC1Jari = management.createDepositTransaction(
                jari_wallet.getId(),
                CryptoCurrency.BTC,
                new BigDecimal(1),
                "Test"
        );

        Transaction transactionETH10Jari = management.createDepositTransaction(
                jari_wallet.getId(),
                CryptoCurrency.ETH,
                new BigDecimal(10),
                "test"
        );

        Transaction transactionBNB1000Jari = management.createDepositTransaction(
                jari_wallet.getId(),
                CryptoCurrency.BNB,
                new BigDecimal(1000),
                "test"
        );

        System.out.println(walletManagement.getById(jari_wallet.getId()));
        System.out.println("-----------------------------------------");

        //Testing createWithdrawalTransaction
        /* //Working as intended
        Transaction shouldFail = management.createWithdrawalTransaction(
                jari_wallet.getId(),
                CryptoCurrency.BTC,
                new BigDecimal(2),
                "More than in bank should throw exception"
        );
         */
        Transaction transactionWithdrawalBNBJari = management.createWithdrawalTransaction(
                jari_wallet.getId(),
                CryptoCurrency.BNB,
                new BigDecimal(2),
                "More than in bank should throw exception"
        );
        System.out.println(walletManagement.getById(jari_wallet.getId()));
        System.out.println("----------------------------------------------------");

        //Testing getTransactionsByWalletId, working
        System.out.println(management.getTransactionsByWalletId("This should return empty array"));
        System.out.println(management.getTransactionsByWalletId(jari_wallet.getId()));
    }
}