package com.hz.magic.component;

public interface TimeoutHandler
{
    void onTimeout(long sequence) throws Exception;
}
