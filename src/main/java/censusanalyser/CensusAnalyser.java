package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser
{
   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException, CSVBuilderException
   {

      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         List<IndiaCensusCSV> cencusCsvList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
         return cencusCsvList.size();
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     CENSUS_FILE_PROBLEM);
      }
   }

   public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException, CSVBuilderException
   {
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = csvBuilder.
               getCSVFileIterator(reader, IndiaStateCodeCSV.class);
         return getCount(stateCodeCSVIterator);
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
   }

   private <E> int getCount(Iterator<E> csvIterator)
   {
      Iterable<E> csvIterable = () -> csvIterator;
      int numberOfEntries = (int) StreamSupport.stream
            (csvIterable.
                  spliterator(), false).count();
      return numberOfEntries;
   }

   public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException
   {
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         List<IndiaCensusCSV> censusCsvList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
         Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
         this.sort(censusCsvList, censusComparator);
         String sortedStateCensusJson = new Gson().toJson(censusCsvList);
         return sortedStateCensusJson;
      }
      catch (IOException | CSVBuilderException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     CENSUS_FILE_PROBLEM);
      }
   }

   private void sort(List<IndiaCensusCSV> censusList, Comparator<IndiaCensusCSV> censusComparator)
   {
      for (int i = 0; i < censusList.size()-1; i++)
      {
         for (int j = 0; j < censusList.size() - i - 1; j++)
         {
            IndiaCensusCSV census1 = censusList.get(j);
            IndiaCensusCSV census2 = censusList.get(j + 1);
            if (censusComparator.compare(census1, census2) > 0)
            {
               censusList.set(j, census2);
               censusList.set(j + 1, census1);
            }
         }
      }
   }
}
