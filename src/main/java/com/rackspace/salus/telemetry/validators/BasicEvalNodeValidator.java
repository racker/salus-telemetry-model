package com.rackspace.salus.telemetry.validators;

import com.rackspace.salus.telemetry.model.BasicEvalNode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class BasicEvalNodeValidator implements
    ConstraintValidator<ValidBasicEvalNode, BasicEvalNode> {

  public static String functionRegex = "(\\w+)\\((.*)\\)";
  private static List<String> functionList = Arrays.asList(

      // Stateful functions
      "spread", "sigma", "count",

      // type conversion
      "bool", "int", "float", "string", "duration",

      // existence
      "isPresent",

      // time
      "year", "month", "day", "weekday", "hour", "minute", "unixNano",

      // math
      "abs", "acos", "acosh", "asin", "asinh", "atan", "atan2", "atanh", "cbrt",
      "ceil", "cos", "cosh", "erf", "erfc", "exp", "exp2", "expm1", "floor",
      "gamma", "hypot", "j0", "j1", "jn", "log", "log10", "log1p", "log2", "logb",
      "max", "min", "mod", "pow", "pow10", "sin", "sinh", "sqrt", "tan", "tanh",
      "trunc", "y0", "y1", "yn",

      // string
      "strContains", "strContainsAny", "strCount", "strHasPrefix",
      "strHasSuffix", "strIndex", "strIndexAny", "strLastIndex", "strLastIndexAny",
      "strReplace", "strToLower", "strToUpper", "strTrim", "strTrimLeft", "strTrimPrefix",
      "strTrimRight", "strTrimSpace", "strTrimSuffix", "humanBytes"

      // conditional function:
      //  not currently handled, non-trivial quoting,
      //  and already available in com.rackspace.salus.event.manage.model.Expression
      //  https://docs.influxdata.com/kapacitor/v1.5/tick/expr/#conditional-functions
  );

  private static Set<String> validFunctions = new HashSet<>(functionList);

  private boolean isValidOperand(String operand) {
    Matcher matcher = Pattern.compile(functionRegex).matcher(operand);
    if (!matcher.matches()) {
      return true;
    }
    // confirm function call invokes valid function name
    return validFunctions.contains(matcher.group(1));
  }

  @Override
  public boolean isValid(BasicEvalNode evalNode, ConstraintValidatorContext context) {
    return evalNode.getOperands().stream().allMatch(this::isValidOperand);
  }
}
