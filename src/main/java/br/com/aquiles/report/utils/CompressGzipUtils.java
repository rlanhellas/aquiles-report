package br.com.aquiles.report.utils;


import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class CompressGzipUtils implements Serializable {

    public static Object uncompress(String gzip) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(gzip);
        GZIPInputStream gis = new GZIPInputStream(fin);
        ObjectInputStream ois = new ObjectInputStream(gis);
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public static String compress(String gzip, Serializable serializable) throws IOException {
        FileOutputStream fos = new FileOutputStream(gzip);
        GZIPOutputStream gz = new GZIPOutputStream(fos);

        ObjectOutputStream oos = new ObjectOutputStream(gz);

        oos.writeObject(serializable);
        oos.close();
        return gzip;
    }

}
