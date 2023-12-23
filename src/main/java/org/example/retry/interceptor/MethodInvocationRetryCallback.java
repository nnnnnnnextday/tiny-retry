package org.example.retry.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.example.retry.RetryCallback;
import org.springframework.util.StringUtils;

public abstract class MethodInvocationRetryCallback<T, E extends Throwable> implements RetryCallback<T, E> {

    public MethodInvocation invocation;

    public String label;

    public MethodInvocationRetryCallback(MethodInvocation invocation, String label) {
        this.invocation = invocation;
        if (StringUtils.isEmpty(label)) {
            this.label = invocation.getMethod().toGenericString();
        } else {
            this.label = label;
        }
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }

    public void setInvocation(MethodInvocation invocation) {
        this.invocation = invocation;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
