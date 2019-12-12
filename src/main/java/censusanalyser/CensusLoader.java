package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader
{

   public Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) throws CensusAnalyserException
   {
      if (country.equals(CensusAnalyser.Country.INDIA))
         return loadCensusData(IndiaCensusCSV.class, csvFilePath);
      else
      {
         return loadCensusData(USCensusCSV.class, csvFilePath);
      }
   }


   private <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) throws CensusAnalyserException
   {
      Map<String, CensusDAO> censusMap = new HashMap<>();
      Iterator<E> csvFileIterator;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0])))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         try
         {
            csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
         }
         catch (RuntimeException e)
         {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
         }
         Iterable<E> csvIterable = () -> csvFileIterator;
         if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV"))
         {
            StreamSupport.stream
                  (csvIterable.spliterator(), false)
                  .map(IndiaCensusCSV.class::cast)
                  .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
         }
         else if (censusCSVClass.getName().equals("censusanalyser.USCensusCSV"))
         {
            StreamSupport.stream
                  (csvIterable.spliterator(), false)
                  .map(USCensusCSV.class::cast)
                  .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
         }

         if (csvFilePath.length == 1)
            return censusMap;
         loadIndiaStateCode(censusMap, csvFilePath[1]);
         return censusMap;
      }
      catch (CSVBuilderException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.ERROR_IN_BUILDER);
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CSV_FILE_PROBLEM);
      }
      catch (RuntimeException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER);
      }
   }

   private int loadIndiaStateCode(Map<String, CensusDAO> censusMap, String csvFilePath) throws CensusAnalyserException
   {
      Iterator<IndiaStateCodeCSV> stateCodeCSVIterator;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         try
         {
            stateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
         }
         catch (RuntimeException e)
         {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
         }
         Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCodeCSVIterator;
         StreamSupport.stream(csvIterable.spliterator(), false).filter(csvState -> censusMap.get(csvState.StateName) != null)
               .forEach(stateCSV -> censusMap.get(stateCSV.StateName).stateCode = stateCSV.StateCode);
         return censusMap.size();
      }
      catch (CSVBuilderException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     ERROR_IN_BUILDER);
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.
               CSV_FILE_PROBLEM);
      }
      catch (CensusAnalyserException e)
      {
         throw new CensusAnalyserException(e.getMessage(), e.type.name());
      }
   }


}
