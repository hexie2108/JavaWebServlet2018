
package it.unitn.webprogramming18.dellmm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

/**
 *metodo per elaborare l'immagine
 * @author mikuc
 */
public class ImageUtils
{

        /**
         * dato un file di immagine, ridimensiona in grandezza richiesta e in formato JPG 
         * @param oldImg file input
         * @param newImg file output
         * @param newWidth la lunghezza
         * @param newHeight l'altezza
         * @throws IOException 
         */
        public static void convertImg(File oldImg, File newImg, int newWidth, int newHeight) throws IOException
        {

                //ridimensionare forzata
                // Thumbnails.of(fromPic).size(100, 100).keepAspectRatio(false).toFile(toPic);
                //Thumbnails.of(fromPic).forceSize(100,100).toFile(toPic);
                //scalare
                //Thumbnails.of(fromPic).scale(0.2f).toFile(toPic);//按比例缩小
                // Thumbnails.of(fromPic).scale(2f);//按比例放大
       
                //ridimensione e riformata immagine in formato JPG con la nuova dimensione indicata (a volte non funziona)
                Builder<BufferedImage> builder = null;
                BufferedImage image = ImageIO.read(oldImg);

                //压缩至指定图片尺寸，保持图片不变形，多余部分裁剪掉
                int imageWidth = image.getWidth();
                int imageHeitht = image.getHeight();
                if ((float) newWidth / newHeight != (float) imageWidth / imageHeitht)
                {
                        if (imageWidth > imageHeitht)
                        {
                                image = Thumbnails.of(oldImg).height(newHeight).asBufferedImage();
                        }
                        else
                        {
                                image = Thumbnails.of(oldImg).width(newWidth).asBufferedImage();
                        }
                        
                        builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, newWidth, newHeight).size(newWidth, newHeight);
                }
                else
                {
                        builder = Thumbnails.of(image).size(newWidth, newHeight);
                }
                builder.outputFormat("jpg").outputQuality(0.9).toFile(newImg);

        }

}
