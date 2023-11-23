package com.example.excel2datab.service;

import com.example.excel2datab.entity.Payment;
import com.example.excel2datab.repository.PaymentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    public boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public void save(MultipartFile file){

        if(isValidExcelFile(file)){
            log.info("The Data Submitted is of type Excel(.xlsx)");
            try {
                getCustomersDataFromExcel(file.getInputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
            return;
        }
        log.error("The Data is not in the Excel Format");
    }

    public void getCustomersDataFromExcel(InputStream inputStream){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex =0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Cell cell = row.getCell(0);
                Integer tId= (int) cell.getNumericCellValue();
                cell = row.getCell(1);
                String pId= String.valueOf((int)cell.getNumericCellValue());
                cell = row.getCell(2);
                BigDecimal amt= BigDecimal.valueOf(cell.getNumericCellValue());
                cell = row.getCell(3);
                String chrgname = cell.getStringCellValue();
//                Optional<Payment> ns= paymentRepository.findById(pId);
//                if(ns.isPresent()){
//                    Payment nsw=ns.get();
//                    BigDecimal amount=nsw.getAmount();
//                    Integer chk= nsw.getTxnTypeId();
//                    if(chk.equals(tId)){
//                        amt=amt.add(amount);
//                        paymentRepository.delete(nsw);
//                    }
//                }
                Optional<Payment> ns= Optional.ofNullable(paymentRepository.findByPaymentIdAndTxnTypeId(pId, tId));
                if(ns.isPresent()){
                    Payment nsw=ns.get();
                    BigDecimal amount=nsw.getAmount();
                    amt=amt.add(amount);
                    nsw.setAmount(amt);
                    paymentRepository.save(nsw);
                    log.info("Data Updated Successfully");
                }
                else{
                    Payment user =Payment.builder().paymentId(pId).txnTypeId(tId).chargeName(chrgname).amount(amt).build();
                    paymentRepository.save(user);
                    log.info("Data Saved Successfully");
                }

//                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
//                        Cell cell = row.getCell(j);
//                        String paymentId;
//                        Integer txnTypeId;
//                        String chargeName;
//                        BigDecimal amount;
//                        dataTable[i][j] = cell.getStringCellValue();
//                    }
//                Iterator<Cell> cellIterator = row.iterator();
////               int noOfRows = Sheet.getLastRowNum() + 1;
////                int noOfColumns = Sheet.getRow(0).getLastCellNum();
////                String[][] dataTable = new String[noOfRows][noOfColumns];
////
////                for (int i = Sheet.getFirstRowNum(); i < Sheet.getLastRowNum() + 1; i++) {
////                    Row row = Sheet.getRow(i);
////                    for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
////                        Cell cell = row.getCell(j);
////                        dataTable[i][j] = cell.getStringCellValue();
////                    }
////                }
//                int cellIndex = 0;
//                Payment enthusiast = new Payment();
//                Payment copied=new Payment();
//                Boolean chkk=false;
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    switch (cellIndex) {
//                        case 0:
//                            long id=(long) (int) cell.getNumericCellValue();
//                            Optional<Payment>user=paymentRepository.findById(id);
//                            if(user.isPresent()){
//                                chkk=true;
//                                copied=user.get();
//                            }
//                            enthusiast.setPaymentId(id);
//                            break;
//                        case 1:
//                            int tid=(int)cell.getNumericCellValue();
//                            if(chkk==true){
//                                int taxn= copied.getTxnId();
//                                if(taxn==tid){
//                                    //joo
//                                    chkk=true;
//                                }
//                                else chkk=false;
//                            }
//                            enthusiast.setTxnId(tid);
//                            break;
//                        case 2:
//                            int amount=(int)cell.getNumericCellValue();
//                            if(chkk==true){
//                                paymentRepository.delete(copied);
//                                amount=amount+copied.getAmount();
//                            }
//                            enthusiast.setAmount(amount);
//                            break;
//                        default:
//                            break;
//                    }
//                    cellIndex++;
//                }
//                paymentRepository.save(enthusiast);
            }
        } catch (IOException e) {
            log.error("Error in saving the Data check the Rows and Columns");
            e.getStackTrace();
        }
        return;
    }
}
