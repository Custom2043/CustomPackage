package custom.osu.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import custom.osu.audio.Audio;
import custom.osu.drawer.AngelCodeFont;
import custom.osu.drawer.CustomDrawer;

public class MainActivity extends Activity {

    private static GLSurfaceView mGLView;
    public static Context context;
    private static CustomRenderer renderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility( //Set fullscreen
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //get String : getIntent().getExtras.get("path")
        renderer = new CustomRenderer();

        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setPreserveEGLContextOnPause(true);
        mGLView.setRenderer(renderer);

        context = this.getBaseContext();

        setContentView(mGLView);

        startService();

        Audio.mainAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLView != null) {
            mGLView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLView != null) {
            mGLView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), MusicService.class));
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MusicService.class));
    }

    public static InputStream getAsset(String filename) throws IOException
    {
        return context.getAssets().open(filename);
    }
    public static AssetFileDescriptor getFileDescriptor(String filename) throws IOException
    {
        return context.getAssets().openFd(filename);
    }
    public static int getTrueWidth(){return mGLView.getWidth();}
    public static int getTrueHeight(){return mGLView.getHeight();}
    public static int getWidth()
    {
        return isScreenRotated() ? mGLView.getHeight() : mGLView.getWidth();
    }
    public static int getHeight()
    {
        return isScreenRotated() ? mGLView.getWidth() : mGLView.getHeight();
    }
    public static boolean isScreenRotated()
    {
        return mGLView.getHeight() > mGLView.getWidth();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (CustomDrawer.getScreen() != null) {
            int X = (int) event.getY();
            int Y = (int) event.getX();
            int eventAction = event.getAction();

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    CustomDrawer.getScreen().touchScreen(X, Y, 0);
                    break;
                case MotionEvent.ACTION_UP:
                    CustomDrawer.getScreen().touchScreen(X, Y, 1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    CustomDrawer.getScreen().touchScreen(X, Y, 2);
                    break;
            }
        }
        return true;
    }

    public static int getDif()
    {
        return renderer.dif;
    }
    public static boolean firstFrame()
    {
        return renderer.firstFrame;
    }
}