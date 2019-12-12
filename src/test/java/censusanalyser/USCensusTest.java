package censusanalyser;

import censusanalyser.CensusAnalyser;
import censusanalyser.CensusAnalyserException;
import org.junit.Assert;
import org.junit.Test;

public class USCensusTest
{
   CensusAnalyser censusAnalyser = new CensusAnalyser();

   public static final String US_CENSUS_FILE_PATH
         ="/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/USCensusData.csv";
   private static final String EMPTY_FILE_PATH
         = "./src/test/resources/empty.csv";

   @Test
   public void givenUSCensusData_WhenCorrect_ShouldReturnCorrectRecord() throws CensusAnalyserException
   {
      int numOfRecords = censusAnalyser.loadUSCensusData(US_CENSUS_FILE_PATH);
      Assert.assertEquals(51, numOfRecords);
   }
}
