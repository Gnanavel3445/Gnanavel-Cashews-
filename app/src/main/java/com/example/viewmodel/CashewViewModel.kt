package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.CashewDatabase
import com.example.database.DatabaseClient
import com.example.database.CashewRepository
import com.example.database.Inquiry
import com.example.database.Favorite
import com.example.model.CashewProduct
import com.example.model.CashewProducts
import com.example.model.ProductCategory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CashewViewModel(application: Application) : AndroidViewModel(application) {
    private val database: CashewDatabase = DatabaseClient.getDatabase(application)
    private val repository = CashewRepository(database.cashewDao())

    // UI Navigation State
    private val _currentTab = MutableStateFlow(0) // 0: Home, 1: Products, 2: Gallery, 3: Contact, 4: About
    val currentTab = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Theme state (Dark/Light mode)
    private val _darkThemeEnabled = MutableStateFlow<Boolean?>(null) // null means follow system
    val darkThemeEnabled = _darkThemeEnabled.asStateFlow()

    fun toggleTheme(dark: Boolean) {
        _darkThemeEnabled.value = dark
    }

    // Catalog States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null) // null = ALL
    val selectedCategory = _selectedCategory.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    // Real Favorites list from Room
    val favoritesList: StateFlow<List<Favorite>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Real Inquiries list from Room (used for checking inquiry history)
    val inquiryHistory: StateFlow<List<Inquiry>> = repository.inquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(gradeCode: String) {
        viewModelScope.launch {
            val isFav = favoritesList.value.any { it.gradeCode == gradeCode }
            if (isFav) {
                repository.removeFavorite(gradeCode)
            } else {
                repository.addFavorite(gradeCode)
            }
        }
    }

    fun isProductFavorite(gradeCode: String): Flow<Boolean> {
        return favoritesList.map { list -> list.any { it.gradeCode == gradeCode } }
    }

    // Filtered Cashew Products
    val filteredProducts: StateFlow<List<CashewProduct>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        CashewProducts.items.filter { product ->
            val matchesCategory = category == null || product.category == category
            val matchesQuery = query.isEmpty() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.grade.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.sizeLabel.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CashewProducts.items)

    // Inquiry Dialog/BottomSheet state
    private val _inquiryTargetProduct = MutableStateFlow<CashewProduct?>(null)
    val inquiryTargetProduct = _inquiryTargetProduct.asStateFlow()

    fun openInquiryForm(product: CashewProduct?) {
        _inquiryTargetProduct.value = product
    }

    // Form inputs
    val inquiryName = MutableStateFlow("")
    val inquiryEmail = MutableStateFlow("")
    val inquiryPhone = MutableStateFlow("")
    val inquiryMessage = MutableStateFlow("")

    private val _inquirySuccessMessage = MutableStateFlow<String?>(null)
    val inquirySuccessMessage = _inquirySuccessMessage.asStateFlow()

    private val _formValidationError = MutableStateFlow<String?>(null)
    val formValidationError = _formValidationError.asStateFlow()

    fun clearInquiryStatus() {
        _inquirySuccessMessage.value = null
        _formValidationError.value = null
    }

    fun submitInquiry() {
        val name = inquiryName.value.trim()
        val email = inquiryEmail.value.trim()
        val phone = inquiryPhone.value.trim()
        val grade = inquiryTargetProduct.value?.grade ?: "General Inquiry"
        val message = inquiryMessage.value.trim()

        if (name.isEmpty()) {
            _formValidationError.value = "Please enter your name"
            return
        }
        if (phone.isEmpty()) {
            _formValidationError.value = "Please enter your phone number"
            return
        }
        if (message.isEmpty()) {
            _formValidationError.value = "Please enter your message"
            return
        }

        viewModelScope.launch {
            repository.insertInquiry(
                Inquiry(
                    name = name,
                    email = email,
                    phone = phone,
                    productGrade = grade,
                    message = message
                )
            )

            _inquirySuccessMessage.value = "Thank you! Your inquiry for cashew grade $grade has been received. We will contact you soon."
            _formValidationError.value = null

            // Clear form inputs
            inquiryName.value = ""
            inquiryEmail.value = ""
            inquiryPhone.value = ""
            inquiryMessage.value = ""
            _inquiryTargetProduct.value = null
        }
    }
}
