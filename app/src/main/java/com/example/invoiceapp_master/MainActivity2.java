package com.example.invoiceapp_master;

import android.os.Bundle;
import android.widget.EditText;

public class MainActivity2 extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
          
         
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

                canvas.drawBitmap(scaledBitmap2,1700,3010,myPaint);    myPdfDocument.finishPage(myPage1);

                File file = new File(Environment.getExternalStorageDirectory(), "/1PerformaVisionArt.pdf");
                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPdfDocument.close();

            }
        });
    }
  }
}
