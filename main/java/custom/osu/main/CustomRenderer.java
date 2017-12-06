package custom.osu.main;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import custom.osu.audio.MediaPlayerManager;
import custom.osu.drawer.CustomDrawer;

public class CustomRenderer implements GLSurfaceView.Renderer
{
    CustomTimer timer = new CustomTimer();
    boolean firstFrame = true;
    int dif = 0;
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        CustomDrawer.init();
        GLES10.glActiveTexture(GLES10.GL_TEXTURE0);
        GLES10.glEnable(GLES10.GL_TEXTURE_2D);
        GLES10.glDisable(GLES10.GL_DEPTH_TEST);
        GLES10.glDisable(GLES10.GL_CULL_FACE);
        GLES10.glEnable(GLES10.GL_BLEND);
        GLES10.glBlendFunc(GLES10.GL_SRC_ALPHA,GLES10.GL_ONE_MINUS_SRC_ALPHA);


    }
    boolean truc =false;
    public void onDrawFrame(GL10 unused) {
        if (firstFrame)
            timer.set0();
        dif = (int)timer.getDifference();
        timer.set0();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        CustomDrawer.draw();
        MediaPlayerManager.update();
        firstFrame = false;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {GLES20.glViewport(0, 0, MainActivity.getTrueWidth(), MainActivity.getTrueHeight());}
}
