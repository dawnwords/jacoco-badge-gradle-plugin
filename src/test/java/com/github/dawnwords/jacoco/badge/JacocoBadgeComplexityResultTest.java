package com.github.dawnwords.jacoco.badge;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type;

public class JacocoBadgeComplexityResultTest {

  @Mock
  private JacocoBadgePercentageResult method;
  @Mock
  private JacocoBadgePercentageResult that;


  @BeforeMethod
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @DataProvider
  private Object[][] test() {
	
	final char decimalSeparatorChar = new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator();  
	final String decimalSeparator = new String(new char[]{decimalSeparatorChar});
	  
    return new Object[][]{
        new Object[]{120, 0, 60, "COMPLEXITY:2" + decimalSeparator + "00",
            "https://img.shields.io/badge/complexity-2" + decimalSeparator + "00-brightgreen.svg"},
        new Object[]{120, 0, 8, "COMPLEXITY:15" + decimalSeparator + "00",
            "https://img.shields.io/badge/complexity-15" + decimalSeparator + "00-yellow.svg"},
        new Object[]{120, 0, 4, "COMPLEXITY:30" + decimalSeparator + "00",
            "https://img.shields.io/badge/complexity-30" + decimalSeparator + "00-orange.svg"},
        new Object[]{120, 0, 2, "COMPLEXITY:60" + decimalSeparator + "00",
            "https://img.shields.io/badge/complexity-60" + decimalSeparator + "00-red.svg"},
    };
  }

  @Test(dataProvider = "test")
  public void test(int thatCovered, int thatMissed, int methodCovered, String expectString,
      String expectBadge) {
    when(that.type()).thenReturn(Type.COMPLEXITY);
    when(that.covered()).thenReturn(thatCovered);
    when(that.missed()).thenReturn(thatMissed);

    when(method.covered()).thenReturn(methodCovered);

    JacocoBadgeComplexityResult result = new JacocoBadgeComplexityResult(that, method);
    assertEquals(result.toString(), expectString);
    assertEquals(result.badgeUrl(), expectBadge);
  }
}
