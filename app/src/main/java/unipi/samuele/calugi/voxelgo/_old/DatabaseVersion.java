//package unipi.samuele.calugi.voxelgo.handlers;
//
//import android.content.Context;
//
//import org.apache.commons.io.IOUtils;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//
//public class DatabaseVersion implements Runnable {
//
//    private final Context context;
//
//    private static DatabaseVersion databaseVersion;
//
//    public static DatabaseVersion getInstance(Context context) {
//        if (databaseVersion == null) {
//            databaseVersion = new DatabaseVersion(context.getApplicationContext());
//        }
//        return databaseVersion;
//    }
//
//    public DatabaseVersion(Context context) {
//        this.context = context;
//
//        Thread thread = new Thread(this);
//        thread.start();
//    }
//
//    @Override
//    public void run() {
//        try {
//            String databaseVersion = IOUtils.toString(URI.create("https://backend.voxel.frangioniwebdev.com/db-version"), StandardCharsets.UTF_8);
//
//            writeFileOnInternalStorage(context, "database-version", databaseVersion);
//
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }
//
//
//    public int getVersion() {
//
//        int version = -1;
//
//        try {
//            version = Integer.parseInt(readFile(context, "database-version"));
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//
//        return version;
//
//    }
//
//
//    private void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
//        File dir = new File(mcoContext.getFilesDir(), "mydir");
//
//        if(!dir.exists()){
//            dir.mkdir();
//        }
//
//        try {
//            File gpxfile = new File(dir, sFileName);
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(sBody);
//            writer.flush();
//            writer.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private String readFile(Context context, String filename) throws IOException {
//
//        File dir = new File(context.getFilesDir(), "mydir");
//        File gpxfile = new File(dir, filename);
//
//        FileInputStream fileInputStream = new FileInputStream(gpxfile);
//
//        return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
//    }
//
//
////    private static int DATABASE_VERSION;
////
////    static {
////
////        Thread thread = new Thread()
////
////
////    }
////
////    private static int getDatabaseVersion() {
////        return DATABASE_VERSION;
////    }
//
//}
