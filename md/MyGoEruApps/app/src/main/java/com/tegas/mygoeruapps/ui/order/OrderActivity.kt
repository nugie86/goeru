package com.tegas.mygoeruapps.ui.order

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import android.Manifest
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.databinding.ActivityOrderBinding
import com.tegas.mygoeruapps.ui.login.LoginActivity
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val viewModel by viewModels<OrderViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 101);
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Basic U0ItTWlkLXNlcnZlci11QWp1TXVRQmkwYmQ4UjZVOHRuQkpRN0Q6:")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }.build()

        val jsonBody = """
            {
            "transaction_detail": {
            "order_id":"ORDER666",
            "gross_amount": 10000
            }
        """.trimIndent()
//        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody)
//
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-VVMpRnOXlhue7K3c")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(
                TransactionFinishedCallback { result ->
                })
            .setMerchantBaseUrl("https://drive.google.com/drive/folders/1rTdm52HUZHAN63YShF7wVHxIr_z6CwUP/")
            .enableLog(true)
            .setLanguage("id")
            .buildSDK()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                Toast.makeText(this, "You need to login to order", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                binding.pesan.setOnClickListener {
                    val orderName = user.name
                    val quantities = binding.jumlah.text.toString()
                    val note = binding.catatan.text.toString()
                    val price = intent.getStringExtra("price")!!.toDouble()

//                    val total = quantities.toInt() * price

                    val transactionRequest = TransactionRequest("go-eru-"+System.currentTimeMillis().toShort() + "", price)
                    val detail = ItemDetails("ItemId", price, 1, orderName)

                    val itemDetails = ArrayList<ItemDetails>()
                    itemDetails.add(detail)

//                    uiKitDetails(transactionRequest, orderName)

                    val customerDetails = CustomerDetails()
                    customerDetails.customerIdentifier = orderName
                    customerDetails.email = user.email
                    transactionRequest.customerDetails = customerDetails

                    transactionRequest.itemDetails = itemDetails
                    MidtransSDK.getInstance().transactionRequest = transactionRequest
                    MidtransSDK.getInstance().startPaymentUiFlow(this)
                }
            }
        }

//        val teacherPrice = intent.getStringExtra("price")
//        val teacherName = intent.getStringExtra("name")
//        binding.pesan.setOnClickListener {
//            val price = teacherPrice!!.toDouble()
//
//            val transactionRequest = TransactionRequest("go-eru-"+System.currentTimeMillis().toShort() + "", price)
//            val detail = ItemDetails("ItemId", price, 1, teacherName)
//
//            val itemDetails = ArrayList<ItemDetails>()
//            itemDetails.add(detail)
//            uiKitDetails(transactionRequest, teacherName.toString())
//
//            transactionRequest.itemDetails = itemDetails
//            MidtransSDK.getInstance().transactionRequest = transactionRequest
//            MidtransSDK.getInstance().startPaymentUiFlow(this)
//        }
    }

//    private fun uiKitDetails(transactionRequest: TransactionRequest, name: String){
//        val customerDetails = CustomerDetails()
//        customerDetails.customerIdentifier = name
////        customerDetails.phone = "086666666"
////        customerDetails.firstName = "James"
////        customerDetails.lastName = "Hetfield"
//        customerDetails.email = "test@test.com"
//
//        transactionRequest.customerDetails = customerDetails
//    }
}