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
   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException
   {
      CsvToBean<IndiaCensusCSV> csvToBean = null;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         Iterator<IndiaCensusCSV> censusCSVIterator = this.getCSVFileIterator(reader, IndiaCensusCSV.class);
         return getCount(censusCSVIterator);
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
      }
   }

   public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException
   {

      CsvToBean<IndiaStateCodeCSV> csvToBean = null;
      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = this.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
         return getCount(stateCodeCSVIterator);
      }
      catch (IOException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
      }
   }

   private <E> int getCount(Iterator<E> csvIterator)
   {
      Iterable<E> csvIterable = () -> csvIterator;
      int numberOfEntries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
      return numberOfEntries;
   }

   private <E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException
   {
      try
      {
         CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
         csvToBeanBuilder.withType(csvClass);
         csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
         CsvToBean<E> csvToBean = csvToBeanBuilder.build();
         return csvToBean.iterator();
      }
      catch (IllegalStateException e)
      {
         throw new CensusAnalyserException(e.getMessage(),
               CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
      }
   }
}
