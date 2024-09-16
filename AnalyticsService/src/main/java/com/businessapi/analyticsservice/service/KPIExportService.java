package com.businessapi.analyticsservice.service;

import org.springframework.stereotype.Service;

@Service
public class KPIExportService {

//    public void exportToExcel(HttpServletResponse response, List<KPI> kpis) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("KPI Data");
//
//        Row headerRow = sheet.createRow(0);
//        Cell headerCell = headerRow.createCell(0);
//        headerCell.setCellValue("KPI Name");
//
//        headerCell = headerRow.createCell(1);
//        headerCell.setCellValue("KPI Value");
//
//        int rowCount = 1;
//        for (KPI kpi : kpis) {
//            Row row = sheet.createRow(rowCount++);
//            row.createCell(0).setCellValue(kpi.getName());
//            row.createCell(1).setCellValue(kpi.getValue());
//        }
//
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment; filename=kpi_data.xlsx");
//
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//
//        outputStream.close();
//    }
//
//    public void exportToPDF(HttpServletResponse response, List<KPI> kpis) throws IOException, com.itextpdf.text.DocumentException {
//        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//        PdfWriter.getInstance(document, response.getOutputStream());
//
//        document.open();
//        document.add(new com.itextpdf.text.Paragraph("KPI Report"));
//
//        PdfPTable table = new PdfPTable(2);
//        table.addCell("KPI Name");
//        table.addCell("KPI Value");
//
//        for (KPI kpi : kpis) {
//            table.addCell(kpi.getName());
//            table.addCell(kpi.getValue().toString());
//        }
//
//        document.add(table);
//        document.close();
//
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=kpi_report.pdf");
//    }
}
