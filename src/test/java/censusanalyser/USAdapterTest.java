package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class USAdapterTest
{
   public static final String US_CENSUS_FILE_PATH
         ="./src/test/resources/USCensusData.csv";
   private static final String EMPTY_FILE_PATH
         = "./src/test/resources/empty.csv";
   private static final String WRONG_DELIMITER_FILE_PATH
         = "./src/test/resources/USCensusDataWithWrongDelimiter.csv";
   private static final String NO_HEADER_FILE_PATH
         = "./src/test/resources/USCensusDataWithoutHeader.csv";
   private static final String NO_FILE_PATH
         = "./src/test/resources/NoFile.csv";

   CensusAdapter adapter = new IndiaCensusAdapter();

   @Test
   public void GivenUSCensusData_ShouldReturnExactCountOfRecords()
   {
      USCensusAdapter usCensusAdapter = new USCensusAdapter();
      Map<String,CensusDAO> usMap = null;
      try
      {
         usMap = usCensusAdapter.loadCensusData(US_CENSUS_FILE_PATH);
         Assert.assertEquals(51,usMap.size());
      }
      catch (CensusAnalyserException e)
      { }

   }

   @Test
   public void givenUsCensusData_WhenDelimiterNotAvailable_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(WRONG_DELIMITER_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }


   @Test
   public void givenUsCensusData_WhenFileDoesNotExist_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(NO_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenUsCensusData_WhenFileIsEmpty_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(EMPTY_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenUsCensusData_WhenFileDoesNotHaveHeader_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(NO_HEADER_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }
}
