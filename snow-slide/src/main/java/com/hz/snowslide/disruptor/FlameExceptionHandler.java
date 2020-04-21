package com.hz.snowslide.disruptor;


import com.hz.magic.component.ExceptionHandler;

/**
 * <p>Package:com.hz.snowslide.disruptor</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 6:38
 */
public class FlameExceptionHandler implements ExceptionHandler {

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        //nothing
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        //nothing
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        //nothing
    }
}
