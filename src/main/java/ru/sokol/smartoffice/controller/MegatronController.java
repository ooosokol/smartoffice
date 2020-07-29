package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sokol.smartoffice.service.MegatronSokolServiceImpl;

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

}
