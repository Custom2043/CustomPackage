package custom.osu.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import custom.osu.beatmap.Hit;

/**
 * Created by utilisateur on 12/11/2017.
 */

public class GameView extends View
{
    private int size;
    private int life = 500;
    public long dif, totalTime;
    private boolean perdu = false;
    private ArrayList<Hit> hits = new ArrayList<Hit>();
    public GameView(Context context, AttributeSet s)
    {
        super(context, s);
        this.setWillNotDraw(false);
        int total = (int)(Math.random() * 50 + 100);
        int time = 0, color = 0xFFFF0000;
        for (int i=0;i<total;i++)
        {
            hits.add(new Hit((int)(Math.random() * 512), (int)(Math.random() * 384), 510, time, color));
            time += Math.random() * 200 + 200;
            if (Math.random() < 0.15) // Change color
            {
                if (color == 0xFFFF0000) color = 0xFF888800;
                else if (color == 0xFF888800) color = 0xFF00FF00;
                else if (color == 0xFF00FF00) color = 0xFF0000FF;
                else if (color == 0xFF0000FF) color = 0xFFFF0000;
            }
        }
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Hit.height = MeasureSpec.getSize(heightMeasureSpec);
        Hit.width = MeasureSpec.getSize(widthMeasureSpec);
        Hit.size = size = (int)(0.07f*Hit.height);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public void onDraw(Canvas c) // 50 fois par seconde
    {
        super.onDraw(c);
        life --;

        if (life < 0) {
            perdu = true;
            life = 0;
        }

        Paint paint = new Paint();
        if (life > 100)
            paint.setColor(Color.BLACK);
        else
            paint.setColor((0xFF00 + (100-life)/2) << 16);
        paint.setStyle(Paint.Style.FILL);
        c.drawPaint(paint);

       if (life == 0){
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            c.drawText("PERDU LOL T TROP NUL MDR", 20, 620, paint);
            return;
        }

        paint.setColor(Color.WHITE);
        c.drawRect(new Rect(Hit.width - 15, 10, Hit.width - 10, (int) ((500 - 10) * life / 500f)), paint);
        paint.setStrokeWidth(5);

        for (Hit hit : hits)
        {

            if (totalTime >= hit.startTime && !hit.touched) {
                if (totalTime < hit.endTime) {
                    hit.remainingTime -= dif;
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    c.drawCircle(hit.getX(), hit.getY(), size, paint);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(hit.color);
                    c.drawCircle(hit.getX(), hit.getY(), (((float) hit.remainingTime) / hit.duration + 1) * size, paint);
                } else if (!hit.touched) {
                    life -= 50;
                    hit.touched = true;
                }
            }
        }
    }

    public void press(int x, int y)
    {
        for (Hit hit : hits)
        {
            if (totalTime >= hit.startTime &&  totalTime < hit.endTime && !hit.touched) // Temporary in
            {
                if (Math.sqrt((x - hit.getX()) * (x - hit.getX()) + (y - hit.getY()) * (y - hit.getY())) < size) // Spacially in
                {
                    hit.touched = true;
                    if (hit.remainingTime < hit.duration / 6)
                        life += 30;
                    else if (hit.remainingTime <  2 * hit.duration / 6)
                        life += 10;
                    else if (hit.remainingTime < hit.duration / 2)
                        life += 5;
                    else
                        hit.touched = false; // not really touched
                }
            }
        }
        if (life > 500)
            life = 500;
    }
}
