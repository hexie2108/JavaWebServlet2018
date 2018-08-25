package it.unitn.webprogramming18.dellmm.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * metodo per update del file multimediali
 *
 * @author mikuc
 */
public class FileUtils {
    // size
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    /**
     * passando l'oggetto file della richiesta, lo ridimensiona e salva su
     * disco, al fine ritorna il nome di file salvato
     *
     * @param item       FileItem della richiesta
     * @param uploadPath il percorso per memorizzare
     * @param width      la lunghezza dell'immagine
     * @param height     l'altezza dell'immagine
     * @return il nome del file salvato
     * @throws ServletException
     * @throws IOException
     */
    public static String upload(FileItem item, String uploadPath, int width, int height) throws ServletException, IOException {
        //crea oggetto file per il percorso
        File uploadDir = new File(uploadPath);
        // se percorso non esiste, crearlo
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdir()) {
                throw new ServletException("non riesce creare la cartella indicata");
            }
        }

        //genera un nuovo nome per file in base a UUID
        String newName = UUID.randomUUID() + ".jpg";

        File newFile = new File(uploadPath + File.separator + newName);
        //ridimensione e genera il nuovo file
        ImageUtils.convertImg2(item.getInputStream(), newFile, width, height);

        return newName;
    }

    /**
     * eliminare il file
     *
     * @param filePath percorso di file
     * @throws IOException
     */
    public static void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.delete();
    }



    /**
     * inizializzare e istanziare uploader
     *
     * @return
     */
    public static ServletFileUpload initial() {
        // set il parametri per uploader
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //set la limite di memoria, la parte che supera la limite verrà memorizzata nei percorsi temporali su disco
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // set il percorso temporali
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        //crea istanza uploader con i parametri
        ServletFileUpload upload = new ServletFileUpload(factory);
        //set la grandezza massima accettabile di file
        upload.setFileSizeMax(MAX_FILE_SIZE);
        //set la grandezza massima accettabile di richiesta
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // set la codifica in UTF8
        upload.setHeaderEncoding("UTF-8");
        return upload;

    }
}
