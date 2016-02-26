package com.TourAccount.services.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Tour;
import com.TourAccount.model.TourEnum;
import com.TourAccount.model.Tourist;
import com.TourAccount.services.AttachFilesWork;
import com.TourAccount.services.PDFWork;
import com.TourAccount.sqlite.DatabaseHandler;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: User
 * Date: 24.02.15
 * Time: 13:06
 */
public class AttachFilesWorkImpl implements AttachFilesWork {

    private PDFWork pdfWork;
    private Context applContext;
    private String dbFileName;
    private String pdfFileName;
    private DatabaseHandler db;

    private static String tahomaFontPath="/assets/tahoma.ttf";

    private static final String LOG = "AttachFilesWorkImpl";

    @Override
    public void setApplContext(Context applContext) {
        this.applContext = applContext;
    }

    @Override
    public DatabaseHandler getDb() {
        return db;
    }

    @Override
    public void setDb(DatabaseHandler db) {
        this.db = db;
    }

    @Override
    public String getPdfFileName() {
        return pdfFileName;
    }

    @Override
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    @Override
    public String getDbFileName() {
        return dbFileName;
    }

    @Override
    public void setDbFileName(String dbFileName) {
        this.dbFileName = dbFileName;
    }

    // Метод для сохранения файла     DB
    @Override
    public void saveDBFile() {

        writeDataFile(dbFileName);
        writeDataFilePDF(pdfFileName);
        //readFile("data.txt");

    }

    @Override
    public void writeDataFile(String FILENAME) {
        String string = db.SearchAllObjects();

        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath()
                    + File.separator + FILENAME);
            //
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(string.getBytes());
            fos.flush();
            fos.close();

        } catch (Exception e) {

            Toast toast = Toast.makeText(applContext,
                    "Exception create file: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }



    }

    @Override
    public void writeDataFilePDF(String FILENAME) {
       // try {

            makePDF(FILENAME);

       /* } catch (Exception e) {

            Toast toast = Toast.makeText(applContext,
                    "Exception create file: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }     */

        //}
    }

    @Override
    public void readFile(String FILENAME) {

        // InputStream instream = null;
        try {
            FileInputStream instream =
                    new FileInputStream
                            (new File(Environment.getExternalStorageDirectory().getPath() + "/" + FILENAME));

            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line, line1 = "";
                try {
                    while ((line = buffreader.readLine()) != null)
                        line1 += line;
                    Toast toast = Toast.makeText(applContext,
                            "InputStreamReader: " + line1, Toast.LENGTH_LONG);
                    toast.show();
                    Log.d(LOG, line1);

                } catch (Exception e) {
                    Toast toast = Toast.makeText(applContext,
                            "InputStreamReader Exception: " + e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
                buffreader.close();
                inputreader.close();
            }
        } catch (FileNotFoundException e) {
            Toast toast = Toast.makeText(applContext,
                    "InputStreamReader Exception: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Toast toast = Toast.makeText(applContext,
                    "InputStreamReader Exception: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void makePDF(String fileName) {
        // open a new document
        pdfWork = new PDFWorkImpl();
        pdfWork.setDb(db);
        try {

            //String FILE = "testMePDF.pdf";
          /*  File file = new File(Environment.getExternalStorageDirectory().getPath()
                    + File.separator + fileName);
            //
            Log.e("FILE SAVE", file.getPath());
            file.getParentFile().mkdirs();    */

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory().getPath()
                    + File.separator + fileName));
            document.open();

            pdfWork.setDb(db);
            pdfWork.setApplContext(applContext);
            pdfWork.setFontPATH(tahomaFontPath);
            pdfWork.setWinFont();
            pdfWork.addMetaData(document);
            pdfWork.addTitlePage(document);
            Log.e("make_PDF", "before get all tours");
            ArrayList<Tour> tours = (ArrayList) db.getAllTours(TourEnum.TourArchiveType.ALL.value);
            Log.e("make_PDF", "after get all tours");
            for (Tour tour : tours) {
                Log.e("make_PDF", "next tour = " + tour.toString());
                List<Currency> tourCourrencies = db.getAllCurrencies( tour.getId());
                List<Tourist> tourists = db.getTourTourists(tour.getId());
                pdfWork.addContent(document, (ArrayList) db.getAllTourItems(tour.getId()), tour, (ArrayList) tourCourrencies, (ArrayList) tourists);

            }
            document.close();
        } catch (Exception e) {
            Log.e("ERROR SAVE", "Err = "+ e.getMessage());
            Toast toast = Toast.makeText(applContext,
                    "make_PDF Exception: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
    }
}
