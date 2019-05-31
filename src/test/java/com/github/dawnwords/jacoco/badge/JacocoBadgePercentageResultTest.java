package com.github.dawnwords.jacoco.badge;

import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.BRANCH;
import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.CLASS;
import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.INSTRUCTION;
import static com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type.LINE;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JacocoBadgePercentageResultTest {

  @Mock
  private NamedNodeMap attr;

  @Mock
  private Node type;
  @Mock
  private Node covered;
  @Mock
  private Node missed;

  @BeforeMethod
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
    when(attr.getNamedItem("type")).thenReturn(type);
    when(attr.getNamedItem("covered")).thenReturn(covered);
    when(attr.getNamedItem("missed")).thenReturn(missed);
  }

  @DataProvider
  public Object[][] test() {
    return new Object[][]{
        new Object[]{INSTRUCTION, "100", "0", "INSTRUCTION:100%",
            "https://img.shields.io/badge/instruction--coverage-100%25-brightgreen.svg"},
        new Object[]{BRANCH, "70", "30", "BRANCH:70%",
            "https://img.shields.io/badge/branch--coverage-70%25-yellow.svg"},
        new Object[]{CLASS, "50", "50", "CLASS:50%",
            "https://img.shields.io/badge/class--coverage-50%25-orange.svg"},
        new Object[]{LINE, "30", "70", "LINE:30%",
            "https://img.shields.io/badge/line--coverage-30%25-red.svg"},
    };
  }

  @Test(dataProvider = "test")
  public void test(Type resultType, String coveredValue, String missedValue,
      String expectedString, String expectedBadge) {
    when(type.getNodeValue()).thenReturn(resultType.name());
    when(covered.getNodeValue()).thenReturn(coveredValue);
    when(missed.getNodeValue()).thenReturn(missedValue);

    JacocoBadgePercentageResult result = new JacocoBadgePercentageResult(attr);
    assertEquals(result.toString(), expectedString);
    assertEquals(result.badgeUrl(), expectedBadge);
  }
}
