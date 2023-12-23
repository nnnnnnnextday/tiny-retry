package org.example.retry.support;

import org.example.retry.*;
import org.example.retry.policy.RefuseNullRetryPolicy;
import org.example.retry.policy.SimpleRetryPolicy;

/**
 * 对标 RetryTemplate
 */
public class RetryTemplate implements RetryOperations {

    private RetryPolicy retryPolicy = new SimpleRetryPolicy();

    @Override
    public <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {

        RetryPolicy retryPolicy = this.retryPolicy;
        RetryContext context = null;//todo 补齐 content逻辑

        try {
            while (canRetry(retryPolicy, context)) {

                try {
                    T result = retryCallback.doWithRetry(context);

                    if (result == null && retryPolicy.getClass().equals(RefuseNullRetryPolicy.class)) {
                        throw new NullPointerException();
                    }//自己的补强逻辑

                    return result;
                } catch (Throwable e) {

                    if (canRetry(retryPolicy, context)) {
                        try {
                            Thread.sleep(2000);//模拟 backoff

                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

            }

            return handleRetryExhausted(null, context);
        } catch (Throwable e) {
            throw e;
        }
    }

    private <T> T handleRetryExhausted(RecoveryCallback recoveryCallback, RetryContext context) {
        //此处执行 recover 逻辑
        System.out.println("----执行 recover 逻辑----");
        return null;
    }

    private boolean canRetry(RetryPolicy retryPolicy, RetryContext context) {
        return true;
    }
}
