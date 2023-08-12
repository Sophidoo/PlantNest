package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.enums.OtpType;
import com.cybertitans.CyberTitans.model.Otp;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.OtpRepository;
import com.cybertitans.CyberTitans.service.OtpService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import static com.asosyalbebe.moment4j.Moment.moment;

@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final ModelMapper mapper;
    private final HelpersServiceImpl helpersService;
    private final OtpRepository otpRepository;

    public OtpServiceImpl(ModelMapper mapper,HelpersServiceImpl helpersService,OtpRepository otpRepository){
        this.mapper=mapper;
        this.helpersService=helpersService;
        this.otpRepository=otpRepository;
    }
    @Override
    public String create(User user) {
        clearOtps(user,OtpType.REGISTER);
        Otp otp = new Otp();
        otp.setUser(user);
        otp.setToken(helpersService.genRandDigits());
        otp.setExpiry(moment().add(30, Calendar.MINUTE).toDate());
        otp.setOtpType(OtpType.REGISTER);
        otpRepository.save(otp);
        return otp.getToken();
    }

    @Override
    public String create(User user,OtpType otpType) {
        clearOtps(user,otpType);
        Otp otp = new Otp();
        otp.setUser(user);
        otp.setToken(helpersService.genRandDigits());
        otp.setExpiry(moment().add(30, Calendar.MINUTE).toDate());
        otp.setOtpType(otpType);
        otpRepository.save(otp);
        return otp.getToken();
    }

    @Override
    public Boolean verify(User user, String token,OtpType otpType) {
        Optional<Otp> otp = otpRepository.findByUserAndTokenAndOtpType(user,token,otpType);
            return otp.isPresent() && moment(otp.get().getExpiry()).isAfter(moment());

    }

     void clearOtps(User user,OtpType otpType) {
        otpRepository.deleteAllByUserAndOtpType(user,otpType);
    }
}
