package com.example.model

enum class ProductCategory {
    WHOLE, PIECES
}

data class CashewProduct(
    val id: String,
    val name: String,
    val grade: String,
    val category: ProductCategory,
    val description: String,
    val characteristics: List<String>,
    val sizeLabel: String,
    val uses: String
)

object CashewProducts {
    val items = listOf(
        // White Whole Cashews
        CashewProduct(
            id = "w180",
            name = "White Wholes W180",
            grade = "W180",
            category = ProductCategory.WHOLE,
            description = "Known globally as the 'King of Cashews', W180 are the largest size cashew kernels available, prized for their massive size, uniform ivory color, and incredibly rich buttery flavor.",
            characteristics = listOf("Highest Premium Tier", "Very Large Size (180 seeds/lb)", "Pure Ivory White", "Distinct Luxury Crunch"),
            sizeLabel = "King / Super Jumbo",
            uses = "Elite gifting, high-end roasting, premier culinary art, luxury snacking."
        ),
        CashewProduct(
            id = "w210",
            name = "White Wholes W210",
            grade = "W210",
            category = ProductCategory.WHOLE,
            description = "Often referred to as the 'Jumbo Cashews', these whole kernels are extremely popular in festive occasions. They boast a majestic size and a wonderfully smooth texture.",
            characteristics = listOf("Jumbo Class Kernels", "Large Size (210 seeds/lb)", "Uniform Ivory Color", "Full Rich Body"),
            sizeLabel = "Jumbo",
            uses = "Festive packages, gourmet confectionery, premier baking."
        ),
        CashewProduct(
            id = "w240",
            name = "White Wholes W240",
            grade = "W240",
            category = ProductCategory.WHOLE,
            description = "Premium wholes widely preferred for bulk consumption, sweet making, and premium kernel packaging. Balances size perfectly with optimal value.",
            characteristics = listOf("Premium Whole Standard", "Medium-Large (240 seeds/lb)", "Excellent Crisp Texture", "Naturally Sweet flavor"),
            sizeLabel = "Premium",
            uses = "Traditional cashew sweets (Kaju Katli), salted nuts packaging, desserts."
        ),
        CashewProduct(
            id = "w320",
            name = "White Wholes W320",
            grade = "W320",
            category = ProductCategory.WHOLE,
            description = "The most popular and widely traded whole cashew kernel category in the world. Exceptional sweet taste, perfectly sized for family snacking and standard manufacturing.",
            characteristics = listOf("Global Favorite Grade", "Standard Wholes (320 seeds/lb)", "Clean ivory look", "Optimal sweetness"),
            sizeLabel = "Popular",
            uses = "Daily snacking, general retail packaging, nut mixes, home recipes."
        ),
        CashewProduct(
            id = "w450",
            name = "White Wholes W450",
            grade = "W450",
            category = ProductCategory.WHOLE,
            description = "Our economy whole grade. Smaller in size but identical in nutritional value, oil content, and delightful sweet taste. Excellent choice for budget-friendly retailing.",
            characteristics = listOf("Economy Whole Value", "Compact Size (450 seeds/lb)", "Excellent Crunch", "Nutritionally identical"),
            sizeLabel = "Economy Wholes",
            uses = "Budget foods, whole-kernel budget snacks, baking mixes."
        ),

        // Cashew Pieces
        CashewProduct(
            id = "jh",
            name = "Jumbo Halves",
            grade = "JH",
            category = ProductCategory.PIECES,
            description = "Premium quality whole kernels split clean horizontally into matching halves. Highly aesthetic and robust pieces that maintain a great crunch.",
            characteristics = listOf("Premium Splits", "Uniform Halves Split", "Highly popular crispiness", "No fine dust"),
            sizeLabel = "Halves (Split)",
            uses = "Decorative confectionery, food garnishing, savory mixtures, commercial roasting."
        ),
        CashewProduct(
            id = "lwp",
            name = "Large White Pieces",
            grade = "LWP",
            category = ProductCategory.PIECES,
            description = "Larger broken cashew pieces curated carefully. Sourced during separation processes, retaining immaculate taste and crispness at a highly accessible price point.",
            characteristics = listOf("Robust Pieces Grade", "Ivory White Color", "High density pieces", "Splendid bite quality"),
            sizeLabel = "Large Broken",
            uses = "Rich gravy pastes, high-grade confectionery thickeners, premium bakery toppings."
        ),
        CashewProduct(
            id = "swp",
            name = "Small White Pieces",
            grade = "SWP",
            category = ProductCategory.PIECES,
            description = "Uniformly sorted smaller white broken pieces. Extensively utilized in dairy factories and standard bakeries for adding a rich, natural creaminess.",
            characteristics = listOf("Fine Sized Pieces", "Rich white appearance", "Cost-effective premium nutting", "Nutrient-packed"),
            sizeLabel = "Small Broken",
            uses = "Ice-creams, chocolate fillings, dessert toppings, baby foods."
        ),
        CashewProduct(
            id = "sp",
            name = "Scorched Pieces",
            grade = "SP",
            category = ProductCategory.PIECES,
            description = "Cashew pieces that feature a slight scorched color from roasting, which intensifies the rich, nutty aroma. Perfect for direct savory processing.",
            characteristics = listOf("Roasted Brown Tints", "Highly aromatic profile", "Extra crispy structure", "Delectable roasted flavor"),
            sizeLabel = "Scorched Broken",
            uses = "Savory spices, dry fruit mixers, spicy Indian snack mixtures (Namkeen)."
        ),
        CashewProduct(
            id = "csp",
            name = "Cashew Splits/Pieces",
            grade = "CSP",
            category = ProductCategory.PIECES,
            description = "An excellent blend of splits and broken kernels selected specifically for standard industrial bakeries and large scale food processing, combining high value with elite flavor.",
            characteristics = listOf("Mixed Splits & Pieces", "Highly economical", "Premium Panruti flavor", "Perfect culinary thickener"),
            sizeLabel = "Splits Blend",
            uses = "Large dairy processing, daily hotel catering, rich soup stocks, budget baking."
        )
    )
}
