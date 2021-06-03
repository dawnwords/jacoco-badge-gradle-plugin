package com.github.dawnwords.jacoco.badge;

class JacocoBadgeComplexityResult extends JacocoBadgePercentageResult {

  private final JacocoBadgePercentageResult method;

  JacocoBadgeComplexityResult(JacocoBadgePercentageResult that,
      JacocoBadgePercentageResult method) {
    super(that);
    this.method = method;
  }

  @Override
  String badgeUrl() {
    return String.format("https://img.shields.io/badge/complexity-%.2f-%s.svg", percent(), color());
  }

  private String color() {
    double percent = percent();
    if (percent < 10) {
      return "brightgreen";
    } else if (percent < 20) {
      return "yellow";
    } else if (percent < 40) {
      return "orange";
    } else {
      return "red";
    }
  }

  private double percent() {
    return 1.0 * (covered() + missed()) / method.covered();
  }

  @Override
  public String toString() {
    return String.format("%s:%.2f", type(), percent());
  }

}
