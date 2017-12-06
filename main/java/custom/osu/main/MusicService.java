package custom.osu.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service
{
    ServiceThread thread = new ServiceThread();
    public IBinder onBind(Intent i)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Start service !");
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.continu = false;
        System.out.println("Destroyed service !");
    }

    public static class ServiceThread extends Thread
    {
        public static boolean continu = true;
        public void run()
        {
            while(true)
            {
                try
                {
                    Thread.sleep(1000);
                }catch(Exception e){}
                //System.out.println("Passe");
            }

        }
    }
}
