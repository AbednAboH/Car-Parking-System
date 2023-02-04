package il.cshaifasweng.OCSFMediatorExample.server;



import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Getter
@Setter
public class TimeTriggeredThread implements Runnable {
    private SimpleServerClass server;

    public TimeTriggeredThread(SimpleServerClass server) {
        this.server = server;
    }

    @Override
    public void run() {
    }
    public  void initThread( ScheduledExecutorService executorService, int delay, int period, TimeUnit timeUnit) {
        // schedule reminders and penalties to be executed every unit of time
        executorService.scheduleAtFixedRate(new TimeTriggeredThread(this.server), delay, period, timeUnit);
    }
}