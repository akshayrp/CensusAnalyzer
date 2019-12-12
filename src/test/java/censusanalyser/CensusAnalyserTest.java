package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest
{
   CensusAnalyser censusAnalyser = new CensusAnalyser();

   private static final String INDIA_CENSUS_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCensusData.csv";
   private static final String WRONG_CENSUS_CSV_FILE_PATH
         = "./src/main/resources/IndiaStateCensusData.csv";
   private static final String INDIA_CODE_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCode.csv";
   private static final String WRONG_CODE_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCode.csv";
   private static final String EMPTY_FILE_PATH
         = "./src/test/resources/empty.csv";
   private static final String WRONG_DELIMITER_FILE_PATH
         = "./src/test/resources/StateCensusDataWrongDelimiter.csv";
   private static final String NO_HEADER_FILE_PATH
   ="/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/StateCensusDataWithoutHeader.csv";
   private static final String NO_FILE_PATH
         ="./src/test/resources/NoFile.csv";
   public static final String US_CENSUS_FILE_PATH
         ="/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/USCensusData.csv";


   @Test
   public void givenIndianCensusCSVFile_WhenFileEmpty_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,EMPTY_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,NO_HEADER_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenStateDataCSVFile_WhenDelimiterIncorrect_ThrowsException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_DELIMITER_FILE_PATH);
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
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CENSUS_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult()
   {
      try
      {
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.getStateWiseSortedData();
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      { }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnPopulation_ShouldReturnsSortedData()
   {
      try
      {
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.getPopulationWiseSortedData();
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Uttar Pradesh", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      { }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnArea_ShouldReturnsSortedData()
   {
      try
      {
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.getAreaWiseSortedData();
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Rajasthan", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      { }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnDensity_ShouldReturnsSortedData()
   {
      try
      {
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.getDensityWiseSortedData();
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Bihar", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      { }
   }

   @Test
   public void givenIndianCensusCSV_WhenNotLoadedAndTriedToSort_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         String sortedCensusData = censusAnalyser.getStateWiseSortedData();
         new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.EMPTY_FILE, e.type);
      }
   }

   @Test
   public void givenIndianStateCodeCsv_ShouldReturnExactCount()
   {
      try
      {
         int numberOfRecord = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_CODE_CSV_FILE_PATH);
         Assert.assertEquals(29, numberOfRecord);
      }
      catch (CensusAnalyserException e)
      {}
   }

   @Test
   public void givenIndianStateCodeCSV_WithWrongPath_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,WRONG_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenIndianStateCodeCSV_WithEmptyFile_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,EMPTY_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenIndianStateCodeCSV_WithNoFilePath_ShouldThrowException()
   {
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,NO_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenUSCensusData_WhenCorrect_ShouldReturnCorrectRecord() throws CensusAnalyserException
   {
      int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.USA,US_CENSUS_FILE_PATH);
      Assert.assertEquals(51, numOfRecords);
   }
}
