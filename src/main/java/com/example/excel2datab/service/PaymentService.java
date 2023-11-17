package com.example.excel2datab.service;

import com.example.excel2datab.entity.Payment;
import com.example.excel2datab.repository.PaymentRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    public boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public void save(MultipartFile file){

        if(isValidExcelFile(file)){
            try {
                getCustomersDataFromExcel(file.getInputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
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
                Iterator<Cell> cellIterator = row.iterator();
//                Cell chk=row.getCell(0);
//                Long cid= (long) chk.getNumericCellValue();
//                Optional<Payment>user1=paymentRepository.findById(cid);
//                if(user1.isPresent()){
//                    Payment part= user1.get();
//                    chk= row.getCell(1);
//                    if((int)chk.getNumericCellValue()==part.getTxnId()){
//                        paymentRepository.delete(part);
//                        chk= row.getCell(2);
//                        int amount=(int)chk.getNumericCellValue();
//                        amount=amount+part.getAmount();
//                        part.setAmount(amount);
//                        paymentRepository.save(part);
//                        break;
//                    }
//                }
//                Cell chk=cellIterator.next();
//                Long id= (long) (int) chk.getNumericCellValue();
//                Optional<Payment> user=paymentRepository.findById(id);
//                if(user.isPresent()){
//                    Payment payment= user.get();
//                    int k=payment.getTxnId();
//                    k+=(int)chk.getNumericCellValue();
//                    paymentRepository.deleteById(id);
//                    payment.setTxnId(k);
//                    customers.add(payment);
//                    continue;
//                }
                int cellIndex = 0;
                Payment enthusiast = new Payment();
                Payment copied=new Payment();
                Boolean chkk=false;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0:
                            long id=(long) (int) cell.getNumericCellValue();
                            Optional<Payment>user=paymentRepository.findById(id);
                            if(user.isPresent()){
                                chkk=true;
                                copied=user.get();
                            }
                            enthusiast.setPaymentId(id);
                            break;
                        case 1:
                            int tid=(int)cell.getNumericCellValue();
                            if(chkk==true){
                                int taxn= copied.getTxnId();
                                if(taxn==tid){
                                    //joo
                                    chkk=true;
                                }
                                else chkk=false;
                            }
                            enthusiast.setTxnId(tid);
                            break;
                        case 2:
                            int amount=(int)cell.getNumericCellValue();
                            if(chkk==true){
                                paymentRepository.delete(copied);
                                amount=amount+copied.getAmount();
                            }
                            enthusiast.setAmount(amount);
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                paymentRepository.save(enthusiast);
            }
//            for (Row row : sheet){
//                if (rowIndex ==0){
//                    rowIndex++;
//                    continue;
//                }
//                Iterator<Cell> cellIterator = row.iterator();
//                int cellIndex = 0;
//                Enthusiasts enthusiast =new Enthusiasts();
//                while (cellIterator.hasNext()){
//                    Cell cell = cellIterator.next();
//                    switch (cellIndex){
//                        case 0 -> enthusiast.setId((int) cell.getNumericCellValue());
//                        case 1 -> enthusiast.setFirstName(cell.getStringCellValue());
//                        case 2 -> enthusiast.setLastName(cell.getStringCellValue());
//                        case 3 -> enthusiast.setHobby(cell.getStringCellValue());
//                        case 4 -> enthusiast.setProfession(Integer.valueOf(cell.getStringCellValue()));
//                        case 5 -> enthusiast.setAge((int) cell.getNumericCellValue());
//                    }
//                    cellIndex++;
//                }
//                customers.add(enthusiast);
//            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return;
    }
}
