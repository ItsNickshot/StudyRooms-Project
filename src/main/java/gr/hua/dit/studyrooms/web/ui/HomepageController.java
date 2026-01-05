package gr.hua.dit.studyrooms.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String home() {
        return "homepage"; // Καλεί το homepage.html
    }
}
