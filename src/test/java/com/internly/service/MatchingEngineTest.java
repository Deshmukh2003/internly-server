package com.internly.service;

import com.internly.entity.*; import org.junit.jupiter.api.Test; import java.util.Set; import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {
    @Test void scoresAcrossDomainsUsingTransparentWeights() {
        StudentProfile student = StudentProfile.builder().domain("Mechanical Engineering").qualification("B.Tech").interests("CAD design").skills(Set.of(Skill.builder().name("AutoCAD").normalizedName("autocad").build(), Skill.builder().name("SolidWorks").normalizedName("solidworks").build())).build();
        Internship internship = Internship.builder().title("CAD Design Intern").domain("Mechanical").qualification("B.Tech").requiredSkills(Set.of(Skill.builder().name("AutoCAD").normalizedName("autocad").build(), Skill.builder().name("SolidWorks").normalizedName("solidworks").build())).build();
        MatchingEngine.MatchResult result = new MatchingEngine().score(student, internship);
        assertEquals(100, result.score()); assertEquals(2, result.matchedSkills()); assertTrue(result.domainMatched());
    }
}
