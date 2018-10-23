package devLogger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
 
/**
 * We can create our own custom Formatter class 
 * by extending java.util.logging.Formatter class 
 * and attach it to any of the handler
 * 
 * 
 * @author Florian Berndl
 */
public class MyFormatter extends Formatter {
 
    @Override
    public String format(LogRecord record) {
        return record.getThreadID()+"::"+record.getSourceClassName()+"::"
                +record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
 
}