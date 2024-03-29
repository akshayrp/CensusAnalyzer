package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class CensusAnalyserTest
{
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
         = "./src/test/resources/StateCensusDataWithoutHeader.csv";
   private static final String NO_FILE_PATH
         = "./src/test/resources/NoFile.csv";
   public static final String US_CENSUS_FILE_PATH
         = "./src/test/resources/USCensusData.csv";


   @Test
   public void givenIndianCensusCSVFile_WhenFileEmpty_ShouldThrowException()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, EMPTY_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER, e.type);
      }
   }

   @Test
   public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, NO_HEADER_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_DELIMITER_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CENSUS_CSV_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.STATE);
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnPopulation_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION);
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Assam", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnArea_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.TOTAL_AREA);
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Rajasthan", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnDensity_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION_DENSITY);
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Bihar", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }


   @Test
   public void givenIndianCensusCSV_WhenNotLoadedAndTriedToSort_ShouldThrowException()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.STATE);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         Map<String, CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         Assert.assertEquals(29, map.size());
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndianStateCodeCSV_WithWrongPath_ShouldThrowException()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, WRONG_CODE_CSV_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, EMPTY_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, NO_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenUSCensusData_WhenCorrect_ShouldReturnCorrectRecord() throws CensusAnalyserException
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      Map<String, CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
      Assert.assertEquals(51, map.size());
   }

   @Test
   public void givenUsCensusData_WhenSortedOnState_ShouldReturnSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.STATE);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("Alabama", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("California", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenUSCensusData_WhenSortedOnArea_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.TOTAL_AREA);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("Alaska", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenUSCensusData_WhenSortedOnDensity_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION_DENSITY);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("District of Columbia", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenUSCensusData_WhenSortedOnPopulationAndDensity_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.USA, US_CENSUS_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION_AND_DENSITY);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("Florida", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndiaCensusData_WhenSortedOnPopulationAndDensity_ShouldReturnsSortedData()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.sortData(CountryFields.POPULATION_AND_DENSITY);
         USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
         Assert.assertEquals("West Bengal", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
      }
   }
}
