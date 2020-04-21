package com.hz.magic.component.disruptor.dsl;

import com.hz.magic.component.disruptor.Sequence;
import com.hz.magic.component.SequenceBarrier;

import java.util.concurrent.Executor;

interface ConsumerInfo
{
    Sequence[] getSequences();

    SequenceBarrier getBarrier();

    boolean isEndOfChain();

    void start(Executor executor);

    void halt();

    void markAsUsedInBarrier();

    boolean isRunning();
}
