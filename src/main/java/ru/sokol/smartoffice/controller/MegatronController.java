package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sokol.smartoffice.model.device.DeviceEnum;

@Controller
public class MegatronController {
    final DeviceEnum megatronLaserSwitch = DeviceEnum.SWITCH2;

    @GetMapping("/megatron")
    String getMegatronPage(){
        if(megatronLaserSwitch.getDevice().getPower()){
            return "megatron3000Active";
        }else {
            return "megatron3000Inactive";
        }
    }

}
