package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sokol.smartoffice.service.MegatronSokolServiceImpl;
import ru.sokol.smartoffice.service.exception.MegatronException;

@Controller
public class MegatronController {

    private final MegatronSokolServiceImpl megatronSokolService;

    public MegatronController(MegatronSokolServiceImpl megatronSokolService) {
        this.megatronSokolService = megatronSokolService;
    }

    @GetMapping("/megatron")
    String getMegatronPage(Model model){
        if(megatronSokolService.getMegatronPowerState()){
            model.addAttribute("testToken", megatronSokolService.getMegatronTestToken());
            return "megatron3000Active";
        }else {
            return "megatron3000Inactive";
        }
    }
    @PostMapping("/megatronProcess")
    String processMegatronRequest(Model model, @RequestParam("token") String token){
        if(megatronSokolService.getMegatronPowerState()){
            model.addAttribute("testToken", megatronSokolService.getMegatronTestToken());
            try {
                megatronSokolService.startMegatron(token);
            } catch (MegatronException e) {
                e.printStackTrace();
            }
            return "megatron3000Active";
        }else {
            return "megatron3000Inactive";
        }
    }

}
