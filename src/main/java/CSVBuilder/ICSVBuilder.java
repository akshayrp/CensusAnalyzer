package CSVBuilder;

import censusanalyser.CensusAnalyserException;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder<E>
{
   Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException, CSVBuilderException;
   List<E> getCSVFileList(Reader reader,Class<E> csvClass) throws CSVBuilderException;
}
