package com.TourAccount.services;

import android.content.Context;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Tour;
import com.TourAccount.model.TourItem;
import com.TourAccount.model.Tourist;
import com.TourAccount.sqlite.DatabaseHandler;
import com.itextpdf.text.*;

import java.io.IOException;
import java.util.ArrayList;


/**
 * User: User
 * Date: 23.02.15
 * Time: 16:59
 */
public interface PDFWork {
    void addMetaData(Document document);

    void addTitlePage(Document document) throws DocumentException, IOException;

    void addContent(Document document) throws DocumentException;

    void createList(Section subCatPart);

    void addEmptyLine(Paragraph paragraph, int number);

    void addContent(Document document, ArrayList<Tour> tourList) throws DocumentException, IOException;

    void setFontPATH(String fontPATH);

    void setWinFont() throws IOException, DocumentException;

    void addContent(Document document, ArrayList<TourItem> tourItems, Tour tour, ArrayList<Currency> tourCourrencies,
                    ArrayList<Tourist> tourists) throws DocumentException, IOException;

    void setDb(DatabaseHandler db);

    DatabaseHandler getDb();

    void setApplContext(Context applContext);
}
