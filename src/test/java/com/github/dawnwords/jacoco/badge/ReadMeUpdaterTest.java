package com.github.dawnwords.jacoco.badge;

import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.BRANCH;
import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.CLASS;
import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.COMPLEXITY;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ReadMeUpdaterTest {

  @Mock
  private JacocoBadgeGenerateSetting setting;

  @Mock
  private JacocoResultParser parser;

  @Mock
  private JacocoBadgePercentageResult branch;
  @Mock
  private JacocoBadgePercentageResult clazz;
  @Mock
  private JacocoBadgePercentageResult complexity;

  @BeforeMethod
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDefaultConstructor() {
    new ReadMeUpdater(setting);
  }

  @Test
  public void testUpdateReadme() throws Exception {
    Path sampleReport = Files.createTempFile("testUpdateReadme", "README.md");
    IOUtils.copy(JacocoResultParserTest.class.getClassLoader()
        .getResourceAsStream("sample-readme.md"), Files.newOutputStream(sampleReport));

    when(setting.getReadmePath()).thenReturn(sampleReport.toString());
    when(clazz.badgeUrl()).thenReturn("class-badge-url");
    when(branch.badgeUrl()).thenReturn("branch-badge-url");
    when(complexity.badgeUrl()).thenReturn("complexity-badge-url");
    when(parser.getJacocoResults()).thenReturn(ImmutableMap.of(
        BRANCH.name(), branch,
        CLASS.name(), clazz,
        COMPLEXITY.name(), complexity
    ));

    new ReadMeUpdater(setting, parser).updateReadme();

    assertEquals(String.join("\n", Files.readAllLines(sampleReport)), "# Sample Project\n"
        + "\n"
        + "### code coverages\n"
        + "![LINE](https://img.shields.io/badge/line--coverage-80%25-brightgreen.svg)\n"
        + "![BRANCH](branch-badge-url)\n"
        + "![COMPLEXITY](complexity-badge-url)\n"
        + "\n"
        + "## some other description\n"
        + "blah blah blah blah blah blah");
  }
}
