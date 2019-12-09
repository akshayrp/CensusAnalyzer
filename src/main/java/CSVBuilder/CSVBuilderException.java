package CSVBuilder;

public class CSVBuilderException extends Exception
{
   enum ExceptionType {
      CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE
   }
   public ExceptionType type;


    public CSVBuilderException(String message,ExceptionType type, Throwable cause) {
      super(message, cause);
      this.type = type;
   }

   public CSVBuilderException(String message, ExceptionType type)
   {
      super(message);
      this.type = type;
   }

}
