package com.app.cine.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SeatHoldService {

    private static final long HOLD_TIME_MINUTES = 5;

    private final RedisTemplate<String, String> redisTemplate;

    public SeatHoldService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean holdSeat(Long sessionId, Long seatId, Long userId) {
        String key = buildKey(sessionId, seatId);

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, userId.toString(),
                        HOLD_TIME_MINUTES, TimeUnit.MINUTES);

        return Boolean.TRUE.equals(success);
    }

    public void releaseSeat(Long sessionId, Long seatId) {
        redisTemplate.delete(buildKey(sessionId, seatId));
    }

    public boolean isHeld(Long sessionId, Long seatId) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(buildKey(sessionId, seatId))
        );
    }

    public String getHolder(Long sessionId, Long seatId) {
        return redisTemplate.opsForValue()
                .get(buildKey(sessionId, seatId));
    }

    private String buildKey(Long sessionId, Long seatId) {
        return "seat:" + sessionId + ":" + seatId;
    }
}
