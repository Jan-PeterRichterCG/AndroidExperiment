package eu.jprichter.squaresandroots.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.jprichter.squaresandroots.kernel.IKernel;
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
    private int minimumHeightIsValidForMaxRoot = 0;
    private boolean diagramBlocksAreValid = false;

    // private ShapeDrawable frame;

    @Inject
    Kernel kernel;

    private static final int LEFT_MARGIN = 10;
    private static final int LEGEND_COLUMN_WIDTH = 60;
    private static final int LEGEND_COLUMN_MARGIN = 10;
    private static final int TOP_MARGIN = 10;
    private static final int BAR_HEIGHT = 40;
    private static final int BAR_VERTICAL_SPACING = 5;
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HORIZONTAL_SPACING = 5;
    private static final int TEXT_ADJUSTMENT = 10;
    private static final int SUCCESS_COLOR = 0xFF00E000;
    private static final int FAILURE_COLOR = 0xFFE00000;

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
    protected void onDraw(Canvas canvas) {
        Ln.d("XXXXXXXXXXXXXXXXXX redraw StatisticsDrawableView");

        this.setBackgroundColor(Color.DKGRAY);

        /* change the minimum height if we need more / less space.
        Note that this will eventually lead to a complete redraw. */
        if(minimumHeightIsValidForMaxRoot != kernel.getMaxRoot()) {
            setMinimumHeight(2 * TOP_MARGIN +
            kernel.getMaxRoot() * (BAR_HEIGHT + BAR_VERTICAL_SPACING));
            minimumHeightIsValidForMaxRoot = kernel.getMaxRoot();
        }

        if(! diagramBlocksAreValid) {
            prepareDiagramBlocks();
        }

        for (ShapeDrawable s : diagramBlocks) {
            s.draw(canvas);
        }

        if (!isInEditMode()) {

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(BAR_HEIGHT);
            paint.setTextAlign(Paint.Align.RIGHT);
            for (int i = 1; i <= kernel.getMaxRoot(); i++) {
                canvas.drawText(String.format("%2d", i),
                        LEFT_MARGIN + LEGEND_COLUMN_WIDTH,
                        TOP_MARGIN + i * (BAR_HEIGHT + BAR_VERTICAL_SPACING) - TEXT_ADJUSTMENT,
                        paint);
            }
        }
    }

    public void invalidateDiagramBlocks() {
        Ln.d("XXXXXXXXXXXXXXXXXX Diagram Blocks invalidated");
        diagramBlocksAreValid = false;
    }

    /**
     * Prepare the list of ShapeDrawables that represent the statistics
     */
    private void prepareDiagramBlocks() {

        diagramBlocks.removeAll(diagramBlocks);

        if (!isInEditMode()) {
            for (int root = 1; root <= kernel.getMaxRoot(); root++) {
                IKernel.StatisticsEntry statistics = kernel.getStatistics(root);
                int s;
                for (s = 1; s <= statistics.successes; s++) {
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                    shapeDrawable.getPaint().setColor(SUCCESS_COLOR);
                    int x1 = LEFT_MARGIN + LEGEND_COLUMN_WIDTH + LEGEND_COLUMN_MARGIN +
                            (s - 1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                    int y1 = TOP_MARGIN + (root - 1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                    int x2 = x1 + BAR_WIDTH;
                    int y2 = y1 + BAR_HEIGHT;
                    shapeDrawable.setBounds(x1, y1, x2, y2);
                    diagramBlocks.add(shapeDrawable);
                }
                for (int f = 1; f <= statistics.failures; f++) {
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
                    shapeDrawable.getPaint().setColor(FAILURE_COLOR);
                    int x1 = LEFT_MARGIN + +LEGEND_COLUMN_WIDTH + LEGEND_COLUMN_MARGIN +
                            (s - 1 + f - 1) * (BAR_WIDTH + BAR_HORIZONTAL_SPACING);
                    int y1 = TOP_MARGIN + (root - 1) * (BAR_HEIGHT + BAR_VERTICAL_SPACING);
                    int x2 = x1 + BAR_WIDTH;
                    int y2 = y1 + BAR_HEIGHT;
                    shapeDrawable.setBounds(x1, y1, x2, y2);
                    diagramBlocks.add(shapeDrawable);
                }
            }
        }
        diagramBlocksAreValid = true;

    }
}

