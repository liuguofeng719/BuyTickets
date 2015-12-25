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
        if (Boolean.TRUE) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        }
        TLog.enableLog();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
