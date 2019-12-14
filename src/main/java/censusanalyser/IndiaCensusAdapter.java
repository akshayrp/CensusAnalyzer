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

public class IndiaCensusAdapter extends CensusAdapter
{

   public IndiaCensusAdapter()
   {

   }

   Map<String, CensusDAO> censusMap = new HashMap<>();

   @Override
   public Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException
   {
      censusMap = super.loadCensusData(IndiaCensusCSV.class, csvFilePath);
      this.loadIndiaStateCode(csvFilePath[1]);
      return censusMap;
   }

   private Map<String, CensusDAO> loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException
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
         return censusMap;
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
}
