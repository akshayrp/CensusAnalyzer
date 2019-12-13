package censusanalyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter
{
   public USCensusAdapter()
   {
   }

   @Override
   public Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException
   {
      Map<String,CensusDAO> USCensusMap = super.loadCensusData(USCensusCSV.class, csvFilePath);
      return USCensusMap;
   }
}
