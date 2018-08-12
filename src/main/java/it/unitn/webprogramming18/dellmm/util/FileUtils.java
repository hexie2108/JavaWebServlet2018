package it.unitn.webprogramming18.dellmm.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    // il nome del file temporali
    private static final String TEMPORALY_FILE_NAME = "temp";
    // size
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    //formattare la data
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");

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

        String fileName = null;

        File oldFile = null;

        //get il nome originale di file
        fileName = new File(item.getName()).getName();
        //controlla se il file è un tipo di immagine valido
        if (!isValidFileExtension(item.getContentType())) {
            throw new ServletException("il tipo di file non è valido");
        }

        //combina i percorsi completi e genera due oggetti file
        oldFile = new File(uploadPath + File.separator + TEMPORALY_FILE_NAME);

        try {
            //salva file nell'oggetto oldFile temporale
            item.write(oldFile);
        } catch (Exception ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
        //ritorna il nome del nuovo file
        return convertJPG(oldFile, uploadPath, width, height);

    }

    public static String convertJPG(File oldFile, String uploadPath, int width, int height) throws IOException {
        //genera un nuovo nome per file in base annoMeseGiornoMinutoSecondoMillisecondo
        String newName = sdf.format(new Date()) + ".jpg";

        File newFile = new File(uploadPath + File.separator + newName);
        //ridimensione e genera il nuovo file
        ImageUtils.convertImg(oldFile, newFile, width, height);
        //cancella il file temporale
        if (!oldFile.delete()) {
            throw new IOException("non riesce eliminare il file temporaneo");
        }

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
     * controlla se il tipo di file corrisponde un tra tipi di file valido
     *
     * @param contentType il tipo di file input
     * @param allowTypes  array di tipi di file valido
     * @return true se è uno tra tipi file valido, false altrimenti
     */
    public static boolean isValidFileExtension(String contentType) {
        String[] allowTypes = new String[]
                {
                        "jpg", "jpeg", "png", "gif", "bmp"
                };
        if (null == contentType || "".equals(contentType)) {
            return false;
        }
        for (String type : allowTypes) {
            if (contentType.contains(type)) {
                return true;
            }
        }
        return false;

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
