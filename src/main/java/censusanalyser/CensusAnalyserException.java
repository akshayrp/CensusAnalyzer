package censusanalyser;

public class CensusAnalyserException extends Exception {

    enum ExceptionType {
        CSV_FILE_PROBLEM,UNABLE_TO_PARSE, UNABLE_TO_IDENTIFY_DELIMITER, EMPTY_FILE, ERROR_IN_BUILDER,WRONG_COUNTRY
    }

   public ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }


    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }
}
