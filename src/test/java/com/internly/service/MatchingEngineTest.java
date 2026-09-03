package com.internly.service;

import com.internly.entity.*; import org.junit.jupiter.api.Test; import java.util.Set; import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {
    @Test void scoresAcrossDomainsUsingTransparentWeights() {
        StudentProfile student = StudentProfile.builder().domain("Mechanical Engineering").qualification("B.Tech").interests("CAD design").skills(Set.of(Skill.builder().name("AutoCAD").normalizedName("autocad").build(), Skill.builder().name("SolidWorks").normalizedName("solidworks").build())).build();
        Internship internship = Internship.builder().title("CAD Design Intern").domain("Mechanical").qualification("B.Tech").requiredSkills(Set.of(Skill.builder().name("AutoCAD").normalizedName("autocad").build(), Skill.builder().name("SolidWorks").normalizedName("solidworks").build())).build();
        MatchingEngine.MatchResult result = new MatchingEngine().score(student, internship);
        assertEquals(100, result.score()); assertEquals(2, result.matchedSkills()); assertTrue(result.domainMatched());
    }

    @Test void treatsSkillNamesCaseInsensitivelyAndExplainsPartialMatches() {
        StudentProfile student = StudentProfile.builder().domain("Commerce").qualification("B.Com").interests("Banking").skills(Set.of(Skill.builder().name("Accounting").normalizedName("accounting").build())).build();
        Internship internship = Internship.builder().title("Finance Intern").domain("Commerce").qualification("B.Com").requiredSkills(Set.of(Skill.builder().name("ACCOUNTING").normalizedName("accounting").build(), Skill.builder().name("Excel").normalizedName("excel").build())).build();
        MatchingEngine.MatchResult result = new MatchingEngine().score(student, internship);
        assertEquals(1, result.matchedSkills()); assertEquals(65, result.score()); assertTrue(result.explanation().contains("1/2 required skills matched"));
    }

    @Test void doesNotAwardDomainOrQualificationWhenTheyDoNotMatch() {
        StudentProfile student = StudentProfile.builder().domain("Civil Engineering").qualification("B.Tech").skills(Set.of()).build();
        Internship internship = Internship.builder().title("Java Intern").domain("IT").qualification("BCA").requiredSkills(Set.of(Skill.builder().name("Java").normalizedName("java").build())).build();
        MatchingEngine.MatchResult result = new MatchingEngine().score(student, internship);
        assertEquals(0, result.score()); assertFalse(result.domainMatched()); assertFalse(result.qualificationMatched());
    }
}
