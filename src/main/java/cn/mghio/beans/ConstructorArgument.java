package cn.mghio.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mghio
 * @since 2020-11-07
 */
@NoArgsConstructor
public class ConstructorArgument {

    private final List<ValueHolder> argumentsValues = new LinkedList<>();

    public void addArgumentValue(Object value) {
        this.argumentsValues.add(new ValueHolder(value));
    }

    public void addArgumentValue(Object value, String type) {
        this.argumentsValues.add(new ValueHolder(value, type));
    }

    public void addArgumentValue(ValueHolder valueHolder) {
        this.argumentsValues.add(valueHolder);
    }

    public List<ValueHolder> getArgumentsValues() {
        return this.argumentsValues;
    }

    public int getArgumentCount() {
        return this.argumentsValues.size();
    }

    public boolean isEmpty() {
        return this.argumentsValues.isEmpty();
    }

    public void clear() {
        this.argumentsValues.clear();
    }

    @Data
    public static class ValueHolder {

        private Object value;
        private String type;
        private String name;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }
    }

}
