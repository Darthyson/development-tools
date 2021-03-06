package selfbus.debugger.actions;


/**
 * An exception that is thrown by the ActionFactory when the creation of an action object fails.
 */
public class ActionCreationException extends RuntimeException
{
   private static final long serialVersionUID = -1870093224626602165L;

   /**
    * Create an empty exception.
    */
   public ActionCreationException()
   {
      super();
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    */
   public ActionCreationException(String message)
   {
      super(message);
   }

   /**
    * Create an exception.
    * 
    * @param cause - the cause for the exception.
    */
   public ActionCreationException(Throwable cause)
   {
      super(cause);
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    * @param cause - the cause for the exception.
    */
   public ActionCreationException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
