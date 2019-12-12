package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class USAdapterTest
{
   public static final String US_CENSUS_FILE_PATH
         ="/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/USCensusData.csv";

   @Test
   public void GivenUSCensusData_ShouldReturnExacteCountOfRecords()
   {
      USCensusAdapter usCensusAdapter = new USCensusAdapter();
      Map<String,CensusDAO> usMap = null;
      try
      {
         usMap = usCensusAdapter.loadCensusData(US_CENSUS_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         e.printStackTrace();
      }
      Assert.assertEquals(51,usMap.size());

   }
}
