package org.example.retry;

public interface RetryCallback<T, E extends Throwable> {

    T doWithRetry(RetryContext context) throws E;
}
