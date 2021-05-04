package com.greyka.imgr.interfaces;

public interface timer_handler {
    void onTickEvent();
    void onFinishEvent();
    void onCreateEvent(int secInFuture,timer_handler TH);
}