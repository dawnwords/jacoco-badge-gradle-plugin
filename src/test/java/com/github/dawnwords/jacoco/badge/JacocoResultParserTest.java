package com.github.dawnwords.jacoco.badge;

import static org.testng.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

public class JacocoResultParserTest {

  @Test
  public void testGetJacocoResults() throws Exception {
    Path sampleReport = Files.createTempFile("testGetJacocoResults", "sample-jacoco-report.xml");
    IOUtils.copy(JacocoResultParserTest.class.getClassLoader()
        .getResourceAsStream("sample-jacoco-report.xml"), Files.newOutputStream(sampleReport));
    Map<String, JacocoBadgePercentageResult> result = new JacocoResultParser(
        new JacocoBadgeGenerateSetting().setJacocoReportPath(sampleReport.toString()))
        .getJacocoResults();
    assertEquals(result.size(), 6);
    assertEquals(result.get("BRANCH").toString(), "BRANCH:100%");
    assertEquals(result.get("INSTRUCTION").toString(), "INSTRUCTION:100%");
    assertEquals(result.get("LINE").toString(), "LINE:100%");
    assertEquals(result.get("COMPLEXITY").toString(), "COMPLEXITY:10.00");
    assertEquals(result.get("METHOD").toString(), "METHOD:100%");
    assertEquals(result.get("CLASS").toString(), "CLASS:100%");
  }
}
