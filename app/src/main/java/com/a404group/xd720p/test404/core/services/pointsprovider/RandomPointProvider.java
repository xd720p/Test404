package com.a404group.xd720p.test404.core.services.pointsprovider;

import com.a404group.xd720p.test404.dto.Point;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by xd720p on 10.07.2017.
 */

public class RandomPointProvider implements PointProvider {

    private final int MIN_RAND = -1;
    private final int MAX_RAND = 1;
    private double currentPointValue = 0;

    @Override
    public Point receivePoint(Date clientDate) {
        Random random = new Random();
        Double randomValue = MIN_RAND + (random.nextDouble() * (MAX_RAND - MIN_RAND));

        currentPointValue = randomValue + currentPointValue;
        return new Point(clientDate, currentPointValue);
    }

}
