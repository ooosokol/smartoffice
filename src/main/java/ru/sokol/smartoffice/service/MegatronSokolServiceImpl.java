package ru.sokol.smartoffice.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MegatronSokolServiceImpl  {
    private static final String secret = "04080F10172A";
    private final AtomicLong counter;

    private final DevicesServiceImpl devicesService;


    public MegatronSokolServiceImpl(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
        counter = new AtomicLong(43);
    }


    public String getMegatronTestToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(String.valueOf(counter.get())));
    }

    private String getMegatronToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(counter.get() + secret));
    }

/*

    public void startMegatron(String megatronToken) throws MegatronException {
        if (getMegatronToken().equals(megatronToken)) {
            LaserDeviceHelper.getInstance().run(LaserDeviceRequest.builder()
                    .setPower(LaserDeviceRequest.MAX_POWER)
                    .setDuration(LaserDeviceRequest.MAX_DURATION)
                    .build()
            );
        } else if (getMegatronTestToken().equals(megatronToken)) {
            LaserDeviceHelper.getInstance().run(LaserDeviceRequest.builder()
                    .setPower(LaserDeviceRequest.MIN_POWER)
                    .setDuration(Duration.ofSeconds(10))
                    .build()
            );
        } else {
            throw new MegatronTockenInvalidException(megatronToken);
        }
        counter.getAndIncrement();
        PushNotificationService.getInstance().sendNewToken(getMegatronToken());
    }
*/


}
