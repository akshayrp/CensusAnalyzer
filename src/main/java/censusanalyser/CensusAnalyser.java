package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser
{
   List<IndiaCensusDAO> censusList;

   public CensusAnalyser()
   {
      this.censusList = new ArrayList<IndiaCensusDAO>();
   }

   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException, CSVBuilderException
   {
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
         while(csvFileIterator.hasNext())
         {
            this.censusList.add(new IndiaCensusDAO(csvFileIterator.next()));
         }
         return this.censusList.size();
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
            (csvIterable.spliterator(), false).count();
      return numberOfEntries;
   }

   public String getStateWiseSortedCensusData()
   {
      Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
      this.sort(censusComparator);
      String sortedStateCensusJson = new Gson().toJson(censusList);
      return sortedStateCensusJson;
   }

   private void sort(Comparator<IndiaCensusDAO> censusComparator)
   {
      for (int i = 0; i < censusList.size() - 1; i++)
      {
         for (int j = 0; j < censusList.size() - i - 1; j++)
         {
            IndiaCensusDAO census1 = censusList.get(j);
            IndiaCensusDAO census2 = censusList.get(j + 1);
            if (censusComparator.compare(census1, census2) > 0)
            {
               censusList.set(j, census2);
               censusList.set(j + 1, census1);
            }
         }
      }
   }
}
