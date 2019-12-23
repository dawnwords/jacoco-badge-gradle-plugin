package com.github.dawnwords.jacoco.badge;

import java.util.Collections;
import java.util.Map;
import org.gradle.api.GradleException;
import org.w3c.dom.NamedNodeMap;

class JacocoBadgePercentageResult {

  private Type type;
  private int covered;
  private int missed;
  private int limit;

  JacocoBadgePercentageResult(NamedNodeMap attr) {
    this(attr, Collections.emptyMap());
  }

  JacocoBadgePercentageResult(NamedNodeMap attr,
                              Map<String, Integer> limit) {
    final String type = attr.getNamedItem("type").getNodeValue();
    this.type = Type.valueOf(type);
    this.covered = Integer.parseInt(attr.getNamedItem("covered").getNodeValue());
    this.missed = Integer.parseInt(attr.getNamedItem("missed").getNodeValue());
    this.limit = limit.getOrDefault(type, 0);
  }

  JacocoBadgePercentageResult(JacocoBadgePercentageResult that) {
    this.type = that.type();
    this.covered = that.covered();
    this.missed = that.missed();
    this.limit = that.limit();
  }

  String badgeUrl() {
    return String.format("https://img.shields.io/badge/%s-%d%%25-%s.svg",
        type.name().toLowerCase() + "--coverage", percent(), color());
  }

  void verifyLimit() {
    final int percent = percent();
    if (percent < limit) {
      throw new GradleException(String.format(
          "%s coverage limit not satisfied, expect at least %d%%, got %d%%", type, limit, percent));
    }
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

  public int limit() {
    return limit;
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
