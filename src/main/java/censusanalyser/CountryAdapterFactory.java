package censusanalyser;

public class CountryAdapterFactory
{
   public static CensusAdapter getCountryObject(CensusAnalyser.Country country)
   {
      if(country.equals(CensusAnalyser.Country.INDIA))
         return new IndiaCensusAdapter();
      else if(country.equals(CensusAnalyser.Country.USA))
         return new USCensusAdapter();
      return null;
   }
}
