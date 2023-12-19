package com.tegas.mygoeruapps.ui.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-ROXpem4aKNSA1n6g")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(
                TransactionFinishedCallback { result ->
                })
            .setMerchantBaseUrl("https://hn8486gw-3000.asse.devtunnels.ms/payment/v1/charge/")
            .enableLog(true)
            .setLanguage("id")
            .buildSDK()

        val teacherPrice = intent.getStringExtra("price")
        val teacherName = intent.getStringExtra("name")
        binding.pesan.setOnClickListener {
            val price = teacherPrice!!.toDouble()

            val transactionRequest = TransactionRequest("go-eru-"+System.currentTimeMillis().toShort() + "", price)
            val detail = ItemDetails("ItemId", price, 1, teacherName)

            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detail)
            uiKitDetails(transactionRequest, teacherName.toString())

            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun uiKitDetails(transactionRequest: TransactionRequest, name: String){
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = name
        customerDetails.phone = "086666666"
        customerDetails.firstName = "James"
        customerDetails.lastName = "Hetfield"
        customerDetails.email = "test@test.com"

        transactionRequest.customerDetails = customerDetails
    }
}