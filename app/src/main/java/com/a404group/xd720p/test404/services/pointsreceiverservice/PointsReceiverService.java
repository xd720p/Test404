package com.a404group.xd720p.test404.services.pointsreceiverservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.a404group.xd720p.test404.R;
import com.a404group.xd720p.test404.core.services.pointsprovider.PointProvider;
import com.a404group.xd720p.test404.core.services.pointsprovider.RandomPointProvider;
import com.a404group.xd720p.test404.dto.Point;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xd720p on 10.07.2017.
 */

public class PointsReceiverService extends Service {

    private PointProvider pointProvider;
    private Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pointProvider = new RandomPointProvider();
        receiveNewPoint();
        return super.onStartCommand(intent, flags, startId);
    }

    private void receiveNewPoint() {
        timer = new Timer();
        long interval = 1000;
        timer.schedule(updateTask, 0, interval);
    }

    TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Point newPoint;

            newPoint = pointProvider.receivePoint(new Date(System.currentTimeMillis()));

            Intent intent = new Intent(getString(R.string.point_broadcast));
            intent.putExtra(getString(R.string.point), newPoint);
            LocalBroadcastManager.getInstance(PointsReceiverService.this).sendBroadcast(intent);
        }
    };
}
