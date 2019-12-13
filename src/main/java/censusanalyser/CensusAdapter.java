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

public abstract class CensusAdapter
{
   public CensusAdapter()
   {
   }

   Map<String, CensusDAO> censusMap = new HashMap<>();
   public abstract Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException;

   public <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, String...csvFilePath) throws CensusAnalyserException
   {
      Iterator<E> csvFileIterator;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0])))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
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
