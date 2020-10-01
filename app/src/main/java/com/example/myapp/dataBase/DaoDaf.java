package com.example.myapp.dataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.model.Daf;
import java.util.Collection;
import java.util.List;

@Dao
public interface DaoDaf {

    @Query("SELECT * FROM Daf")
    List<Daf> getAllHistoricalLearning();

    @Query("SELECT MAX(id) FROM Daf")
    int getHighestId();

    @Query("SELECT * FROM Daf WHERE indexTypeOfStudy = :typeOfStudy")
    List<Daf> getAllLearning(int typeOfStudy);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllLearning(Collection<Daf> AllLearning);

    @Query("UPDATE Daf SET isLearning = :isLearning WHERE indexTypeOfStudy = :typeOfStudy and masechet = :masechet and pageNumber = :page")
    void updateIsLearning(boolean isLearning ,int typeOfStudy, String masechet , int page);

    @Query("UPDATE Daf SET chazara = :chazara WHERE indexTypeOfStudy = :typeOfStudy and masechet = :masechet and pageNumber = :page")
    void updateNumOfChazara(int chazara , int typeOfStudy, String masechet , int page);

    @Query("DELETE FROM Daf")
    void deleteAll();

    @Query("SELECT DISTINCT indexTypeOfStudy FROM Daf WHERE NOT indexTypeOfStudy = 0")
    List<Integer> getAllIndexTypeOfLeaning();

    @Query("DELETE FROM Daf WHERE indexTypeOfStudy = :typeOfLeaning and isLearning = 0")
    void deleteTypeOfLeaning(int typeOfLeaning);

    @Query("UPDATE Daf SET indexTypeOfStudy = 0 WHERE indexTypeOfStudy = :typeOfLeaning and isLearning = 1")
    void updateDeletedLeaning(int typeOfLeaning);

    @Query("UPDATE Daf SET indexTypeOfStudy = :typeOfLeaning -1 WHERE indexTypeOfStudy = :typeOfLeaning")
    void updateIndexTypeOfStudy(int typeOfLeaning);

}
