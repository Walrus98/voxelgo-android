//package unipi.samuele.calugi.voxelgo.viewmodels;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//
//public class MainViewModel extends ViewModel {
//
//    private final MutableLiveData<Cursor> collectibleLiveData;
//    private final SQLiteDatabase sqLiteDatabase;
//
//    public MainViewModel(SQLiteDatabase sqLiteDatabase) {
//        this.sqLiteDatabase = sqLiteDatabase;
//        collectibleLiveData = new MutableLiveData<>();
//    }
//
//    public MutableLiveData<Cursor> getMutableLiveData(){
//        return collectibleLiveData;
//    }
//
//    public void updateDatabase() {
//        Cursor newCursor = sqLiteDatabase.rawQuery("SELECT * FROM collectibles", null);
//        collectibleLiveData.setValue(newCursor);
//    }
//
//
//}