package si.iitech.bear_bull_calculator;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsCodeExecutor {

	private static final String imports = "var MathUtils = Java.type('si.iitech.util.MathUtils'); var Color = Java.type('java.awt.Color');";

	private static ScriptEngine engine =  new ScriptEngineManager().getEngineByName("js");
	// "var ParseUtils = Java.type('si.iitech.lib.parser.ParserUtils');\n" +
	// "var DecimalSeparator = Java.type('si.iitech.lib.parser.DecimalSeparator');";

	public static Double getDoubleValue(String notation, String jsCode, CoinDataObject coinDataObject) throws NoSuchMethodException, ScriptException {
		Double value = getDoubleValuePrivate(jsCode, coinDataObject);
		coinDataObject.addCalculatedValue(notation, value);
		return value;
	}

	private static Double getDoubleValuePrivate(String jsCode, Object... inputs) throws NoSuchMethodException, ScriptException {
		Bindings binding = engine.createBindings();
		engine.eval(imports, binding);
		engine.eval(jsCode, binding);
		engine.setBindings(binding, ScriptContext.ENGINE_SCOPE);
		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("execute", inputs);
		if (result instanceof Number) {
			return ((Number) result).doubleValue();
		}
		return null;
	}

	public static String getStringValue(String notation, String jsCode, CoinDataObject coinDataObject) throws NoSuchMethodException, ScriptException {
		String value = getStringValuePrivate(jsCode, coinDataObject);
		coinDataObject.addCalculatedValue(notation, value);
		return value;
	}

	private static String getStringValuePrivate(String jsCode, Object... inputs) throws NoSuchMethodException, ScriptException {
		Bindings binding = engine.createBindings();
		engine.eval(imports, binding);
		engine.eval(jsCode, binding);
		engine.setBindings(binding, ScriptContext.ENGINE_SCOPE);
		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("execute", inputs);
		if (result instanceof String) {
			return ((String) result);
		}
		return null;
	}

	public static Boolean getBooleanValue(String notation, String jsCode, CoinDataObject coinDataObject) throws NoSuchMethodException, ScriptException {
		Boolean value = getBooleanValuePrivate(jsCode, coinDataObject);
		coinDataObject.addCalculatedValue(notation, value);
		return value;
	}

	private static Boolean getBooleanValuePrivate(String jsCode, Object... inputs) throws NoSuchMethodException, ScriptException {
		Bindings binding = engine.createBindings();
		engine.eval(imports, binding);
		engine.eval(jsCode, binding);
		engine.setBindings(binding, ScriptContext.ENGINE_SCOPE);
		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("execute", inputs);
		if (result instanceof Boolean) {
			return ((Boolean) result);
		}
		return null;
	}

	public static byte[] getByteArrayValue(String notation, String jsCode, CoinDataObject coinDataObject) throws NoSuchMethodException, ScriptException {
		byte[] value = getByteArrayValuePrivate(jsCode, coinDataObject);
		coinDataObject.addCalculatedValue(notation, value);
		return value;
	}

    private static byte[] getByteArrayValuePrivate(String jsCode, Object inputs) throws NoSuchMethodException, ScriptException {
        Bindings binding = engine.createBindings();
		engine.eval(imports, binding);
		engine.eval(jsCode, binding);
		engine.setBindings(binding, ScriptContext.ENGINE_SCOPE);
		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("execute", inputs);
		if (result instanceof byte[]) {
			return ((byte[]) result);
		}
		return null;
    }

}
