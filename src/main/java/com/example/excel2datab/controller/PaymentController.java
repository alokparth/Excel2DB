package com.example.excel2datab.controller;

import com.example.excel2datab.entity.Payment;
import com.example.excel2datab.repository.PaymentRepository;
import com.example.excel2datab.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @GetMapping("fetch")
    public ResponseEntity<Payment> getbyids(@RequestParam("PaymentId")int pid,@RequestParam("TaxnId")int tid){
        String Id= String.valueOf(pid);
        Optional<Payment>user= Optional.ofNullable(paymentRepository.findByPaymentIdAndTxnTypeId(Id, tid));
//        if(user.isPresent()){
//            return new ResponseEntity<>(user.get(),HttpStatus.FOUND);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return user.map(payment -> new ResponseEntity<>(payment, HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("fetchall")
    public ResponseEntity<List<Payment>> getall(){
        List<Payment> users = new ArrayList<>(paymentRepository.findAll());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    @DeleteMapping("delete")
    public ResponseEntity<String> deletebyids(@RequestParam("PaymentId")int pid,@RequestParam("TaxnId")int tid){
        String Id= String.valueOf(pid);
        Optional<Payment>user= Optional.ofNullable(paymentRepository.findByPaymentIdAndTxnTypeId(Id, tid));
        if(user.isPresent()){
            int Mid=user.get().getId();
            paymentRepository.deleteById(Mid);
            log.info("The data regarding Id: {} is deleted successfully",Mid);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.FOUND);
        }
        log.info("There Exist no entries Regarding Payment Id : {} and Taxn Id : {} ",Id,tid);
        return new ResponseEntity<>("Specified Data Doesn't Exist in the Table",HttpStatus.NOT_FOUND);
    }
}
