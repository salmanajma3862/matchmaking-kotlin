package com.salmanajmal.ziya.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.salmanajmal.ziya.data.api.SubscriptionApiService
import com.salmanajmal.ziya.data.api.VerifyPurchaseRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val subscriptionApiService: SubscriptionApiService
) : PurchasesUpdatedListener {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productDetails: StateFlow<List<ProductDetails>> = _productDetails.asStateFlow()

    private val _purchases = MutableStateFlow<List<Purchase>>(emptyList())
    val purchases: StateFlow<List<Purchase>> = _purchases.asStateFlow()

    private val _isBillingClientConnected = MutableStateFlow(false)
    val isBillingClientConnected: StateFlow<Boolean> = _isBillingClientConnected.asStateFlow()

    // Define your product IDs here
    private val productIds = listOf(
        "premium_monthly",
        "premium_yearly"
    )

    init {
        startConnection()
    }

    private fun startConnection() {
        _billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished")
                    _isBillingClientConnected.value = true
                    queryProductDetails()
                    queryPurchases()
                } else {
                    Log.e(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e(TAG, "Billing service disconnected")
                _isBillingClientConnected.value = false
                // Retry connection logic could go here
            }
        })
    }

    private fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map { productId ->
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                }
            )
            .build()

        _billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _productDetails.value = productDetailsList
            } else {
                Log.e(TAG, "Error querying product details: ${billingResult.debugMessage}")
            }
        }
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails, offerToken: String) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        _billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
            _purchases.value = purchases
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "User canceled the purchase")
        } else {
            Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                _billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "Purchase acknowledged")
                        // TODO: Send purchase token to backend for verification
                        verifyPurchaseOnBackend(purchase)
                    }
                }
            }
        }
    }
    
    private fun verifyPurchaseOnBackend(purchase: Purchase) {
        scope.launch {
            try {
                val request = VerifyPurchaseRequest(
                    purchaseToken = purchase.purchaseToken,
                    productId = purchase.products.first(),
                    orderId = purchase.orderId ?: ""
                )
                val response = subscriptionApiService.verifyPurchase(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d(TAG, "Purchase verified on backend")
                } else {
                    Log.e(TAG, "Backend verification failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error verifying purchase", e)
            }
        }
    }

    private fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        _billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _purchases.value = purchasesList
                for (purchase in purchasesList) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        handlePurchase(purchase)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "BillingManager"
    }
}
