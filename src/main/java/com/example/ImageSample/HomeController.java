package com.example.ImageSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ImageDataRepository imageDataRepository;

    @GetMapping("")
    public String home(Model model){
        List<ImageData> imageDataList = imageDataRepository.findAll();

        model.addAttribute("imageData",imageDataList);

        return "test/upload";
    }
}
