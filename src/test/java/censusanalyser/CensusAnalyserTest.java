package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest
{

   private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
   private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
   private static final String INDIA_CODE_CSV_FILE_PATH
         = "/home/admin1/IdeaProjects/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCode.csv";
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
      catch (CSVBuilderException e)
      {
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
         censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
      }
      catch (CensusAnalyserException e)
      {
         Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
      }
      catch (CSVBuilderException e)
      {
      }
   }


   @Test
   public void givenIndianStateCodeCsv_ShouldReturnExactCount()
   {
      CensusAnalyser censusAnalyser = new CensusAnalyser();
      try
      {

      int numberOfRecord = censusAnalyser.loadIndiaStateCode(INDIA_CODE_CSV_FILE_PATH);
         Assert.assertEquals(37,numberOfRecord);
      }
      catch (CensusAnalyserException e)
      {
      }
      catch (CSVBuilderException e)
      {
      }

   }
}
