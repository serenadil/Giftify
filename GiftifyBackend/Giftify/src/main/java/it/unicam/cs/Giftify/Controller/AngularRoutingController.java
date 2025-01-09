package it.unicam.cs.Giftify.Controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularRoutingController {

    @RequestMapping({"/community/**", "/home", })
    public String redirectToAngular() {
        return "forward:/index.html";
    }
}
