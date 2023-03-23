package me.monst.qwicktree.config.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.exception.UnreadableValueException;
import me.monst.pluginutil.configuration.exception.ValueOutOfBoundsException;
import me.monst.pluginutil.configuration.transform.Transformer;

public class BooleanTransformer implements Transformer<Boolean> {
    
    @Override
    public Boolean parse(String input) throws ArgumentParseException {
        if (input.equalsIgnoreCase("true")
                || input.equalsIgnoreCase("yes")
                || input.equalsIgnoreCase("on"))
            return true;
        if (input.equalsIgnoreCase("false")
                || input.equalsIgnoreCase("no")
                || input.equalsIgnoreCase("off"))
            return false;
        throw new ArgumentParseException("'" + input + "' is not a boolean.");
    }
    
    @Override
    public Boolean convert(Object object) throws ValueOutOfBoundsException, UnreadableValueException {
        if (object instanceof Boolean)
            return (Boolean) object;
        if (object instanceof Number)
            throw new ValueOutOfBoundsException(((Number) object).intValue() != 0);
        return parse(object.toString());
    }
    
    @Override
    public Object toYaml(Boolean value) {
        return value;
    }
    
}
