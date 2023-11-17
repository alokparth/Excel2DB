//package com.example.excel2datab.service;
//
//import com.example.excel2datab.entity.Payment;
//import com.example.excel2datab.repository.PaymentRepository;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ExcelUploadService {
//
//    @Autowired
//    PaymentRepository paymentRepository;
//    public List<Payment> getCustomersDataFromExcel(InputStream inputStream){
//        List<Payment> customers = new ArrayList<>();
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            int rowIndex =0;
//            for (Row row : sheet) {
//                if (rowIndex == 0) {
//                    rowIndex++;
//                    continue;
//                }
//                Iterator<Cell> cellIterator = row.iterator();
//                Cell chk=cellIterator.next();
//                int id=(int)chk.getNumericCellValue();
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
//                int cellIndex = 0;
//                Payment enthusiast = new Payment();
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    switch (cellIndex) {
//                        case 0:
//                            enthusiast.setPaymentId((int) cell.getNumericCellValue());
//                            break;
//                        case 1:
//                            enthusiast.setTxnId((int)cell.getNumericCellValue());
//                            break;
//                        default:
//                            break;
//                    }
//                    cellIndex++;
//                }
//                customers.add(enthusiast);
//            }
////            for (Row row : sheet){
////                if (rowIndex ==0){
////                    rowIndex++;
////                    continue;
////                }
////                Iterator<Cell> cellIterator = row.iterator();
////                int cellIndex = 0;
////                Enthusiasts enthusiast =new Enthusiasts();
////                while (cellIterator.hasNext()){
////                    Cell cell = cellIterator.next();
////                    switch (cellIndex){
////                        case 0 -> enthusiast.setId((int) cell.getNumericCellValue());
////                        case 1 -> enthusiast.setFirstName(cell.getStringCellValue());
////                        case 2 -> enthusiast.setLastName(cell.getStringCellValue());
////                        case 3 -> enthusiast.setHobby(cell.getStringCellValue());
////                        case 4 -> enthusiast.setProfession(Integer.valueOf(cell.getStringCellValue()));
////                        case 5 -> enthusiast.setAge((int) cell.getNumericCellValue());
////                    }
////                    cellIndex++;
////                }
////                customers.add(enthusiast);
////            }
//        } catch (IOException e) {
//            e.getStackTrace();
//        }
//        return customers;
//    }
//}
