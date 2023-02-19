package edu.eci.proyect.controller.health;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

   @RequestMapping("/health") public String checkAPI(){
       return "<h1>The API is working ok!</h1>";
   }
}