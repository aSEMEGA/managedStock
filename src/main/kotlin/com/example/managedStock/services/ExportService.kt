package com.example.managedStock.services

import com.example.managedStock.dto.VenteDto
import com.example.managedStock.dto.ProductDto
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ExportService(
    private val venteService: VenteService,
    private val productService: ProductService
) {

    fun exportVentesToExcel(startDate: LocalDateTime, endDate: LocalDateTime): ByteArray {
        val ventes = venteService.getVentesByDateRange(startDate, endDate)
        
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Ventes")
        
        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("ID")
        headerRow.createCell(1).setCellValue("Date")
        headerRow.createCell(2).setCellValue("Produit ID")
        headerRow.createCell(3).setCellValue("Quantité")
        headerRow.createCell(4).setCellValue("Total")
        headerRow.createCell(5).setCellValue("Type Vente")
        
        // Fill data
        ventes.forEachIndexed { index, vente ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(vente.id.toDouble())
            row.createCell(1).setCellValue(vente.datecreationVente.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            row.createCell(2).setCellValue(vente.productId.toDouble())
            row.createCell(3).setCellValue(vente.quantite.toDouble())
            row.createCell(4).setCellValue(vente.total.toDouble())
            row.createCell(5).setCellValue(vente.TypeVente.name)
        }
        
        // Auto-size columns
        for (i in 0..5) {
            sheet.autoSizeColumn(i)
        }
        
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        
        return outputStream.toByteArray()
    }

    fun exportStockToExcel(): ByteArray {
        val products = productService.getAllProducts()
        
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Stock")
        
        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("ID")
        headerRow.createCell(1).setCellValue("Nom")
        headerRow.createCell(2).setCellValue("Quantité")
        headerRow.createCell(3).setCellValue("Prix")
        headerRow.createCell(4).setCellValue("Seuil Stock")
        headerRow.createCell(5).setCellValue("Statut")
        
        // Fill data
        products.forEachIndexed { index, product ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(product.id.toDouble())
            row.createCell(1).setCellValue(product.nom)
            row.createCell(2).setCellValue(product.quantity.toDouble())
            row.createCell(3).setCellValue(product.price.toDouble())
            row.createCell(4).setCellValue(product.seuilStock.toDouble())
            row.createCell(5).setCellValue(if (product.quantity <= product.seuilStock) "Stock Bas" else "OK")
        }
        
        // Auto-size columns
        for (i in 0..5) {
            sheet.autoSizeColumn(i)
        }
        
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        
        return outputStream.toByteArray()
    }

    fun generateVentesReport(startDate: LocalDateTime, endDate: LocalDateTime): String {
        val ventes = venteService.getVentesByDateRange(startDate, endDate)
        val totalRevenue = venteService.getTotalRevenue(startDate, endDate)
        val mostSoldProducts = venteService.getMostSoldProducts(5)
        
        val report = StringBuilder()
        report.append("=== RAPPORT DES VENTES ===\n")
        report.append("Période: ${startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}\n")
        report.append("Nombre de ventes: ${ventes.size}\n")
        report.append("Chiffre d'affaires total: $totalRevenue\n\n")
        
        report.append("=== PRODUITS LES PLUS VENDUS ===\n")
        mostSoldProducts.forEachIndexed { index, product ->
            report.append("${index + 1}. ${product["productName"]} - Quantité: ${product["totalQuantity"]} - CA: ${product["totalRevenue"]}\n")
        }
        
        return report.toString()
    }

    fun generateStockReport(): String {
        val products = productService.getAllProducts()
        val lowStockProducts = venteService.getLowStockProducts()
        
        val report = StringBuilder()
        report.append("=== RAPPORT DE STOCK ===\n")
        report.append("Nombre total de produits: ${products.size}\n")
        report.append("Produits en stock bas: ${lowStockProducts.size}\n\n")
        
        report.append("=== PRODUITS EN STOCK BAS ===\n")
        lowStockProducts.forEach { product ->
            report.append("- ${product.nom}: ${product.quantity}/${product.seuilStock}\n")
        }
        
        return report.toString()
    }
}
