package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class IndiaAnalyzerTest
{
   private static final String INDIA_CENSUS_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCensusData.csv";
   private static final String WRONG_CENSUS_CSV_FILE_PATH
         = "./src/main/resources/IndiaStateCensusData.csv";
   private static final String INDIA_CODE_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCode.csv";
   private static final String EMPTY_FILE_PATH
         = "./src/test/resources/empty.csv";
   private static final String WRONG_DELIMITER_FILE_PATH
         = "./src/test/resources/StateCensusDataWrongDelimiter.csv";
   private static final String NO_HEADER_FILE_PATH
         = "./src/test/resources/StateCensusDataWithoutHeader.csv";
   private static final String NO_FILE_PATH
         = "./src/test/resources/NoFile.csv";

   CensusAdapter adapter = new IndiaCensusAdapter();

   @Test
   public void givenIndiaCSVData_ShouldGetMapOfCorrectSize()
   {
      Map<String, CensusDAO> keyMap = null;
      try
      {
         keyMap = adapter.loadCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         Assert.assertEquals(29, keyMap.size());
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(NO_HEADER_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER, e.type);
      }
   }

   @Test
   public void givenStateDataCSVFile_WhenDelimiterIncorrect_ThrowsException()
   {

      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(WRONG_DELIMITER_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER, e.type);
      }
   }

   @Test
   public void givenIndiaCensusData_WithWrongFile_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(WRONG_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenIndiaCensusData_WhenFilePassingSequenceWrong_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(INDIA_CODE_CSV_FILE_PATH, INDIA_CENSUS_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER, e.type);
      }
   }

   @Test
   public void givenIndianCensusCSVFile_WhenFileEmpty_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(EMPTY_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER, e.type);
      }
   }

   @Test
   public void givenIndiaCensusCSVFile_WhenFileDoesNotExist_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         adapter.loadCensusData(NO_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }
}
