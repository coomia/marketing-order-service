package com.meiye.controller.part;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/8/8 0008.
 */
@Controller
@RequestMapping(value = "/web/part")
public class PartController {

    @RequestMapping("/loadBrandTypes")
    public String loadBrandTypes(){
        return "/part/partBrandType";
    }
}
