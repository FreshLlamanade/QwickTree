package me.monst.qwicktree.config.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.exception.UnreadableValueException;
import me.monst.pluginutil.configuration.exception.ValueOutOfBoundsException;
import me.monst.pluginutil.configuration.transform.Transformer;

public class IntegerTransformer implements Transformer<Integer> {
    
    @Override
    public Integer parse(String input) throws ArgumentParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + input + "' is not an integer.");
        }
    }
    
    @Override
    public Integer convert(Object object) throws ValueOutOfBoundsException, UnreadableValueException {
        if (object instanceof Integer)
            return (Integer) object;
        if (object instanceof Number)
            throw new ValueOutOfBoundsException(((Number) object).intValue());
        throw new ValueOutOfBoundsException(parse(object.toString()));
    }
    
    @Override
    public Integer toYaml(Integer value) {
        return value;
    }
    
}
