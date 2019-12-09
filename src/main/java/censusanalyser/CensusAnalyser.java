package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateEncodingException;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser
{
   public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException, CSVBuilderException
   {

      try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
      {
         ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
         Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
         return getCount(censusCSVIterator);
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
         throw new CensusAnalyserException(e.getMessage(),e.type.name());
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
         throw new CensusAnalyserException(e.getMessage(),e.type.name());
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
