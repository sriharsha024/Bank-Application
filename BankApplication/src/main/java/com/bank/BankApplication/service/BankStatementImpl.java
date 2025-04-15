package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.EmailDetails;
import com.bank.BankApplication.entity.Transaction;
import com.bank.BankApplication.entity.User;
import com.bank.BankApplication.repo.TransactionRepo;
import com.bank.BankApplication.repo.UserRepo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BankStatementImpl implements BankStatement {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    private static final String FILE = "D:\\MyStatement.pdf";
    @Autowired
    private EmailService emailService;

    @Override
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        Optional<User> optionalUser = userRepo.findByAccountNumber(accountNumber);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Account number does not exist.");
        }
        User user = optionalUser.get();
        LocalDate accountCreatedDate = user.getCreatedAt().toLocalDate();

        if (start.isBefore(accountCreatedDate)) {
            throw new IllegalArgumentException("Start date cannot be before account creation date.");
        }

        List<Transaction> transactionList = transactionRepo.findByAccountNumberAndTransactionDateandTimeBetween(
                accountNumber, start.atStartOfDay(), end.plusDays(1).atStartOfDay());

        String customerName = user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName();

        Document document = new Document(PageSize.A4, 40, 40, 30, 30);
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        // Font setup
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // Bank Info Header
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("THE BANK APP", titleFont));
        bankName.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankName.setBorder(Rectangle.NO_BORDER);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(15f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Opp AMB Mall, Kondapur, Hyderabad", normalFont));
        bankAddress.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankAddress.setBorder(Rectangle.NO_BORDER);
        bankAddress.setPaddingBottom(10f);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);
        document.add(bankInfoTable);
        document.add(Chunk.NEWLINE);

        // Statement Info
        PdfPTable statementInfo = new PdfPTable(2);
        statementInfo.setWidthPercentage(100);

        statementInfo.addCell(getLabelCell("Start Date:", headerFont));
        statementInfo.addCell(getValueCell(startDate, normalFont));

        statementInfo.addCell(getLabelCell("End Date:", headerFont));
        statementInfo.addCell(getValueCell(endDate, normalFont));

        statementInfo.addCell(getLabelCell("Customer Name:", headerFont));
        statementInfo.addCell(getValueCell(customerName, normalFont));

        statementInfo.addCell(getLabelCell("Account Number:", headerFont));
        statementInfo.addCell(getValueCell(accountNumber, normalFont));

        statementInfo.addCell(getLabelCell("Address:", headerFont));
        statementInfo.addCell(getValueCell(user.getAddress(), normalFont));

        document.add(statementInfo);
        document.add(Chunk.NEWLINE);

        // Transaction Table
        PdfPTable transactionsTable = new PdfPTable(4);
        transactionsTable.setWidthPercentage(100);
        transactionsTable.setSpacingBefore(10f);
        transactionsTable.setWidths(new float[]{3, 4, 2, 3});

        addTableHeader(transactionsTable, "DATE", headerFont);
        addTableHeader(transactionsTable, "TRANSACTION TYPE", headerFont);
        addTableHeader(transactionsTable, "AMOUNT", headerFont);
        addTableHeader(transactionsTable, "STATUS", headerFont);

        for (Transaction transaction : transactionList) {
            transactionsTable.addCell(new Phrase(transaction.getTransactionDateandTime().toString(), normalFont));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType(), normalFont));
            transactionsTable.addCell(new Phrase(String.valueOf(transaction.getAmount()), normalFont));
            transactionsTable.addCell(new Phrase(transaction.getStatus(), normalFont));
        }

        document.add(transactionsTable);
        document.close();

        EmailDetails emailDetails =EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messagebody("Your transaction pdf from "+startDate+" to "+endDate)
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
    private PdfPCell getLabelCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell getValueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private void addTableHeader(PdfPTable table, String columnTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(columnTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(8f);
        table.addCell(header);
    }


}
