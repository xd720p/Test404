package com.a404group.xd720p.test404.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.a404group.xd720p.test404.R;
import com.a404group.xd720p.test404.dto.Point;
import com.a404group.xd720p.test404.services.pointsreceiverservice.PointsReceiverService;
import com.scichart.charting.ClipMode;
import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.modifiers.AxisDragModifierBase;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.annotations.HorizontalLineAnnotation;
import com.scichart.charting.visuals.axes.AutoRange;
import com.scichart.charting.visuals.axes.IAxis;

import com.scichart.charting.visuals.renderableSeries.IRenderableSeries;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.extensions.builders.SciChartBuilder;

import java.util.Collections;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.chart_layout)
    LinearLayout chartLayout;
    private SciChartSurface surface;
    private SciChartBuilder sciChartBuilder;
    private XyDataSeries mountainData;
    private IRenderableSeries mountainSeries;
    private HorizontalLineAnnotation horizontalLineAnnotation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSurface();

        startService(new Intent(this, PointsReceiverService.class));
        IntentFilter intentFilter = new IntentFilter(getString(R.string.point_broadcast));
        LocalBroadcastManager.getInstance(this).registerReceiver(pointsBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pointsBroadcastReceiver);
        super.onDestroy();
    }

    private void initSurface() {
        surface = new SciChartSurface(this);
        chartLayout.addView(surface);
        SciChartBuilder.init(this);
        sciChartBuilder = SciChartBuilder.instance();
        final IAxis xAxis = sciChartBuilder.newDateAxis()
                .withVisibleRange(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 60000))
                .withTextFormatting("hh:mm:ss")
                .build();

        final IAxis yAxis = sciChartBuilder.newNumericAxis()
                .withVisibleRange(-4, 4)
                .withAutoRangeMode(AutoRange.Once)
                .build();

        ModifierGroup additionalModifiers = sciChartBuilder.newModifierGroup()

                .withPinchZoomModifier().build()

                .withZoomPanModifier().withReceiveHandledEvents(true).build()

                .withZoomExtentsModifier().withReceiveHandledEvents(true).build()

                .withXAxisDragModifier().withReceiveHandledEvents(true).withDragMode(AxisDragModifierBase.AxisDragMode.Scale).withClipModex(ClipMode.None).build()

                .withYAxisDragModifier().withReceiveHandledEvents(true).withDragMode(AxisDragModifierBase.AxisDragMode.Pan).build()

                .build();

        mountainData = sciChartBuilder.newXyDataSeries(Date.class, Double.class).withFifoCapacity(3600).build();

        mountainSeries = sciChartBuilder.newMountainSeries()
                .withDataSeries(mountainData)
                .withStrokeStyle(ColorUtil.CornflowerBlue)
                .build();

        Collections.addAll(surface.getYAxes(), yAxis);
        Collections.addAll(surface.getXAxes(), xAxis);
        Collections.addAll(surface.getChartModifiers(), additionalModifiers);
        surface.getRenderableSeries().add(mountainSeries);
    }

    private BroadcastReceiver pointsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Point newPoint = (Point) intent.getSerializableExtra(getString(R.string.point));
            if ((newPoint) != null) {
                updateMountain(newPoint);
            }
        }
    };


    private void updateMountain(final Point newPoint) {
        UpdateSuspender.using(surface, new Runnable() {
            @Override
            public void run() {
                mountainData.append(newPoint.getDateTime(), newPoint.getRate());
                surface.getAnnotations().remove(horizontalLineAnnotation);
                horizontalLineAnnotation = sciChartBuilder.newHorizontalLineAnnotation()
                        .withPosition(0, newPoint.getRate())
                        .withStroke(2, ColorUtil.White)
                        .withHorizontalGravity(Gravity.RIGHT)
                        .build();
                surface.getAnnotations().add(horizontalLineAnnotation);

            }
        });

    }

}
