package censusanalyser;

import com.google.gson.Gson;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser
{
   public enum Country
   {
      INDIA, USA
   }

   public Country country;
   Map<String, CensusDAO> censusMap;
   Map<CountryFields, Comparator<CensusDAO>> sortBy = null;


   public CensusAnalyser()
   {
      this.censusMap = new HashMap<>();
      this.sortBy = new HashMap<>();
      this.sortBy.put(CountryFields.STATE, Comparator.comparing(census -> census.state));
      this.sortBy.put(CountryFields.POPULATION, Comparator.comparing(census -> census.population, Comparator.reverseOrder()));
      this.sortBy.put(CountryFields.TOTAL_AREA, Comparator.comparing(census -> census.totalArea, Comparator.reverseOrder()));
      this.sortBy.put(CountryFields.POPULATION_DENSITY, Comparator.comparing(census -> census.populationDensity, Comparator.reverseOrder()));
      Comparator<CensusDAO> firstComparator = Comparator.comparing(censusDAO -> censusDAO.population, Comparator.reverseOrder());
      this.sortBy.put(CountryFields.POPULATION_AND_DENSITY, firstComparator.thenComparing(censusDAO -> censusDAO.populationDensity, Comparator.reverseOrder()));
   }

   public Map<String, CensusDAO> loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException
   {
      this.country = country;
      CensusAdapter countryAdapter = CountryAdapterFactory.getCountryObject(country);
      censusMap = countryAdapter.loadCensusData(csvFilePath);
      return censusMap;
   }

   public String sortData(CountryFields fields) throws CensusAnalyserException
   {
      if (censusMap.size() == 0 || censusMap.equals(null))
      {
         throw new CensusAnalyserException("No Data to Read", CensusAnalyserException.ExceptionType.EMPTY_FILE);
      }
      ArrayList arrayList = censusMap.values().stream()
            .sorted(this.sortBy.get(fields))
            .map(censusDAO -> censusDAO.getCensusDTO(this.country))
            .collect(toCollection(ArrayList::new));
      String sortedStateCensus = new Gson().toJson(arrayList);
      return sortedStateCensus;
   }
}