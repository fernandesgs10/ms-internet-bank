package br.com.internet.bank.common;

@SuppressWarnings("ALL")
public class InternetBankException extends RuntimeException {

    public InternetBankException(String message) {
        super(message);
    }

    public InternetBankException(String message, Throwable cause) {
        super(message, cause);
    }
}
