package com.ticket;

import android.app.Application;

import com.ticket.utils.CrashHandler;

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
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
