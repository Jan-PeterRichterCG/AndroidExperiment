package eu.jprichter.squaresandroots.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.jprichter.squaresandroots.kernel.impl.Kernel;
import roboguice.RoboGuice;
import roboguice.util.Ln;

/**
 * Created by jrichter on 25.10.2015.
 */
public class StatisticsDrawableView extends View {
    private List<ShapeDrawable> diagramBlocks = new ArrayList<ShapeDrawable>();
    private ShapeDrawable frame;

    @Inject Kernel kernel;

    public StatisticsDrawableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        if (!isInEditMode()) {
            RoboGuice.getInjector(getContext()).injectMembers(this);

            setMinimumHeight(kernel.getMaxRoot()*20);
        }


        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView created.");

        for (int n=0; n < 16; n++) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
            shapeDrawable.getPaint().setColor(0xff000000 + n * 0x00111111);
            shapeDrawable.setBounds(10, 10 + 22*n , 200, 10 + 22*n + 20);
            diagramBlocks.add(shapeDrawable);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView measured. Width = " +
                w + " Height = " + h);

        int maxRoot = kernel.getMaxRoot();


        frame = new ShapeDrawable(new RectShape());
        frame.getPaint().setColor(0xffff0000);
        frame.setBounds(0, 0, w, h);


    }

    protected void onDraw(Canvas canvas) {
        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView drawn.");

        frame.draw(canvas);

        for(ShapeDrawable s : diagramBlocks) {
            s.draw(canvas);
        }
    }
}

