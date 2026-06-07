package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.model.CashewProduct
import com.example.model.CashewProducts
import com.example.model.ProductCategory
import com.example.ui.theme.*
import com.example.viewmodel.CashewViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CashewAppMain(
    viewModel: CashewViewModel,
    modifier: Modifier = Modifier
) {
    var showSplash by remember { mutableStateOf(true) }
    val darkThemeState by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()
    val isSystemDark = isSystemInDarkTheme()

    val useDarkTheme = when (darkThemeState) {
        null -> isSystemDark
        else -> darkThemeState == true
    }

    MyApplicationTheme(darkTheme = useDarkTheme) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = showSplash,
                transitionSpec = {
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                },
                label = "MainCrossfade"
            ) { isSplash ->
                if (isSplash) {
                    SplashScreen(
                        onSplashFinished = { showSplash = false }
                    )
                } else {
                    AppHomeScreenLayout(viewModel = viewModel, useDarkTheme = useDarkTheme)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "SplashRings")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RingRotation"
    )

    var startAnimation by remember { mutableStateOf(false) }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200, easing = EaseInOutCirc),
        label = "AlphaTransition"
    )
    val scaleFactor by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(1200, easing = EaseOutBack),
        label = "ScaleTransition"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200)
        onSplashFinished()
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ForestGreenDeep,
            ForestGreenMedium,
            Color(0xFF0F2B15)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background cashew shapes draw
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawCircle(
                color = BrightGold.copy(alpha = 0.04f),
                radius = 350f,
                center = Offset(width * 0.1f, height * 0.2f)
            )
            drawCircle(
                color = SoftGold.copy(alpha = 0.05f),
                radius = 500f,
                center = Offset(width * 0.9f, height * 0.8f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .graphicsLayer(
                    alpha = animatedAlpha,
                    scaleX = scaleFactor,
                    scaleY = scaleFactor
                )
        ) {
            // Animated Corporate Branding Frame
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(BrightGold, SoftGold, ForestGreenDeep, BrightGold)
                            ),
                            style = Stroke(width = 4.dp.toPx()),
                            alpha = 0.8f
                        )
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cashew_logo),
                    contentDescription = "GnanavelCashews Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "GnanavelCashews",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.5.sp
                ),
                color = BrightGold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                modifier = Modifier.width(60.dp),
                thickness = 2.dp,
                color = SoftGold.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Premium Quality Cashew Nuts Supplier",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp
                ),
                color = WarmCream.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color = BrightGold,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Panruti, Tamil Nadu • India",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                ),
                color = SoftGold.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AppHomeScreenLayout(
    viewModel: CashewViewModel,
    useDarkTheme: Boolean
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val inquiryTargetProduct by viewModel.inquiryTargetProduct.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .shadow(16.dp)
                    .testTag("bottom_nav_menu"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    Triple("Home", Icons.Default.Home, Icons.Outlined.Home),
                    Triple("Products", Icons.Default.ShoppingCart, Icons.Outlined.ShoppingCart),
                    Triple("Gallery", Icons.Default.PhotoLibrary, Icons.Outlined.PhotoLibrary),
                    Triple("Contact", Icons.Default.ContactPhone, Icons.Outlined.ContactPhone),
                    Triple("About", Icons.Default.Info, Icons.Outlined.Info)
                )

                items.forEachIndexed { index, item ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.second else item.third,
                                contentDescription = item.first,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = item.first,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = if (useDarkTheme) ForestGreenMedium else SageGreen
                        )
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> if (targetState > initialState) fullWidth else -fullWidth },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeIn(animationSpec = tween(220))) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { fullWidth -> if (targetState > initialState) -fullWidth else fullWidth },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
                    ) + fadeOut(animationSpec = tween(220)))
                },
                label = "TabTransition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> HomeScreenSpace(viewModel = viewModel)
                    1 -> ProductsCatalogSpace(viewModel = viewModel)
                    2 -> GallerySpaceLayout()
                    3 -> ContactSpaceLayout(viewModel = viewModel)
                    4 -> AboutCompanySpace(viewModel = viewModel)
                }
            }

            // Global inquiry form popup
            if (inquiryTargetProduct != null) {
                InquiryFormDialog(
                    product = inquiryTargetProduct!!,
                    onDismiss = { viewModel.openInquiryForm(null) },
                    viewModel = viewModel
                )
            }
        }
    }
}

// ----------------------------------------------------
// TABS LAYOUTS
// ----------------------------------------------------

@Composable
fun HomeScreenSpace(viewModel: CashewViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Welcome Header Profile Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background artistic graphic
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        color = BrightGold.copy(alpha = 0.15f),
                        radius = w * 0.3f,
                        center = Offset(w * 0.95f, h * 0.1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .border(2.dp, BrightGold, CircleShape)
                            .background(WarmCream),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cashew_logo),
                            contentDescription = "GnanavelCashews Profile Icon",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "GnanavelCashews",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrightGold,
                                fontFamily = FontFamily.Serif
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Premium Quality Cashew Nuts Supplier",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = WarmCream.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Hero Image Carousel Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(4.dp)
                .background(ForestGreenDeep),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.cashew_banner),
                contentDescription = "Premium Quality Cashews Showcase",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dynamic Gradient Mesh Overlay for Tagline Readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrightGold,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = " 100% EXPORT QUALITY ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDeep
                        ),
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }

                Text(
                    text = "Pure Panruti Cashew Selections",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
                Text(
                    text = "Supplying elite wholeness & pieces globally.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = WarmCream.copy(alpha = 0.8f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Command Navigation Actions
        Text(
            text = "Quick Navigation",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickNavCard(
                title = "View Catalog",
                icon = Icons.Default.MenuBook,
                subtitle = "Explore Grades",
                modifier = Modifier.weight(1f),
                color = ForestGreenMedium,
                onClick = { viewModel.selectTab(1) }
            )

            QuickNavCard(
                title = "Reach Us",
                icon = Icons.Default.Directions,
                subtitle = "Google Maps",
                modifier = Modifier.weight(1f),
                color = BrightGold,
                onClick = { viewModel.selectTab(3) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Featured Grades Segment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Featured Products",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(
                onClick = { viewModel.selectTab(1) },
                colors = ButtonDefaults.textButtonColors(contentColor = BrightGold)
            ) {
                Text("See All Products", fontWeight = FontWeight.SemiBold)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to Catalog icon",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Horizontal Row of Featured Grades
        val featuredList = CashewProducts.items.take(3)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(featuredList) { item ->
                FeaturedGridCard(product = item, viewModel = viewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Trust Badges Grid Segment
        Text(
            text = "Why GnanavelCashews?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TrustTile(
                    title = "Premium Quality Standards",
                    desc = "Crispy, double-peeled pure white kernels strictly graded without dust.",
                    icon = Icons.Default.Verified
                )
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                TrustTile(
                    title = "Famous Panruti Heritage",
                    desc = "Directly sourced from our processing core in the Cashew Capital Panruti.",
                    icon = Icons.Default.Landscape
                )
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                TrustTile(
                    title = "Eco-Inert Packaging",
                    desc = "Vacuum seated with food safe carbon/nitrogen mixtures keeping unmatched freshness.",
                    icon = Icons.Default.Inventory2
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Share App Feature Row Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Share GnanavelCashews",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Introduce premium cashew kernels to wholesalers & retailers.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "GnanavelCashews Premium Kernels")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Indulge in premium cashew kernels (W180, WH Splits, etc.) from GnanavelCashews! Ph: +91 8111065196. Google Maps: https://maps.app.goo.gl/JvtawJBVk6nspxeM9"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share GnanavelCashews via:"))
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .testTag("share_app_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share symbol",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun QuickNavCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    subtitle: String,
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun TrustTile(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(ForestGreenMedium.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestGreenMedium,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = ForestGreenDeep
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun FeaturedGridCard(
    product: CashewProduct,
    viewModel: CashewViewModel
) {
    val favorites by viewModel.favoritesList.collectAsStateWithLifecycle()
    val isFav = favorites.any { it.gradeCode == product.id }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Product grade emblem block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(SageGreen.copy(alpha = 0.3f), VeryLightGreenbg)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = product.grade,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = ForestGreenMedium,
                            fontFamily = FontFamily.Serif
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Column {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = product.sizeLabel,
                        fontSize = 11.sp,
                        color = BrightGold,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = { viewModel.openInquiryForm(product) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenMedium)
                ) {
                    Text("Inquire", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Small Heart Icon
            IconButton(
                onClick = { viewModel.toggleFavorite(product.id) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like icon",
                    tint = if (isFav) Color.Red else Color.LightGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ----------------------------------------------------
// PRODUCT CATALOG SPACE
// ----------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductsCatalogSpace(viewModel: CashewViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val favorites by viewModel.favoritesList.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Cashew Catalog",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDeep,
                        fontFamily = FontFamily.Serif
                    )
                )
                Text(
                    text = "Explore sorted Premium wholesale grades",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Favorites quick count badge
            BadgeBox(count = favorites.size)
        }

        // Expanded Modern Search Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("product_search_input"),
            placeholder = { Text("Search by Grade, Size, or Category...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search search icon") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text")
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ForestGreenMedium,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
        )

        // Grade Segment Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipLayout(
                label = "All Grades (${CashewProducts.items.size})",
                isSelected = selectedCategory == null,
                onClick = { viewModel.selectCategory(null) }
            )
            FilterChipLayout(
                label = "Wholes (${CashewProducts.items.count { it.category == ProductCategory.WHOLE }})",
                isSelected = selectedCategory == ProductCategory.WHOLE,
                onClick = { viewModel.selectCategory(ProductCategory.WHOLE) }
            )
            FilterChipLayout(
                label = "Pieces (${CashewProducts.items.count { it.category == ProductCategory.PIECES }})",
                isSelected = selectedCategory == ProductCategory.PIECES,
                onClick = { viewModel.selectCategory(ProductCategory.PIECES) }
            )
        }

        if (filteredProducts.isEmpty()) {
            EmptySearchPlaceholder()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val isFav = favorites.any { it.gradeCode == product.id }
                    FullProductCard(
                        product = product,
                        isFav = isFav,
                        onFavoriteToggle = { viewModel.toggleFavorite(product.id) },
                        onInquire = { viewModel.openInquiryForm(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChipLayout(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = if (isSelected) ForestGreenDeep else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (isSelected) ForestGreenDeep else Color.LightGray),
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) BrightGold else Color.Gray,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun BadgeBox(count: Int) {
    Box(
        modifier = Modifier
            .size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "My Favs icon",
            tint = if (count > 0) Color.Red else Color.LightGray,
            modifier = Modifier.size(28.dp)
        )

        if (count > 0) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(BrightGold, CircleShape)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = ForestGreenDeep,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun EmptySearchPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = "Search off feedback",
            tint = Color.LightGray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Cashew Grades Found",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try clearing your search query or looking for alternate specifications (e.g. wholes, splits).",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FullProductCard(
    product: CashewProduct,
    isFav: Boolean,
    onFavoriteToggle: () -> Unit,
    onInquire: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Visual Emblem Sidebar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.Top)
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(SageGreen.copy(alpha = 0.4f), WarmCream)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = product.grade,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = ForestGreenDeep,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (product.category == ProductCategory.WHOLE) ForestGreenMedium.copy(alpha = 0.1f) else BrightGold.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = product.category.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (product.category == ProductCategory.WHOLE) ForestGreenMedium else BrightGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Main Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = ForestGreenDeep
                        )
                        Text(
                            text = "Grade: ${product.grade} • ${product.sizeLabel}",
                            fontSize = 12.sp,
                            color = BrightGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Favorite Button icon
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .offset(y = (-6).dp)
                            .size(36.dp)
                            .testTag("fav_product_${product.id}")
                    ) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite toggle",
                            tint = if (isFav) Color.Red else Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Characteristics bullet blocks
                Text(
                    text = "Key Characteristics:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                product.characteristics.forEach { trait ->
                    Row(
                        modifier = Modifier.padding(bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(BrightGold, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = trait,
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Divider(color = Color.LightGray.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(10.dp))

                // Dynamic Action buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // WhatsApp contact button
                    Button(
                        onClick = {
                            val msg = "Hello GnanavelCashews! I would like to inquire about the pricing and availability details for cashew grade '${product.grade}' (${product.name})."
                            val encodedMsg = Uri.encode(msg)
                            val url = "https://api.whatsapp.com/send?phone=+918111065196&text=$encodedMsg"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (ex: Exception) {
                                Toast.makeText(context, "WhatsApp is not installed. Dialing +91 8111065196 instead.", Toast.LENGTH_SHORT).show()
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918111065196"))
                                context.startActivity(dialIntent)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_call), // Fallback or native call icon representation
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    // Native Inquiry form button
                    Button(
                        onClick = onInquire,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDeep),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Inquiry", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// SYSTEM INQUIRY CUSTOM DIALOG
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryFormDialog(
    product: CashewProduct,
    onDismiss: () -> Unit,
    viewModel: CashewViewModel
) {
    val name by viewModel.inquiryName.collectAsStateWithLifecycle()
    val email by viewModel.inquiryEmail.collectAsStateWithLifecycle()
    val phone by viewModel.inquiryPhone.collectAsStateWithLifecycle()
    val message by viewModel.inquiryMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.inquirySuccessMessage.collectAsStateWithLifecycle()
    val validationError by viewModel.formValidationError.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(product) {
        viewModel.clearInquiryStatus()
        viewModel.inquiryMessage.value = "Hello GnanavelCashews, I want to obtain active container wholesale quote for ${product.grade} whole cashews."
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("inquiry_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header Panel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Commercial Inquiry",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = ForestGreenDeep
                        )
                        Text(
                            text = "Grade: ${product.grade} Selection",
                            color = BrightGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close inquiry icon")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (successMessage != null) {
                    // Success display Screen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SageGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = ForestGreenMedium,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = successMessage!!,
                                fontSize = 13.sp,
                                color = ForestGreenDeep,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onDismiss,
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenMedium)
                            ) {
                                Text("Back to Catalog")
                            }
                        }
                    }
                } else {
                    // Actual inputs and status logs
                    if (validationError != null) {
                        Text(
                            text = validationError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Product brief tag banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = ForestGreenMedium.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Product: ${product.name} whole premium grains.\nGraded directly from Middle Sathipattu.",
                            fontSize = 11.sp,
                            color = ForestGreenMedium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.inquiryName.value = it },
                        modifier = Modifier.fillMaxWidth().testTag("inquiry_name_input"),
                        label = { Text("Your Name *") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenMedium,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.inquiryEmail.value = it },
                        modifier = Modifier.fillMaxWidth().testTag("inquiry_email_input"),
                        label = { Text("Your Email (Optional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenMedium,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { viewModel.inquiryPhone.value = it },
                        modifier = Modifier.fillMaxWidth().testTag("inquiry_phone_input"),
                        label = { Text("WhatsApp Contact No *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenMedium,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { viewModel.inquiryMessage.value = it },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("inquiry_message_input"),
                        label = { Text("Inquiry Detail *") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenMedium,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss, modifier = Modifier.padding(end = 8.dp)) {
                            Text("Cancel", color = Color.Gray)
                        }

                        Button(
                            onClick = { viewModel.submitInquiry() },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDeep),
                            modifier = Modifier.testTag("inquiry_submit_button")
                        ) {
                            Text("Submit Inquiry")
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// GALLERY WALKTHROUGH SPACE (CUSTOM ILLUSTRATIVE STAGES)
// ----------------------------------------------------

data class GalleryItem(
    val title: String,
    val description: String,
    val stage: String,
    val baseColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GallerySpaceLayout() {
    val items = listOf(
        GalleryItem(
            stage = "Stage 01",
            title = "Panruti Orchards Harvesting",
            description = "Sourcing pristine raw cashew apples directly from our red-soil forest plantations of Cuddalore district under uniform sunshine levels.",
            baseColor = Color(0xFF2E7D32)
        ),
        GalleryItem(
            stage = "Stage 02",
            title = "Scientific Steam Cooking",
            description = "Softening raw hard shells inside computer-metered steam boiler plants, which preserves critical nutrients, flavor oils, and pure ivory colors.",
            baseColor = Color(0xFFC5A01A)
        ),
        GalleryItem(
            stage = "Stage 03",
            title = "Pristine Peeling & Drying",
            description = "Precision drying and manual fine peeling of the kernel testa layer, ensuring no outer visual scratches or core breaking structure.",
            baseColor = Color(0xFFE5BA73)
        ),
        GalleryItem(
            stage = "Stage 04",
            title = "Grading & Quality Check",
            description = "Visual sorting kernels through certified wholes (W180, W210) & piece divisions under extreme dustless, sanitized sorting hubs.",
            baseColor = Color(0xFF2D6A4F)
        ),
        GalleryItem(
            stage = "Stage 05",
            title = "Inert Gas Fresh Packaging",
            description = "Packaging kernels safely with food-grade carbon dioxide or inert nitrogen flush blocks, ensuring crispiness remains locked for exports.",
            baseColor = Color(0xFF133B1C)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Processing Gallery",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDeep,
                    fontFamily = FontFamily.Serif
                )
            )
            Text(
                text = "Farm-To-Fork cashew shell processing inside Panruti core",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Custom drawn illustrated backdrop
                        Canvas(modifier = Modifier.matchParentSize()) {
                            val w = size.width
                            val h = size.height
                            // Draw sweeping circular gradients on backing card
                            drawCircle(
                                color = item.baseColor.copy(alpha = 0.06f),
                                radius = w * 0.35f,
                                center = Offset(w * 0.85f, h * 0.5f)
                            )
                        }

                        // Display Details
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 16.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = item.baseColor.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = item.stage,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = item.baseColor
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = ForestGreenDeep
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    lineHeight = 16.sp
                                )
                            }

                            // Dynamic Symbol illustration representation
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(item.baseColor.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val stageVector = when (item.stage) {
                                    "Stage 01" -> Icons.Default.Park
                                    "Stage 02" -> Icons.Default.LocalFireDepartment
                                    "Stage 03" -> Icons.Default.FilterAlt
                                    "Stage 04" -> Icons.Default.WorkspacePremium
                                    else -> Icons.Default.Inventory
                                }
                                Icon(
                                    imageVector = stageVector,
                                    contentDescription = null,
                                    tint = item.baseColor,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CONTACT AND LOCATION SPACE
// ----------------------------------------------------

@Composable
fun ContactSpaceLayout(viewModel: CashewViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val businessPhone = "+918111065196"
    val businessEmail = "gnanavelvel121@gmail.com"
    val businessAddress = "Erulakuppam Road, Middle Sathipattu, Panruti Taluk – 607106, Cuddalore District, Tamil Nadu, India"
    val googleMapsLink = "https://maps.app.goo.gl/JvtawJBVk6nspxeM9"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Contact Desk",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDeep,
                    fontFamily = FontFamily.Serif
                )
            )
            Text(
                text = "Instant communication pathways to our processing core",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Contact details layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Phone block
                ContactLineItem(
                    title = "Hotline Phone",
                    subtitle = businessPhone,
                    icon = Icons.Default.Phone,
                    onClickAction = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$businessPhone"))
                        context.startActivity(intent)
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

                // Email block
                ContactLineItem(
                    title = "Corporate Email",
                    subtitle = businessEmail,
                    icon = Icons.Default.Email,
                    onClickAction = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$businessEmail")).apply {
                            putExtra(Intent.EXTRA_SUBJECT, "Inquiry for premium cashew grades: GnanavelCashews")
                        }
                        context.startActivity(intent)
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

                // Address block
                ContactLineItem(
                    title = "Physical Office & Processing Plant",
                    subtitle = businessAddress,
                    icon = Icons.Default.Place,
                    onClickAction = null
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Copy buttons and WhatsApp Desk Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("GnanavelCashews Info", "Phone: $businessPhone\nEmail: $businessEmail\nAddress: $businessAddress")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Contact Information copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenMedium)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy symbols", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Info", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val text = "Hello GnanavelCashews! I would like to inquire regarding high volume wholesale orders."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$businessPhone&text=${Uri.encode(text)}"))
                            try {
                                context.startActivity(intent)
                            } catch (ex: Exception) {
                                Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp icon representation", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Location Space (Google Maps Intent Desk)
        Text(
            text = "GPS Geolocation Map",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Custom drawn highly descriptive map locator box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(SageGreen.copy(alpha = 0.4f), WarmCream)
                            )
                        )
                        .border(1.dp, ForestGreenMedium.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Directions locator symbol",
                            tint = ForestGreenMedium,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Processing Center: Middle Sathipattu",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = ForestGreenDeep
                        )
                        Text(
                            text = "Panruti Taluk, Cuddalore district - Tamil Nadu",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Navigate with confidence. Open our location instantly in Google Maps to get direct, real-time driving directions from your current position.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsLink))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDeep)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = "Map symbols")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Maps app", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            // Direct Directions Uri
                            val directionsUri = Uri.parse("google.navigation:q=11.7584,79.5292") // Coordinate coordinates
                            var directionsIntent = Intent(Intent.ACTION_VIEW, directionsUri)
                            directionsIntent.setPackage("com.google.android.apps.maps")
                            if (directionsIntent.resolveActivity(context.packageManager) == null) {
                                // Fallback to generic maps link
                                directionsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsLink))
                            }
                            context.startActivity(directionsIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrightGold)
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = "Drive symbols", tint = ForestGreenDeep)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Get Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreenDeep)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ContactLineItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClickAction: (() -> Unit)?
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClickAction != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = onClickAction
                    )
                } else Modifier
            ),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ForestGreenMedium.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestGreenMedium,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BrightGold
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDeep,
                lineHeight = 18.sp
            )
            if (onClickAction != null) {
                Text(
                    text = "Tap to interact logo",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// ----------------------------------------------------
// ABOUT COMPANY SPACE
// ----------------------------------------------------

@Composable
fun AboutCompanySpace(viewModel: CashewViewModel) {
    val inquiries by viewModel.inquiryHistory.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val darkThemeState by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()
    val isSystemDark = isSystemInDarkTheme()
    val isAppInDark = when (darkThemeState) {
        null -> isSystemDark
        else -> darkThemeState == true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "About GnanavelCashews",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDeep,
                    fontFamily = FontFamily.Serif
                )
            )
            Text(
                text = "Discover GnanavelCashews' processing transparency & values",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // About Description Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "GnanavelCashews",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDeep,
                        fontFamily = FontFamily.Serif
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Corporate Brand Profile",
                    fontSize = 12.sp,
                    color = BrightGold,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We specialize in supplying premium-quality cashew kernels with strict quality standards and customer satisfaction. We provide various grades of cashew nuts suitable for retail, wholesale, export, and food industries.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Nestled within the Cuddalore district of Tamil Nadu, our processing hub leverages Panruti's famous red soil and coastal weather conditions to cultivate cashews that carry a signature sweet aroma and buttery richness found nowhere else in the world.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }

        // Toggle Dark Mode Card Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Visual Interface Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ForestGreenDeep
                    )
                    Text(
                        text = "Toggle client dark theme modes.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Switch(
                    checked = isAppInDark,
                    onCheckedChange = { viewModel.toggleTheme(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = BrightGold,
                        checkedTrackColor = ForestGreenMedium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Inquiry logs section (Room data!)
        Text(
            text = "My Inquiry History Log",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (inquiries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = "Inbox symbols",
                                tint = Color.LightGray,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No inquiries submitted yet.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Showing submitted wholes logs:",
                        fontSize = 12.sp,
                        color = BrightGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        inquiries.forEach { inquiry ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = VeryLightGreenbg.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Grade Inquiry: ${inquiry.productGrade}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ForestGreenDeep
                                        )
                                        Text(
                                            text = "Submitted",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 10.sp,
                                            color = ForestGreenMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Client Representative: ${inquiry.name} • ${inquiry.phone}",
                                        fontSize = 11.sp,
                                        color = Color.DarkGray
                                    )

                                    if (inquiry.email.isNotEmpty()) {
                                        Text(
                                            text = "Email Destination: ${inquiry.email}",
                                            fontSize = 11.sp,
                                            color = Color.DarkGray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Surface(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                                    ) {
                                        Text(
                                            text = "\"${inquiry.message}\"",
                                            fontSize = 11.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
