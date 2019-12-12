package censusanalyser;

import java.util.Map;

public class CensusLoader extends CensusAdapter
{

   @Override
   public Map<String, CensusDAO> loadCensusData(String[] csvFilePath) throws CensusAnalyserException
   {
      if (country.equals(CensusAnalyser.Country.INDIA))
         return loadCensusData(IndiaCensusCSV.class, csvFilePath);
      else
      {
         return loadCensusData(USCensusCSV.class, csvFilePath);
      }
   }




}
