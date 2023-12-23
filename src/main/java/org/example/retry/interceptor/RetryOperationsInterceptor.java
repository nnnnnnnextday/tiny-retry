package org.example.retry.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.retry.RetryCallback;
import org.example.retry.RetryContext;
import org.example.retry.RetryOperations;
import org.example.retry.support.RetryTemplate;
import org.springframework.aop.ProxyMethodInvocation;

/**
 * 对标RetryOperationsInterceptor
 */
public class RetryOperationsInterceptor implements MethodInterceptor {
    private MethodInvocationRecoverer<?> recoverer;

    private RetryOperations retryOperations = new RetryTemplate();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        String name = invocation.getMethod().toGenericString();

        RetryCallback<Object, Throwable> retryCallback = new MethodInvocationRetryCallback<Object, Throwable>(invocation, name) {
            @Override
            public Object doWithRetry(RetryContext context) throws Exception {
                if (this.invocation instanceof ProxyMethodInvocation) {
                    try {
                        Object result = ((ProxyMethodInvocation) this.invocation).invocableClone().proceed();
                        return result;
                    } catch (Exception | Error e) {
                        throw e;
                    } catch (Throwable throwable) {
                        throw new IllegalStateException(throwable);
                    }
                } else {
                    throw new IllegalStateException("this should not happen with AOP");
                }
            }
        };

        return this.retryOperations.execute(retryCallback);
    }
}
