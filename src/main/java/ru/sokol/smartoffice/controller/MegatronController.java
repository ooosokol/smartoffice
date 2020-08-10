package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sokol.smartoffice.service.FanServiceImpl;
import ru.sokol.smartoffice.service.MegatronSokolServiceImpl;
import ru.sokol.smartoffice.service.exception.MegatronException;

@Controller
public class MegatronController {

    private final MegatronSokolServiceImpl megatronSokolService;
    private final FanServiceImpl fanService;

    public MegatronController(MegatronSokolServiceImpl megatronSokolService, FanServiceImpl fanService) {
        this.megatronSokolService = megatronSokolService;
        this.fanService = fanService;
    }

    @GetMapping("/megatron")
    String getMegatronPage(Model model){
        if(megatronSokolService.getMegatronPowerState()){
            model.addAttribute("testToken", megatronSokolService.getMegatronTestToken());
            model.addAttribute("deviceReady", megatronSokolService.isReady());
            model.addAttribute("tokenExpireTime", megatronSokolService.getTokenExpireTime());
            model.addAttribute("gameFinished", fanService.getCurrentPhase().equals(Short.valueOf("5")));
            return "megatron3000Active";
        }else {
            return "megatron3000Inactive";
        }
    }
    @PostMapping("/megatron")
    String processMegatronRequest(Model model, @RequestParam("token") String token){
        if(megatronSokolService.getMegatronPowerState()){
            model.addAttribute("testToken", megatronSokolService.getMegatronTestToken());
            model.addAttribute("deviceReady", megatronSokolService.isReady());
            model.addAttribute("tokenExpireTime", megatronSokolService.getTokenExpireTime());
            model.addAttribute("gameFinished", fanService.getCurrentPhase().equals(Short.valueOf("5")));
            try {
                Boolean maxPower = megatronSokolService.startMegatron(token);
                model.addAttribute("maxPower", maxPower);
            } catch (MegatronException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", e.getMessage());
            }
            return "megatron3000Active";
        }else {
            return "megatron3000Inactive";
        }
    }

}
