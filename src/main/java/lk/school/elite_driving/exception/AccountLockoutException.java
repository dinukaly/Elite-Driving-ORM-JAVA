package lk.school.elite_driving.exception;

public class AccountLockoutException extends LoginException {
    public AccountLockoutException(String message) {
        super(message, new AccountLockoutException(message));
    }
}