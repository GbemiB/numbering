package com.molcom.nms.authenticate;

import com.molcom.nms.security.JwtSecurityService;
import com.molcom.nms.security.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "nms/authenticate")
public class AuthenticateController {

    @Autowired
    private JwtSecurityService jwtSecurityService;

    @GetMapping("/getJwt")
    public Token authUser() throws Exception {
        return jwtSecurityService.create512JwtToken("TEST");
    }
}
