package com.example

import com.example.data.model.CartItem
import com.example.data.model.Product
import com.example.data.model.StockStatus
import com.example.ui.viewmodel.WarungViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRupiahFormatting() {
        val formatted = WarungViewModel.formatRupiah(1250000.0)
        assertTrue(formatted.contains("1.250.000") || formatted.contains("1,250,000") || formatted.contains("1250000"))
    }

    @Test
    fun testProductStockStatus() {
        val criticalProduct = Product(
            code = "BRG-008",
            name = "Telur Ayam 1kg",
            category = "Sembako",
            price = 28000.0,
            costPrice = 24000.0,
            stock = 2,
            minStock = 5
        )
        assertEquals(StockStatus.CRITICAL, criticalProduct.stockStatus)

        val lowProduct = Product(
            code = "BRG-012",
            name = "Kecap Manis",
            category = "Bumbu",
            price = 18000.0,
            costPrice = 14000.0,
            stock = 4,
            minStock = 6
        )
        assertEquals(StockStatus.LOW, lowProduct.stockStatus)

        val safeProduct = Product(
            code = "BRG-001",
            name = "Beras Premium",
            category = "Sembako",
            price = 68000.0,
            costPrice = 58000.0,
            stock = 14,
            minStock = 5
        )
        assertEquals(StockStatus.SAFE, safeProduct.stockStatus)
    }

    @Test
    fun testCartSubtotalAndProfit() {
        val product = Product(
            code = "BRG-001",
            name = "Beras Premium 5kg",
            category = "Sembako",
            price = 68000.0,
            costPrice = 58000.0,
            stock = 14
        )
        val cartItem = CartItem(product = product, quantity = 2)
        assertEquals(136000.0, cartItem.subtotal, 0.01)
        assertEquals(20000.0, cartItem.profit, 0.01)
    }
}
