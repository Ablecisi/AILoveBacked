package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.UserActiveDayMapper;
import com.ablecisi.ailovebacked.service.UserActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    private static final ZoneId CN = ZoneId.of("Asia/Shanghai");

    private final UserActiveDayMapper userActiveDayMapper;

    public UserActivityServiceImpl(UserActiveDayMapper userActiveDayMapper) {
        this.userActiveDayMapper = userActiveDayMapper;
    }

    @Override
    public void touchCurrentUser(long userId) {
        LocalDate today = LocalDate.now(CN);
        userActiveDayMapper.upsert(userId, today);
    }
}
