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
   Map<String, CensusDAO> censusMap;

   public CensusAnalyser()
   {
      this.censusMap = new HashMap<>();
   }

   CensusLoader censusLoader = new CensusLoader();

   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException
   {
      censusMap = censusLoader.loadCensusData(csvFilePath, IndiaCensusCSV.class);
      return censusMap.size();
   }

   public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException
   {
      censusMap = censusLoader.loadCensusData(csvFilePath, USCensusCSV.class);
      return censusMap.size();
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

   public String getStateWiseSortedData() throws CensusAnalyserException
   {
      Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
      return sortData(censusComparator);
   }

   public String getPopulationWiseSortedData() throws CensusAnalyserException
   {
      Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.population, Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   public String getAreaWiseSortedData() throws CensusAnalyserException
   {
      Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.totalArea, Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   public String getDensityWiseSortedData() throws CensusAnalyserException
   {
      Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.populationDensity, Comparator.reverseOrder());
      return sortData(censusComparator);
   }

   private String sortData(Comparator<CensusDAO> censusComparator) throws CensusAnalyserException
   {
      if (censusMap.size() == 0 || censusMap.equals(null))
      {
         throw new CensusAnalyserException("No Data to Read", CensusAnalyserException.ExceptionType.EMPTY_FILE);
      }
      List<CensusDAO> censusDAOS = censusMap.values().stream().collect(Collectors.toList());
      this.sort(censusDAOS, censusComparator);
      String sortedStateCensusJson = new Gson().toJson(censusDAOS);
      return sortedStateCensusJson;
   }

   private void sort(List<CensusDAO> censusDAOS, Comparator<CensusDAO> censusComparator)
   {
      for (int i = 0; i < censusDAOS.size() - 1; i++)
      {
         for (int j = 0; j < censusDAOS.size() - i - 1; j++)
         {
            CensusDAO census1 = censusDAOS.get(j);
            CensusDAO census2 = censusDAOS.get(j + 1);
            if (censusComparator.compare(census1, census2) > 0)
            {
               censusDAOS.set(j, census2);
               censusDAOS.set(j + 1, census1);
            }
         }
      }
   }
}
