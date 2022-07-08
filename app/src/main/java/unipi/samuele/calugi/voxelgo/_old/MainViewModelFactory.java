//package unipi.samuele.calugi.voxelgo.viewmodels;
//
//import android.database.sqlite.SQLiteDatabase;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//public class MainViewModelFactory implements ViewModelProvider.Factory {
//
//    private final SQLiteDatabase sqLiteDatabase;
//
//    public MainViewModelFactory(SQLiteDatabase sqLiteDatabase) {
//        this.sqLiteDatabase = sqLiteDatabase;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new MainViewModel(sqLiteDatabase);
//    }
//}