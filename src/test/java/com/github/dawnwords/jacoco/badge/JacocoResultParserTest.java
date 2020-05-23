package com.github.dawnwords.jacoco.badge;

import static org.testng.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

public class JacocoResultParserTest {

  @Test
  public void testGetJacocoResults() throws Exception {
	final char decimalSeparatorChar = new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator();  
	final String decimalSeparator = new String(new char[]{decimalSeparatorChar});
		
    Path sampleReport = Files.createTempFile("testGetJacocoResults", "sample-jacoco-report.xml");
    IOUtils.copy(JacocoResultParserTest.class.getClassLoader()
        .getResourceAsStream("sample-jacoco-report.xml"), Files.newOutputStream(sampleReport));
    Map<String, JacocoBadgePercentageResult> result = new JacocoResultParser(
        new JacocoBadgeGenerateSetting()
            .setLimit(ImmutableMap.of("branch", 90))
            .setJacocoReportPath(sampleReport.toString()))
        .getJacocoResults();
    assertEquals(result.size(), 6);
    assertEquals(result.get("BRANCH").toString(), "BRANCH:100%");
    assertEquals(result.get("INSTRUCTION").toString(), "INSTRUCTION:100%");
    assertEquals(result.get("LINE").toString(), "LINE:100%");
    assertEquals(result.get("COMPLEXITY").toString(), "COMPLEXITY:10" + decimalSeparator + "00");
    assertEquals(result.get("METHOD").toString(), "METHOD:100%");
    assertEquals(result.get("CLASS").toString(), "CLASS:100%");
  }
}
