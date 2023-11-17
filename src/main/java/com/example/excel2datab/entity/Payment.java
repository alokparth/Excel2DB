package com.example.excel2datab.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Payment {
    @Id
    private Long paymentId;
    private int txnId;
    private int amount;
}
