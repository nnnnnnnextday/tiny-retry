package org.example.retry.interceptor;

public interface MethodInvocationRecoverer<T> {

    T recover(Object[] args, Throwable cause);
}
