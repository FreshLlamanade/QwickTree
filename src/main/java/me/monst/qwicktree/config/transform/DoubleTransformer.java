package me.monst.qwicktree.config.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.exception.UnreadableValueException;
import me.monst.pluginutil.configuration.exception.ValueOutOfBoundsException;
import me.monst.pluginutil.configuration.transform.Transformer;

public class DoubleTransformer implements Transformer<Double> {
    
    @Override
    public Double parse(String input) throws ArgumentParseException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + input + "' is not a number.");
        }
    }
    
    @Override
    public Double convert(Object object) throws ValueOutOfBoundsException, UnreadableValueException {
        if (object instanceof Double)
            return (Double) object;
        if (object instanceof Integer)
            return ((Integer) object).doubleValue();
        if (object instanceof Number)
            throw new ValueOutOfBoundsException(((Number) object).intValue());
        throw new ValueOutOfBoundsException(parse(object.toString()));
    }
    
    @Override
    public Double toYaml(Double value) {
        return value;
    }
    
}
