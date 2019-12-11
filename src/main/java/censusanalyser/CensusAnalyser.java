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

      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
      }
      catch (CSVBuilderException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
      }
      catch (RuntimeException e)
      {
         throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_IDENTIFY_DELIMITER);
      }
   }

   public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException
   {
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
         while (stateCodeCSVIterator.hasNext())
         {
            IndiaStateCodeCSV stateCSV = stateCodeCSVIterator.next();
            IndiaCensusDAO DAOMerged = censusMap.get(stateCSV.StateName);
            if (DAOMerged == null)
               continue;
            DAOMerged.StateCode = stateCSV.StateCode;
         }
         return censusMap.size();

      }

      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     CENSUS_FILE_PROBLEM);
      }
      catch (CensusAnalyserException e)
      {
         throw new CensusAnalyserException(e.getMessage(), e.type.name());
      }
      catch (CSVBuilderException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     UNABLE_TO_PARSE);
      }

   }

   private <E> int getCount(Iterator<E> csvIterator)
   {
      Iterable<E> csvIterable = () -> csvIterator;
      int numberOfEntries = (int) StreamSupport.stream
            (csvIterable.spliterator(), false).count();
      return numberOfEntries;
   }

   public String getStateWiseSortedCensusData() throws CensusAnalyserException
   {
      if (censusMap.size() == 0 || censusMap.equals(null))
      {
         throw new CensusAnalyserException("No Data to Read", CensusAnalyserException.ExceptionType.EMPTY_FILE);
      }
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
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
