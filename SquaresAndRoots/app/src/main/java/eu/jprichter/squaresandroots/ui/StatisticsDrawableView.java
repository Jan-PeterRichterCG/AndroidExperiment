package eu.jprichter.squaresandroots.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
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
 * <p/>
 * Created by jrichter on 25.10.2015.
 */
public class StatisticsDrawableView extends View {
    private List<ShapeDrawable> diagramBlocks = new ArrayList<ShapeDrawable>();
    private int diagramBlocksIsValidForMaxRoot = 0;
    private ShapeDrawable frame;

    @Inject
    Kernel kernel;

    private static final int LEFT_MARGIN = 10;
    private static final int LEGEND_COLUMN_WIDTH = 60;
    private static final int TOP_MARGIN = 10;
    private static final int BAR_HEIGHT = 40;
    private static final int BAR_VERTICAL_SPACING = 5;
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HORIZONTAL_SPACING = 5;
    private static final int TEXT_ADJUSTMENT = 5;
    private static final int LINE_WIDTH = 1;
    private static final int SUCCESS_COLOR = 0xFF00F000;
    private static final int FAILURE_COLOR = 0xFFF00000;

    public StatisticsDrawableView(Context context) {
        super(context);
        RoboGuice.getInjector(getContext()).injectMembers(this);
        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView created via StatisticsDrawableView(Context).");
    }

    public StatisticsDrawableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RoboGuice.getInjector(getContext()).injectMembers(this);
        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView created via StatisticsDrawableView(Context, AttributeSet).");
    }

    public StatisticsDrawableView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        RoboGuice.getInjector(getContext()).injectMembers(this);
        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView created via StatisticsDrawableView(Context, AttributeSet, DefStyle).");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        Ln.d("XXXXXXXXXXXXXXXXXX StatisticsDrawableView measured. Width = " + w + " Height = " + h);

        prepareFrame(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Ln.d("XXXXXXXXXXXXXXXXXX redraw StatisticsDrawableView");

        if(diagramBlocksIsValidForMaxRoot != kernel.getMaxRoot()) {
            /* do this only if maxRoot has changed */
            prepareDiagramBlocks();
            diagramBlocksIsValidForMaxRoot = kernel.getMaxRoot();
        }

        frame.draw(canvas);

        for (ShapeDrawable s : diagramBlocks) {
            s.draw(canvas);
        }

        if (!isInEditMode()) {

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(BAR_HEIGHT);
            for (int i = 1; i <= kernel.getMaxRoot(); i++) {
                canvas.drawText(Integer.valueOf(i).toString(),
                        LEFT_MARGIN,
                        TOP_MARGIN + i * (BAR_HEIGHT + BAR_VERTICAL_SPACING) - TEXT_ADJUSTMENT,
                        paint);
            }
        }
    }

    /**
     * Prepare a frame as a ShapeDrawable
     */
    private void prepareFrame(int w, int h) {

        Path path = new Path();
        path.addRect(LEFT_MARGIN / 2, TOP_MARGIN / 2, w - LEFT_MARGIN, h - TOP_MARGIN, Path.Direction.CW);
        frame = new ShapeDrawable(new PathShape(path, w, h));
        Paint paint = frame.getPaint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        frame.setBounds(0, 0, w, h);
    }

    /**
     * Prepare the list of ShapeDrawables that represent the statistics
     */
    private void prepareDiagramBlocks() {

        diagramBlocks.removeAll(diagramBlocks);

        if (!isInEditMode()) {
            RoboGuice.getInjector(getContext()).injectMembers(this);
            setMinimumHeight(2 * TOP_MARGIN +
                    kernel.getMaxRoot() * (BAR_HEIGHT + BAR_VERTICAL_SPACING));

            for (int root = 1; root <= kernel.getMaxRoot(); root++) {
                int succ = kernel.getSucessful(root);
                int fail = kernel.getFailed(root);
                Ln.d("XXXXXXXXXXXXXXXXXX root: " + root + " succ: " + succ + " fail: " + fail);
                int s;
                for (s = 1; s <= succ; s++) {
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                    shapeDrawable.getPaint().setColor(SUCCESS_COLOR);
                    int x1 = LEFT_MARGIN + LEGEND_COLUMN_WIDTH +
                            (s - 1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                    int y1 = TOP_MARGIN + (root - 1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                    int x2 = x1 + BAR_WIDTH;
                    int y2 = y1 + BAR_HEIGHT;
                    shapeDrawable.setBounds(x1, y1, x2, y2);
                    diagramBlocks.add(shapeDrawable);
                }
                for (int f = 1; f <= fail; f++) {
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                    shapeDrawable.getPaint().setColor(FAILURE_COLOR);
                    int x1 = LEFT_MARGIN + +LEGEND_COLUMN_WIDTH +
                            (s - 1 + f - 1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                    int y1 = TOP_MARGIN + (root - 1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                    int x2 = x1 + BAR_WIDTH;
                    int y2 = y1 + BAR_HEIGHT;
                    shapeDrawable.setBounds(x1, y1, x2, y2);
                    diagramBlocks.add(shapeDrawable);
                }
            }
        }

    }
}

