package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser
{
   OpenCSVBuilder openCSVBuilder = new OpenCSVBuilder();

   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException
   {
      CsvToBean<IndiaCensusCSV> csvToBean = null;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         Iterator<IndiaCensusCSV> censusCSVIterator = openCSVBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
         return getCount(censusCSVIterator);
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.
                     ExceptionType.
                     CENSUS_FILE_PROBLEM);
      }
   }

   public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException
   {

      CsvToBean<IndiaStateCodeCSV> csvToBean = null;
      try (Reader reader = Files.
            newBufferedReader(Paths.
                  get(csvFilePath)))
      {
         Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = openCSVBuilder.
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
   }

   private <E> int getCount(Iterator<E> csvIterator)
   {
      Iterable<E> csvIterable = () -> csvIterator;
      int numberOfEntries = (int) StreamSupport.stream
            (csvIterable.
                  spliterator(), false).count();
      return numberOfEntries;
   }
}
