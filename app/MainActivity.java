package com.example.octocat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button button, buttonNextActivity;
    String[] informationArray = new String[]{"Date","Company Name","Company Address","Customer GST","Customer PAN","Customer Mobile","Email-ID","Service Package"};

    Bitmap bmp, scaledBitmap, bmp2, scaledBitmap2;

    int year;
    int month;
    int day;

    LinearLayout linearLayoutBottom;
    EditText textDate,customerName,customerAddress,customerGst,customerPan,customerPhone,customerEmail,customerServiceType ;
    CheckBox boxLogoDesign,boxVcDesign,boxProductPrinting,boxGoogleAds,boxFestivalFly,boxSocialMMarketing,boxProductFlyPosting,boxWebHosting,boxVcPrint,boxProductPhoto,boxSeoServices,boxDailyFly,boxMobileApp,boxOther;
    EditText textDurationContract,textDomainName,textRemarks,textProduct1,textAmount1,textProduct2,textAmount2,textProduct3,textAmount3,textKeyword,textAmountWithoutGst,textAmountWithGst,textAdvancePayment,textModeOfPayment,textBalPending,textTermOfBal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonNextActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        button = findViewById(R.id.buttonCreatePdf);
        button.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        textDate = findViewById(R.id.editTextDate);

        final Calendar calendar = Calendar.getInstance();

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        textDate.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        customerName = findViewById(R.id.editTextCustName);
        customerAddress = findViewById(R.id.editTextCustAddress);
        customerGst = findViewById(R.id.editTextCustGst);
        customerPan = findViewById(R.id.editTextCustPan);
        customerPhone = findViewById(R.id.editTextPhone);
        customerEmail = findViewById(R.id.editTextEmailAddress);
        customerServiceType = findViewById(R.id.editTextServiceType);

        linearLayoutBottom = findViewById(R.id.linerLayout2);

        textDurationContract = findViewById(R.id.editTextContractDuration);
        textDomainName = findViewById(R.id.editTextDomainName);
        textRemarks = findViewById(R.id.editTextRemarks);
        textAmountWithoutGst = findViewById(R.id.editTextAmtWithoutGst);
        textAmountWithGst = findViewById(R.id.editTextAmtWithGst);
        textAdvancePayment = findViewById(R.id.editTextAdvancePay);
        textModeOfPayment = findViewById(R.id.editTextModePay);
        textBalPending = findViewById(R.id.editTextBalPendingAmt);
        textTermOfBal = findViewById(R.id.editTextTermBalPending);

        textProduct1 = findViewById(R.id.editTextProduct1);
        textAmount1 = findViewById(R.id.editTextAmount1);
        textProduct2 = findViewById(R.id.editTextProduct2);
        textAmount2 = findViewById(R.id.editTextAmount2);
        textProduct3 = findViewById(R.id.editTextProduct3);
        textAmount3 = findViewById(R.id.editTextAmount3);
        textKeyword = findViewById(R.id.editTextKeyword);

        boxLogoDesign = findViewById(R.id.checkBoxLogoDesign);
        boxVcDesign = findViewById(R.id.checkBoxVcDesign);
        boxProductPrinting = findViewById(R.id.checkBoxProductPrint);
        boxGoogleAds = findViewById(R.id.checkBoxGoogleAds);
        boxFestivalFly = findViewById(R.id.checkBoxFesFlyer);
        boxSocialMMarketing = findViewById(R.id.checkBoxSocialMarketing);
        boxProductFlyPosting = findViewById(R.id.checkBoxProductFlyer);
        boxWebHosting = findViewById(R.id.checkBoxWebDesHosting);
        boxVcPrint = findViewById(R.id.checkBoxVcPrint);
        boxProductPhoto = findViewById(R.id.checkBoxProductPhoto);
        boxSeoServices = findViewById(R.id.checkBoxSeoSem);
        boxDailyFly = findViewById(R.id.checkBoxDailyIntFlyer);
        boxMobileApp = findViewById(R.id.checkBoxAppDev);
        boxOther = findViewById(R.id.checkBoxOther);

//        bmp = BitmapFactory.decodeResource(getResources(), com.google.android.material.R.drawable.ic_android_black_24dp);
//        scaledBitmap = Bitmap.createScaledBitmap(bmp, 500,300,true);
//
//        bmp2 = BitmapFactory.decodeResource(getResources(), com.google.android.material.R.drawable.ic_android_black_24dp);
//        scaledBitmap2 = Bitmap.createScaledBitmap(bmp2, 250,250,true);

        createPDF();
    }


    private void createPDF() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Toast.makeText(MainActivity.this,"PDF Created in - My Files/Internal Storage", Toast.LENGTH_LONG).show();

                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2480, 3508, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();
                canvas.drawBitmap(scaledBitmap,10,10,myPaint);

                canvas.drawBitmap(scaledBitmap2,1700,3010,myPaint);

                myPaint.setTypeface(Typeface.SANS_SERIF);

                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(80f);
                myPaint.setColor(Color.rgb(37,171,165));
                canvas.drawText("Contract/Profroma Invoice", myPageInfo1.getPageWidth()/2,120,myPaint);

                myPaint.setTextSize(50f);
                myPaint.setColor(Color.rgb(112,119,119));
                canvas.drawText("Contract No.", 2200,150,myPaint);
                canvas.drawLine(2350,150,myPageInfo1.getPageWidth()-50,150,myPaint);


                myPaint.setTextSize(40f);
                myPaint.setColor(Color.rgb(112,119,119));
                myPaint.setTextScaleX(1f);
                canvas.drawText("1002, Ganesh Glory Near BSNL Office Jagatpur-Chainpur Road, S.G. Hwy, Ahmedabad, Gujarat 382481", 1450,220,myPaint);

                myPaint.setTextSize(40f);
                myPaint.setColor(Color.rgb(112,119,119));
                canvas.drawText("Email-vision.art.infotech@gmail.com, GSTIN: 24BTFPS9764N1ZQ, PAN No: 24BTFPS9764N",1450,265,myPaint);

                myPaint.setStrokeWidth(10);
                canvas.drawLine(25,315,myPageInfo1.getPageWidth()-25,325,myPaint);


//                myPaint.setTextAlign(Paint.Align.LEFT);
//                myPaint.setTextSize(8.0f);
//                myPaint.setColor(Color.rgb(0,0,0));
//                canvas.drawText("Contract/Profroma Invoice",10,65,myPaint);

//                myPaint.setTextAlign(Paint.Align.RIGHT);
//                myPaint.setTextSize(6.0f);
//                myPaint.setColor(Color.rgb(112,119,119));
//                canvas.drawText("GSTIN: 24BTFPS9764N1ZQ",240,60,myPaint);

//                myPaint.setTextAlign(Paint.Align.RIGHT);
//                myPaint.setTextSize(6.0f);
//                myPaint.setColor(Color.rgb(112,119,119));
//                canvas.drawText("PAN No: 24BTFPS9764N",240,70,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(55f);
                myPaint.setColor(Color.BLACK);

//                 name number lines values
                myPaint.setStrokeWidth(5);
                int startXPosition = 50;
                int endXPosition = myPageInfo1.getPageWidth()-50;
                int startYPosition = 400;

                for(int i=0; i<8; i++){
                    canvas.drawText(informationArray[i],startXPosition,startYPosition,myPaint);
                    canvas.drawLine(startXPosition,startYPosition+6,endXPosition,startYPosition+6,myPaint);
                    startYPosition+=100;
                }


                //                to take input and print in pdf
                canvas.drawText(String.valueOf(textDate.getText()),600,400,myPaint);
                canvas.drawText(String.valueOf(customerName.getText()),600,500,myPaint);
                canvas.drawText(String.valueOf(customerAddress.getText()),600,600,myPaint);
                canvas.drawText(String.valueOf(customerGst.getText()),600,700,myPaint);
                canvas.drawText(String.valueOf(customerPan.getText()),600,800,myPaint);
                canvas.drawText(String.valueOf(customerPhone.getText()),600,900,myPaint);
                canvas.drawText(String.valueOf(customerEmail.getText()),600,1000,myPaint);
                canvas.drawText(String.valueOf(customerServiceType.getText()),600,1100,myPaint);


                // horizontal line in the name, number invoice column

                canvas.drawLine(550,350,550,1100,myPaint);
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(4);

                //                table values

                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(6);

                //                making table width and height

                canvas.drawRect(50,1150,myPageInfo1.getPageWidth()-50,1750,myPaint);
                canvas.drawLine(myPageInfo1.getPageWidth()/2,1150,myPageInfo1.getPageWidth()/2,1750,myPaint);

                myPaint.setStrokeWidth(0);
                myPaint.setStyle(Paint.Style.FILL);

                myPaint.setTextSize(60f);

                canvas.drawText("Logo Design",70,1220,myPaint);
                if(boxLogoDesign.isChecked()) {
                    canvas.drawText("✔", 1125, 1210, myPaint); }
                canvas.drawLine(1100,1220,1200,1220,myPaint);

                canvas.drawText("V.C, Envelope Letterhead Design",70,1300,myPaint);
                if(boxVcDesign.isChecked()){
                    canvas.drawText("✔",1125,1290,myPaint); }
                canvas.drawLine(1100,1300,1200,1300,myPaint);

                canvas.drawText("Product Brochure & Printing",70,1380,myPaint);
                if(boxProductPrinting.isChecked()){
                    canvas.drawText("✔",1125,1370,myPaint);}
                canvas.drawLine(1100,1380,1200,1380,myPaint);

                canvas.drawText("Google Ads setup charges",70,1460,myPaint);
                if(boxGoogleAds.isChecked()){
                    canvas.drawText("✔",1125,1450,myPaint);}
                canvas.drawLine(1100,1460,1200,1460,myPaint);

                canvas.drawText("Festival Flyer (24 Flyers Yearly)",70,1540,myPaint);
                if(boxFestivalFly.isChecked()){
                    canvas.drawText("✔",1125,1530,myPaint);}
                canvas.drawLine(1100,1540,1200,1540,myPaint);

                canvas.drawText("Social Media Marketing",70,1620,myPaint);
                if(boxSocialMMarketing.isChecked()){
                    canvas.drawText("✔",1125,1610,myPaint);}
                canvas.drawLine(1100,1620,1200,1620,myPaint);

                canvas.drawText("Product Flyer & Posting",70,1700,myPaint);
                if(boxProductFlyPosting.isChecked()){
                    canvas.drawText("✔",1125,1690,myPaint);}
                canvas.drawLine(1100,1700,1200,1700,myPaint);



                canvas.drawText("Web Design Domain & Hosting",1255,1220,myPaint);
                if(boxWebHosting.isChecked()) {
                    canvas.drawText("✔", 2325, 1210, myPaint); }
                canvas.drawLine(2300,1220,2400,1220,myPaint);

                canvas.drawText("V.C Card, Envelope Letterhead Printing",1255,1300,myPaint);
                if(boxVcPrint.isChecked()){
                    canvas.drawText("✔",2325,1290,myPaint); }
                canvas.drawLine(2300,1300,2400,1300,myPaint);

                canvas.drawText("Product Photography",1255,1380,myPaint);
                if(boxProductPhoto.isChecked()){
                    canvas.drawText("✔",2325,1370,myPaint);}
                canvas.drawLine(2300,1380,2400,1380,myPaint);

                canvas.drawText("SEO/SEM Services",1255,1460,myPaint);
                if(boxSeoServices.isChecked()){
                    canvas.drawText("✔",2325,1450,myPaint);}
                canvas.drawLine(2300,1460,2400,1460,myPaint);

                canvas.drawText("Daily International Flyer Design ",1255,1540,myPaint);
                if(boxDailyFly.isChecked()){
                    canvas.drawText("✔",2325,1530,myPaint);}
                canvas.drawLine(2300,1540,2400,1540,myPaint);

                canvas.drawText("Mobile App Development",1255,1620,myPaint);
                if(boxMobileApp.isChecked()){
                    canvas.drawText("✔",2325,1610,myPaint);}
                canvas.drawLine(2300,1620,2400,1620,myPaint);

                canvas.drawText("Other",1255,1700,myPaint);
                if(boxOther.isChecked()){
                    canvas.drawText("✔",2325,1690,myPaint);}
                canvas.drawLine(2300,1700,2400,1700,myPaint);


                myPaint.setTextSize(65f);
                canvas.drawText("Duration of Contract:",50,1850,myPaint);
                canvas.drawLine(660,1850,myPageInfo1.getPageWidth()-50,1850,myPaint);
                canvas.drawText(String.valueOf(textDurationContract.getText()),690,1845,myPaint);


                canvas.drawText("Domain Name:",50,1920,myPaint);
                canvas.drawLine(490,1925,myPageInfo1.getPageWidth()-50,1925,myPaint);
                canvas.drawText(String.valueOf(textDomainName.getText()),500,1920,myPaint);


                canvas.drawText("Remarks:",50,1995,myPaint);
                canvas.drawLine(350,2000,myPageInfo1.getPageWidth()-50,2000,myPaint);
                canvas.drawText(String.valueOf(textRemarks.getText()),375,1995,myPaint);


                //                Second table width and height

                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(5);

                canvas.drawRect(50,2040,myPageInfo1.getPageWidth()-50,2500,myPaint);
                canvas.drawLine(250,2040,250,2350,myPaint);

                myPaint.setStrokeWidth(4);
                myPaint.setStyle(Paint.Style.FILL);

                myPaint.setTypeface(Typeface.DEFAULT_BOLD);
                myPaint.setTextSize(50f);
                canvas.drawText("Sr. No.",80,2100,myPaint);
                canvas.drawText("1",125,2180,myPaint);
                canvas.drawText("2",125,2255,myPaint);
                canvas.drawText("3",125,2330,myPaint);
                canvas.drawLine(50,2125,myPageInfo1.getPageWidth()-50,2125,myPaint);

                myPaint.setTextSize(50f);
                canvas.drawText("Product Details",470,2100,myPaint);
                canvas.drawLine(50,2200,myPageInfo1.getPageWidth()-50,2200,myPaint);

                canvas.drawText(String.valueOf(textProduct1.getText()),300,2190,myPaint);
                canvas.drawText(String.valueOf(textAmount1.getText()),1100,2190,myPaint);

                canvas.drawText(String.valueOf(textProduct2.getText()),300,2265,myPaint);
                canvas.drawText(String.valueOf(textAmount2.getText()),1100,2265,myPaint);

                canvas.drawText(String.valueOf(textProduct3.getText()),300,2340,myPaint);
                canvas.drawText(String.valueOf(textAmount3.getText()),1100,2340,myPaint);

                canvas.drawText(String.valueOf(textKeyword.getText()),1600,2190,myPaint);


                canvas.drawLine(1050,2040,1050,2500,myPaint);
                canvas.drawText("Amount in INR",1125,2100,myPaint);
                canvas.drawLine(50,2275,myPageInfo1.getPageWidth()-50,2275,myPaint);

                canvas.drawLine(1550,2040,1550,2500,myPaint);
                canvas.drawText("Keyword List ",1800,2100,myPaint);

                canvas.drawLine(50,2350,myPageInfo1.getPageWidth()-50,2350,myPaint);
                canvas.drawLine(50,2425,myPageInfo1.getPageWidth()-50,2425,myPaint);
                canvas.drawText("Total Amount in INR without GST",295,2410,myPaint);
                canvas.drawText(String.valueOf(textAmountWithoutGst.getText()),1110,2410,myPaint);


                canvas.drawText("Final Contract Value with Tax in INR",225,2480,myPaint);
                canvas.drawText(String.valueOf(textAmountWithGst.getText()),1110,2480,myPaint);


                myPaint.setTypeface(Typeface.DEFAULT);
                myPaint.setTextSize(65f);
                canvas.drawText("Advance Payment Amount in INR:",50,2585,myPaint);
                canvas.drawLine(1050,2585,myPageInfo1.getPageWidth()-50,2585,myPaint);
                canvas.drawText(String.valueOf(textAdvancePayment.getText()),1075,2580,myPaint);


                canvas.drawText("Mode of Payment:",50,2660,myPaint);
                canvas.drawLine(600,2660,myPageInfo1.getPageWidth()-50,2660,myPaint);
                canvas.drawText(String.valueOf(textModeOfPayment.getText()),625,2655,myPaint);


                canvas.drawText("Balance Pending Amount In INR:",50,2735,myPaint);
                canvas.drawLine(1020,2735,myPageInfo1.getPageWidth()-50,2735,myPaint);
                canvas.drawText(String.valueOf(textBalPending.getText()),1050,2730,myPaint);


                canvas.drawText("Terms of Balance Pending Payment:",50,2815,myPaint);
                canvas.drawLine(1120,2815,myPageInfo1.getPageWidth()-50,2815,myPaint);
                canvas.drawText(String.valueOf(textTermOfBal.getText()),1150,2810,myPaint);


                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(8);

                canvas.drawRect(50,2900,myPageInfo1.getPageWidth()-50,3300,myPaint);
                canvas.drawLine(myPageInfo1.getPageWidth()/2,2900,myPageInfo1.getPageWidth()/2,3300,myPaint);

                myPaint.setStrokeWidth(0);
                myPaint.setStyle(Paint.Style.FILL);

                myPaint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("Customer Sign with Stamp:",75,2975,myPaint);
                canvas.drawText("(For Digital Not Applicable)",75,3050,myPaint);
                canvas.drawText("For Vision Art Infotech:",1275,2975,myPaint);


                myPaint.setColor(Color.rgb(37,171,165));
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(40);
                canvas.drawLine(20,3400,myPageInfo1.getPageWidth()-20,3400,myPaint);


                myPdfDocument.finishPage(myPage1);

                File file = new File(Environment.getExternalStorageDirectory(), "/1Invoice.pdf");
                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPdfDocument.close();

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}