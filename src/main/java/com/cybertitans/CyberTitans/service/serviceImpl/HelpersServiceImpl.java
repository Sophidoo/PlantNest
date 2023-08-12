package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.service.HelpersService;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class HelpersServiceImpl implements HelpersService {
    @Override
    public String genRandDigits() {
        int number = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.format("%06d", number);
    }
}
