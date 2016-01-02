package com.ticket;

import android.app.Application;

import com.ticket.utils.CrashHandler;
import com.ticket.utils.TLog;

public class TicketsApplication extends Application {

    public static TicketsApplication ticketsApplication;

    public static TicketsApplication getTicketApplication() {
        return ticketsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ticketsApplication = this;
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        //TODO 禁用和启用Log
        TLog.disableLog();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
