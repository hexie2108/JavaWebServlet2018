package it.unitn.webprogramming18.dellmm.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * genera la stringa codificato in MD5 ed effettua il confronto
 * 
 * @author fendo
 */
public class MD5Utils
{

    

        /**
         * alfabeto di 16bit esadecimale
         */
        private final static char[] hexDigitsChar =
        {
                '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        /**
         * codificare la string in MD5
         * @param source string da codificare
         * @return string codificato
         * @throws java.io.UnsupportedEncodingException
         * @throws java.security.NoSuchAlgorithmException
         *
         */
        public static String getMD5(String source) throws UnsupportedEncodingException, NoSuchAlgorithmException
        {
                String mdString = null;
                if (source != null)
                {
                                mdString = getMD5(source.getBytes("UTF-8"));
                }
                return mdString;
        }

        /**
         * codificare l'array di byte in MD5
         * @param source string da codificare
         * @return string codificato
         * @throws java.security.NoSuchAlgorithmException
         *
         */
        private static String getMD5(byte[] source) throws NoSuchAlgorithmException
        {
                String s = null;

                final int temp = 0xf;
                final int arraySize = 32;
                final int strLen = 16;
                final int offset = 4;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(source);
                byte[] tmp = md.digest();
                char[] str = new char[arraySize];
                int k = 0;
                for (int i = 0; i < strLen; i++)
                {
                        byte byte0 = tmp[i];
                        str[k++] = hexDigitsChar[byte0 >>> offset & temp];
                        str[k++] = hexDigitsChar[byte0 & temp];
                }
                s = new String(str);

                return s;
        }

        /**
         * * dato una stringa e una string codificato, controlla se sono uguali dopo aver codificato
         *
         * @param string
         * @param md5 
         * @return true se sono uguali, false se non sono uguali
         * @throws java.io.UnsupportedEncodingException
         * @throws java.security.NoSuchAlgorithmException
         */
        public static boolean checkMD5Equal(String string, String md5) throws UnsupportedEncodingException, NoSuchAlgorithmException
        {
                return getMD5(string).equalsIgnoreCase(md5);
        }

}
