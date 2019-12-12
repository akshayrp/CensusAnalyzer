package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser
{
   Map<String, IndiaCensusDAO> censusMap;

   public CensusAnalyser()
   {
      this.censusMap = new HashMap<>();
   }

   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException
   {
      Iterator<IndiaCensusCSV> csvFileIterator;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         try
         {
            csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
         }
         catch (RuntimeException e)
         {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
         }
         Iterable<IndiaCensusCSV> csvIterable = () -> csvFileIterator;
         StreamSupport.stream
               (csvIterable.spliterator(), false).forEach(censusCSV -> censusMap.put(censusCSV.state, new IndiaCensusDAO(censusCSV)));
         return this.censusMap.size();
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

   public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException
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
         StreamSupport.stream(csvIterable.spliterator(),false).filter(csvState -> censusMap.get(csvState.StateName) != null)
               .forEach(stateCSV -> censusMap.get(stateCSV.StateName).StateCode = stateCSV.StateCode);
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

   public String getStateWiseSortedData() throws CensusAnalyserException
   {
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
      return sortData(censusComparator);
   }

   public String getPopulationWiseSortedData() throws CensusAnalyserException
   {
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.population,Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   public String getAreaWiseSortedData() throws CensusAnalyserException
   {
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.areaInSqKm,Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   public String getDensityWiseSortedData() throws CensusAnalyserException
   {
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.densityPerSqKm,Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   private String sortData(Comparator<IndiaCensusDAO> censusComparator) throws CensusAnalyserException
   {
      if (censusMap.size() == 0 || censusMap.equals(null))
      {
         throw new CensusAnalyserException("No Data to Read", CensusAnalyserException.ExceptionType.EMPTY_FILE);
      }
      List<IndiaCensusDAO> censusDAOS = censusMap.values().stream().collect(Collectors.toList());
      this.sort(censusDAOS, censusComparator);
      String sortedStateCensusJson = new Gson().toJson(censusDAOS);
      return sortedStateCensusJson;
   }

   private void sort(List<IndiaCensusDAO> censusDAOS, Comparator<IndiaCensusDAO> censusComparator)
   {
      for (int i = 0; i < censusDAOS.size() - 1; i++)
      {
         for (int j = 0; j < censusDAOS.size() - i - 1; j++)
         {
            IndiaCensusDAO census1 = censusDAOS.get(j);
            IndiaCensusDAO census2 = censusDAOS.get(j + 1);
            if (censusComparator.compare(census1, census2) > 0)
            {
               censusDAOS.set(j, census2);
               censusDAOS.set(j + 1, census1);
            }
         }
      }
   }
}
