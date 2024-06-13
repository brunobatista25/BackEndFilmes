package com.brunao.brunao.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StatusController {

    @ApiOperation(value = "Retorna que a api esta de pé")
    @GetMapping(path = "/status")
    public String statusApi(){
        return "A aplicação está de pé";
    }

}
