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
   public <E> Map<String, CensusDAO> loadCensusData(String csvFilePath, Class<E> censusCSVClass) throws CensusAnalyserException
   {
      Map<String, CensusDAO> censusMap = new HashMap<>();
      Iterator<E> csvFileIterator;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
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
         if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV"))
         {
            StreamSupport.stream
                  (csvIterable.spliterator(), false)
                  .map(IndiaCensusCSV.class::cast)
                  .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
         }
         else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV"))
         {
            StreamSupport.stream
                  (csvIterable.spliterator(), false)
                  .map(USCensusCSV.class::cast)
                  .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
         }
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
}
