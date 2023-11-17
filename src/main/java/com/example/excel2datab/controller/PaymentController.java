package com.example.excel2datab.controller;

import com.example.excel2datab.entity.Payment;
import com.example.excel2datab.repository.PaymentRepository;
import com.example.excel2datab.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentService paymentService;

    @PostMapping("send")
    public ResponseEntity<?> insert(@RequestParam("file")MultipartFile file){
        paymentService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Payment> selectbyid(@PathVariable Long id){
        Optional<Payment>user =paymentRepository.findById(id);
        if(user.isPresent()){
            Payment payment=user.get();
            return new ResponseEntity<Payment>(payment,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<Payment>> selectall(){
        List<Payment> ans = new ArrayList<>(paymentRepository.findAll());
        return new ResponseEntity<List<Payment>>(ans, HttpStatus.OK);
    }
}
