package it.unicam.cs.Giftify.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String sayHello() {
        return "Ciao dal backend!";
    }
}
