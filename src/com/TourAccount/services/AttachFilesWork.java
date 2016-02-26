package com.TourAccount.services;

import android.content.Context;
import com.TourAccount.sqlite.DatabaseHandler;

/**
 * User: User
 * Date: 24.02.15
 * Time: 13:06
 */
public interface AttachFilesWork {
    void setApplContext(Context applContext);

    DatabaseHandler getDb();

    void setDb(DatabaseHandler db);

    String getPdfFileName();

    void setPdfFileName(String pdfFileName);

    String getDbFileName();

    void setDbFileName(String dbFileName);

    // Метод для сохранения файла     DB
    void saveDBFile();

    void writeDataFile(String FILENAME);

    void writeDataFilePDF(String FILENAME);

    void readFile(String FILENAME);

    void makePDF(String fileName);
}
