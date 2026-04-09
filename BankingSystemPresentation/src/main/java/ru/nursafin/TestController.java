package ru.nursafin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nursafin.money.Money;

import java.math.BigDecimal;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test() {
        return "ok";
    }

    @GetMapping(" EasterEgg")
    public Money easterEgg() {
        return new Money(new BigDecimal(100500));
    }

    @GetMapping("/")
    public String home() {
        return "Everything that kills me makes me feel alive";
    }
}
