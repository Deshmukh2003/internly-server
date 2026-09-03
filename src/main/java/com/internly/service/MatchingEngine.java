package com.internly.service;

import com.internly.entity.*; import org.springframework.stereotype.Component; import java.util.*; import java.util.stream.Collectors;

@Component
public class MatchingEngine {
    public MatchResult score(StudentProfile student, Internship internship) {
        Set<String> studentSkills = student.getSkills().stream().map(Skill::getNormalizedName).collect(Collectors.toSet());
        Set<String> required = internship.getRequiredSkills().stream().map(Skill::getNormalizedName).collect(Collectors.toSet());
        long matched = required.stream().filter(studentSkills::contains).count();
        double skill = required.isEmpty() ? 1 : (double) matched / required.size();
        boolean domainMatched = same(student.getDomain(), internship.getDomain()) || containsIgnoreCase(internship.getEligibleBranches(), student.getDomain());
        boolean qualificationMatched = same(student.getQualification(), internship.getQualification()) || blank(internship.getQualification());
        double domain = domainMatched ? 1 : 0;
        double qualification = qualificationMatched ? 1 : 0;
        double interest = interestMatch(student.getInterests(), internship.getTitle(), internship.getDomain());
        int score = (int) Math.round((skill * 0.50 + domain * 0.25 + qualification * 0.15 + interest * 0.10) * 100);
        return new MatchResult(score, matched, required.size(), domainMatched, qualificationMatched, "" + score + "% match — " + matched + "/" + required.size() + " required skills matched" + (domainMatched ? ", domain matched" : ""));
    }
    private double interestMatch(String interests, String... values) { if (blank(interests)) return 0; String text=interests.toLowerCase(Locale.ROOT); return Arrays.stream(values).filter(Objects::nonNull).anyMatch(v -> text.contains(v.toLowerCase(Locale.ROOT))) ? 1 : 0; }
    private boolean same(String a,String b) { return !blank(a) && !blank(b) && a.trim().equalsIgnoreCase(b.trim()); }
    private boolean containsIgnoreCase(String haystack,String needle) { return !blank(haystack) && !blank(needle) && haystack.toLowerCase(Locale.ROOT).contains(needle.trim().toLowerCase(Locale.ROOT)); }
    private boolean blank(String value) { return value == null || value.isBlank(); }
    public record MatchResult(int score, long matchedSkills, int requiredSkills, boolean domainMatched, boolean qualificationMatched, String explanation) {}
}
