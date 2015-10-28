package eu.jprichter.squaresandroots.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.jprichter.squaresandroots.kernel.impl.Kernel;
import roboguice.RoboGuice;
import roboguice.util.Ln;

/**
 * A graphical view of the success statistics
 *
 * Created by jrichter on 25.10.2015.
 */
public class StatisticsDrawableView extends View {
    private List<ShapeDrawable> diagramBlocks = new ArrayList<ShapeDrawable>();
    private ShapeDrawable frame;

    @Inject Kernel kernel;

    private static final int LEFT_MARGIN = 10;
    private static final int TOP_MARGIN = 10;
    private static final int BAR_HEIGHT = 40;
    private static final int BAR_VERTICAL_SPACING = 2;
    private static final int BAR_WIDTH = 100;
    private static final int BAR_HORIZONTAL_SPACING = 1;
    private static final int SUCCESS_COLOR = 0xFF00FF00;
    private static final int FAILURE_COLOR = 0xFFFF0000;


    public StatisticsDrawableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView created.");

        if (!isInEditMode()) {
            RoboGuice.getInjector(getContext()).injectMembers(this);
            setMinimumHeight(2*TOP_MARGIN +
                    kernel.getMaxRoot()*(BAR_HEIGHT + BAR_VERTICAL_SPACING));
        }

        for (int root=1; root <= kernel.getMaxRoot(); root++) {
            int succ = kernel.getSucessful(root);
            int fail = kernel.getFailed(root);
            Ln.d("XXXXXXXXXXXXXXXXXX root: " + root + " succ: " + succ + " fail: " + fail);
            int s;
            for (s = 1; s <= succ; s++) {
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                shapeDrawable.getPaint().setColor(SUCCESS_COLOR);
                int x1 = LEFT_MARGIN + (s-1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                int y1 = TOP_MARGIN + (root-1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                int x2 = x1 + BAR_WIDTH;
                int y2 = y1 + BAR_HEIGHT;
                shapeDrawable.setBounds(x1, y1, x2, y2);
                diagramBlocks.add(shapeDrawable);
                Ln.d("XXXXXXXXXXXXXXXXXX Succ added: " + x1 + "/" + y1 + " - "+ x2 + "/" + y2);
            }
            for (int f = 1; f <= fail; f++) {
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                shapeDrawable.getPaint().setColor(FAILURE_COLOR);
                int x1 = LEFT_MARGIN + (s-1+f-1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                int y1 = TOP_MARGIN + (root-1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                int x2 = x1 + BAR_WIDTH;
                int y2 = y1 + BAR_HEIGHT;
                shapeDrawable.setBounds(x1, y1, x2, y2);
                diagramBlocks.add(shapeDrawable);
                Ln.d("XXXXXXXXXXXXXXXXXX Fail added: " + x1 + "/" + y1 + " - " + x2 + "/" + y2);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView measured. Width = " +
                w + " Height = " + h);

        int maxRoot = kernel.getMaxRoot();


        frame = new ShapeDrawable(new RectShape());
        frame.getPaint().setColor(0x800F0F00);
        frame.setBounds(0, 0, w, h);


    }

    protected void onDraw(Canvas canvas) {
        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView drawn.");

        frame.draw(canvas);

        for(ShapeDrawable s : diagramBlocks) {
            Ln.d("XXXXXXXXXXXXXXXXXX draw " +
                    s.getBounds().left + "/" +
                    s.getBounds().top + " - " +
                    s.getBounds().right + "/" +
                    s.getBounds().bottom);

            s.draw(canvas);
        }
    }
}

