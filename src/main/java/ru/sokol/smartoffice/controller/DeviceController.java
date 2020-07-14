package ru.sokol.smartoffice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sokol.smartoffice.model.Device;
import ru.sokol.smartoffice.service.DevicesServiceImpl;

import java.util.Collection;

@Controller
public class DeviceController {

    private final DevicesServiceImpl devicesService;
    private final SimpMessagingTemplate template;

    public DeviceController(DevicesServiceImpl devicesService, SimpMessagingTemplate template) {
        this.devicesService = devicesService;
        this.template = template;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld(){
        return "HelloWorld";
    }

    @GetMapping("/devices")
    @ResponseBody
    public Collection<Device> getDevicesStatus(){
        return devicesService.getListedDevices();
    }


    @SubscribeMapping("/devices")
    public Collection<Device> chatInit() {
        return devicesService.getListedDevices();
    }
/*

    @MessageMapping("/devices")
    public void processDeviceCommand()
*/

/*

    @Scheduled(fixedRate = 5000)
    public void greeting() {
        this.template.convertAndSend("/topic/devices", DeviceEnum.LASER.getDevice());
    }
*/

}
