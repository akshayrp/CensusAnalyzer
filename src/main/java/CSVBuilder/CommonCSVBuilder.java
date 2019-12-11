package CSVBuilder;

import censusanalyser.CensusAnalyserException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class CommonCSVBuilder<E> implements ICSVBuilder
{

   @Override
   public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CSVBuilderException
   {
      Iterator<E> iterator = null;
      try
      {
         iterator = (Iterator<E>) CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader).iterator();
         return iterator;
      }
      catch (IOException e)
      {
         throw new CSVBuilderException(e.getMessage(),
               CSVBuilderException.
                     ExceptionType.UNABLE_TO_PARSE);
      }

   }

   @Override
   public List<E> getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException
   {
      List<E> recordList = null;
      try
      {
         recordList = (List<E>) CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader).getRecords();
         return (List<E>) recordList;
      }
      catch (IOException e)
      {
         throw new CSVBuilderException(e.getMessage(),
               CSVBuilderException.
                     ExceptionType.UNABLE_TO_PARSE);
      }
   }
}
