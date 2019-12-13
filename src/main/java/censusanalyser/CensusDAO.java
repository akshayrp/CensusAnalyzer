package censusanalyser;

public class CensusDAO
{
   public String state;
   public String stateCode;
   public Double totalArea;
   public Double populationDensity;
   public int population;

   public CensusDAO()
   {
   }

   public CensusDAO(IndiaCensusCSV indiaCensusCSV)
   {
      state = indiaCensusCSV.state;
      totalArea = indiaCensusCSV.areaInSqKm;
      populationDensity = indiaCensusCSV.densityPerSqKm;
      population = indiaCensusCSV.population;
   }

   public CensusDAO(USCensusCSV censusCSV)
   {
      state = censusCSV.state;
      stateCode = censusCSV.stateId;
      totalArea =censusCSV.totalArea;
      populationDensity = censusCSV.populationDensity;
      population = censusCSV.population;

   }

   public Object getCensusDTO(CensusAnalyser.Country country) {
   if (country.equals(CensusAnalyser.Country.USA))
      return new USCensusCSV(state, stateCode, population, populationDensity, totalArea);
   return new IndiaCensusCSV(state,population,populationDensity,totalArea);
}
}
