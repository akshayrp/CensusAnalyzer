package censusanalyser;

import CSVBuilder.CSVBuilderException;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

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
         ="./src/test/resources/StateCensusDataWithoutHeader.csv";

   @Test
   public void givenIndianCensusCSVFileReturnsCorrectRecords()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
         Assert.assertEquals(29, numOfRecords);
      }
      catch (CensusAnalyserException e)
      {
      }
   }

   @Test
   public void givenIndianCensusCSVFile_WhenFileEmpty_ShouldThrowException()
   {
      try
      {
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadIndiaCensusData(EMPTY_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException()
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadIndiaCensusData(NO_HEADER_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE, e.type);
      }
   }

   @Test
   public void givenStateDataCSVFile_WhenDelimiterIncorrect_ThrowsException()
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadIndiaCensusData(WRONG_DELIMITER_FILE_PATH);
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
         censusAnalyser.loadIndiaCensusData(WRONG_CENSUS_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() throws CensusAnalyserException
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {
         censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
         censusAnalyser.loadIndiaStateCode(INDIA_CODE_CSV_FILE_PATH);
         String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
         IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
         Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
      }
      catch (CensusAnalyserException e)
      {
         e.printStackTrace();
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
         String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
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
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {
         censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
         int numberOfRecord = censusAnalyser.loadIndiaStateCode(INDIA_CODE_CSV_FILE_PATH);
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
         CensusAnalyser censusAnalyser = new CensusAnalyser();
         ExpectedException exceptionRule = ExpectedException.none();
         exceptionRule.expect(CensusAnalyserException.class);
         censusAnalyser.loadIndiaStateCode(WRONG_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
      }
   }
}
