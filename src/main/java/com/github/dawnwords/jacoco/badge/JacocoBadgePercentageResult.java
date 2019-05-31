package com.github.dawnwords.jacoco.badge;

import org.w3c.dom.NamedNodeMap;

class JacocoBadgePercentageResult {

  private Type type;
  private int covered;
  private int missed;

  JacocoBadgePercentageResult(NamedNodeMap attr) {
    this.type = Type.valueOf(attr.getNamedItem("type").getNodeValue());
    this.covered = Integer.parseInt(attr.getNamedItem("covered").getNodeValue());
    this.missed = Integer.parseInt(attr.getNamedItem("missed").getNodeValue());
  }

  JacocoBadgePercentageResult(JacocoBadgePercentageResult that) {
    this.type = that.type();
    this.covered = that.covered();
    this.missed = that.missed();
  }

  String badgeUrl() {
    return String.format("https://img.shields.io/badge/%s-%d%%25-%s.svg",
        type.name().toLowerCase() + "--coverage", percent(), color());
  }

  private String color() {
    int percent = percent();
    if (percent >= 80) {
      return "brightgreen";
    } else if (percent > 60) {
      return "yellow";
    } else if (percent > 40) {
      return "orange";
    } else {
      return "red";
    }
  }

  Type type() {
    return type;
  }

  int covered() {
    return covered;
  }

  int missed() {
    return missed;
  }

  private int percent() {
    return (int) (100.0 * covered / (covered + missed));
  }

  @Override
  public String toString() {
    return type + ":" + percent() + "%";
  }

  enum Type {
    INSTRUCTION, BRANCH, LINE, COMPLEXITY, METHOD, CLASS
  }
}
