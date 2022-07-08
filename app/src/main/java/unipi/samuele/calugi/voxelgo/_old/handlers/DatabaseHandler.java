//package unipi.samuele.calugi.voxelgo.handlers;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//    private static DatabaseHandler databaseHandler;
//
//    public static DatabaseHandler getInstance(Context context) {
//        if (databaseHandler == null) {
//            databaseHandler = new DatabaseHandler(context.getApplicationContext());
//        }
//        return databaseHandler;
//    }
//
//    public DatabaseHandler(Context context) {
//        super(context, VoxelUtils.DATABASE_NAME, null, VoxelUtils.DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String queryCollectibles =
//            "CREATE TABLE \"collectibles\" (" +
//                "\"collectible_id\" INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "\"collectible_name\" TEXT," +
//                "\"collectible_model\" TEXT," +
//                "\"collectible_image\" TEXT," +
//                "\"collectible_rarity\" INTEGER" +
//            ");";
//
//        sqLiteDatabase.execSQL(queryCollectibles);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        @Override
////        protected void onPostExecute(ApiResponse.Model[] models) {
////            super.onPostExecute(models);
////
////            ContentValues values = new ContentValues();
////
////            for (ApiResponse.Model model : models) {
////                values.put("collectible_name", model.getModel_name());
////                values.put("collectible_model", "https://voxel.frangioniwebdev.com/?model=" + model.getModel_url_name());
////                values.put("collectible_image", "https://cdn.discordapp.com/attachments/606422186923917322/982390254847795270/unknown.png");
////                values.put("collectible_rarity", model.getModel_rarity());
////            }
////            sqLiteDatabase.insert("collectibles", null, values);
////        }
////    }
////}
//
//
//
////        String queryCaught =
////            "CREATE TABLE \"caughts\" (" +
////                "\"caught_id\" INTEGER," +
////                "\"caught_amount\" INTEGER," +
////                "FOREIGN KEY(\"caughts_amount\") REFERENCES collectibles(\"collectible_id\")" +
////            ");";
//
//
////    public void addCollectible(String name) {
////        SQLiteDatabase database = this.getWritableDatabase();
////
////        ContentValues values = new ContentValues();
////        values.put("collectible_name", name);
////        values.put("collectible_model", "Brutto");
////        values.put("collectible_image", "123");
////        values.put("collectible_rarity", 2);
////
////        database.insert("collectibles", null, values);
////        database.close();
////    }
