package censusanalyser;

import CSVBuilder.CSVBuilderException;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest
{

   private static final String INDIA_CENSUS_CSV_FILE_PATH
         = "./src/test/resources/IndiaStateCensusData.csv";
   private static final String WRONG_CENSUS_CSV_FILE_PATH
         = "./src/main/resources/IndiaStateCensusData.csv";
   private static final String INDIA_CODE_CSV_FILE_PATH
         = "/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCode.csv";
   private static final String WRONG_CODE_CSV_FILE_PATH
         = "/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCode.csv";
   private static final String EMPTY_FILE_PATH
         = "/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/empty.csv";

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
   public void givenIndianStateCodeCsv_ShouldReturnExactCount()
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {
         int numberOfRecord = censusAnalyser.loadIndiaStateCode(INDIA_CODE_CSV_FILE_PATH);
         Assert.assertEquals(37, numberOfRecord);
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
         censusAnalyser.loadIndiaStateCode(WRONG_CODE_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
      }
   }

   @Test
   public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() throws CensusAnalyserException, CSVBuilderException
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
      String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
      IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
      Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
   }
}
