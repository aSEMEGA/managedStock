package com.example.managedStock.controllers

import com.example.managedStock.services.ExportService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/export")
class ExportController(private val exportService: ExportService) {

    @GetMapping("/ventes/excel")
    fun exportVentesToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<ByteArray> {
        val excelData = exportService.exportVentesToExcel(startDate, endDate)
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        headers.setContentDispositionFormData("attachment", "ventes_${startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}_${endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}.xlsx")
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelData)
    }

    @GetMapping("/stock/excel")
    fun exportStockToExcel(): ResponseEntity<ByteArray> {
        val excelData = exportService.exportStockToExcel()
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        headers.setContentDispositionFormData("attachment", "stock_${LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}.xlsx")
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelData)
    }

    @GetMapping("/ventes/report")
    fun generateVentesReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<String> {
        val report = exportService.generateVentesReport(startDate, endDate)
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        headers.setContentDispositionFormData("attachment", "rapport_ventes_${startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}_${endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}.txt")
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(report)
    }

    @GetMapping("/stock/report")
    fun generateStockReport(): ResponseEntity<String> {
        val report = exportService.generateStockReport()
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        headers.setContentDispositionFormData("attachment", "rapport_stock_${LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))}.txt")
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(report)
    }
}
