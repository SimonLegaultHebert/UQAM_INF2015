package tp1.inf2015;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jake
 */
public class SimpleRegex {

    private final String regex;
    private int groupToFetch = 0;
    private Pattern pattern;

    public SimpleRegex(String regex, int groupToFetch) {
        this.regex = regex;
        this.groupToFetch = groupToFetch;
        this.pattern = compilePattern();
    }

    public SimpleRegex(String regex) {
        this(regex, 0);
    }

    public String getRegex() {
        return regex;
    }

    public int getGroupToFetch() {
        return groupToFetch;
    }

    public Collection<String> getRegexGroups(String text) {
        return getRegexGroups(text, getGroupToFetch());
    }

    public Collection<String> getRegexGroups(String text, int group) {
        Collection<String> groups = new ArrayList<String>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String foundString = matcher.group(group);
            groups.add(foundString);
        }
        return groups;
    }

    private Pattern compilePattern() {
        return Pattern.compile(getRegex(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    public String getRegexGroup(final String text) {
        return getRegexGroup(text, getGroupToFetch());

    }

    public String replaceAll(final String text, final String replacement) {
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    //TODO 6: for all functions in this class, catch stupid runtimeexceptions coming from regex parsing because they're very dangerous
    public Collection<String> getRegexGroups(String text, int group, int group2, String separator) {
        Collection<String> groups = new ArrayList<String>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String foundString = matcher.group(group);
            String foundString2 = matcher.group(group2);
            groups.add(foundString + separator + foundString2);
        }
        return groups;
    }

    public String getRegexGroup(final String text, final int group) {
        final Collection<String> regexGroups = getRegexGroups(text, group);
        final Iterator<String> iterator = regexGroups.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    public boolean matches(final String content) {
        return pattern.matcher(content).matches();
    }
}
