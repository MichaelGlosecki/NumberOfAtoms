import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution {
    public String countOfAtoms(String formula) {
        Matcher matcher = Pattern.compile("([A-Z][a-z]*)(\\d*)|(\\()|(\\))(\\d*)").matcher(formula);
        Stack<Map<String, Integer>> formulaStack = new Stack<>();
        formulaStack.push(new TreeMap<>());

        //Traverse the matches.
        while (matcher.find()) {
            String matcherGroup = matcher.group();
//            System.out.println(matcherGroup);
            if (matcherGroup.equals("(")) {
                //Found a new sub formula.
                formulaStack.push(new TreeMap<>());
            } else if (matcherGroup.startsWith(")")) {
                //Found the end of a current formula. Check for a multiplier.
                Map<String, Integer> currentSubFormula = formulaStack.pop();
                int multiplier = 1;
                if (matcherGroup.length() > 1) {
                    multiplier = Integer.parseInt(matcherGroup.substring(1, matcherGroup.length()));
                }
                //Multiply all of the values in the sub formula by the multiplier and add the elements to original map.
                for (String elementName : currentSubFormula.keySet()) {
                    int multipliedValue = currentSubFormula.get(elementName) * multiplier;
                    int currentValue = formulaStack.peek().getOrDefault(elementName, 0);
                    formulaStack.peek().put(elementName, currentValue + multipliedValue);
                }
            } else {
                //Adding a new element to the current formula.

                //Get the full element name.
                int index = 1;
                while (index < matcherGroup.length() && Character.isLowerCase(matcherGroup.charAt(index))) {
                    index++;
                }
                String elementName = matcherGroup.substring(0, index);

                //Determine if there are multiple atoms.
                int multiplier = 1;
                if (index < matcherGroup.length()) {
                    multiplier = Integer.parseInt(matcherGroup.substring(index, matcherGroup.length()));
                }

                //Check to see if we have seen this before, otherwise use the multiplier as the value.
                int currentValue = formulaStack.peek().getOrDefault(elementName, 0);
                formulaStack.peek().put(elementName, currentValue + multiplier);
            }
        }

        //Because its in a TreeMap everything should be in order.
        StringBuilder output = new StringBuilder();
        for (String name: formulaStack.peek().keySet()) {
            output.append(name);
            final int count = formulaStack.peek().get(name);
            if (count > 1) {
                output.append(String.valueOf(count));
            }
        }
        return output.toString();
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.countOfAtoms("MgNaAl5(Si4O10)3(OH)6"));
    }
}