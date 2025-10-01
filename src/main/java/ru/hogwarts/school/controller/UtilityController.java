package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utilities")
public class UtilityController {

    @GetMapping("/fast-sum")
    public ResponseEntity<Integer> fastSum() {
        int n = 1_000_000;
        int result = n * (n + 1) / 2;
        return ResponseEntity.ok(result);
    }
}