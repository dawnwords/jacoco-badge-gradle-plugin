package com.github.dawnwords.jacoco.badge;

import com.github.dawnwords.jacoco.badge.JacocoBadgePercentageResult.Type;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.gradle.api.DefaultTask;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

class JacocoResultParser {

  private JacocoBadgeGenerateSetting setting;

  JacocoResultParser(JacocoBadgeGenerateSetting setting) {
    this.setting = setting;
  }

  Map<String, JacocoBadgePercentageResult> getJacocoResults()
      throws Exception {
    final Path reportPath = Paths.get(setting.getJacocoReportPath());
    final HashMap<String, JacocoBadgePercentageResult> results = new HashMap<>();

    try (InputStream in = Files.newInputStream(reportPath)) {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setFeature("http://xml.org/sax/features/namespaces", false);
      factory.setFeature("http://xml.org/sax/features/validation", false);
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      Document doc = factory.newDocumentBuilder().parse(in);
      XPathExpression expr = XPathFactory.newInstance().newXPath().compile("//report/counter");
      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        JacocoBadgePercentageResult s = new JacocoBadgePercentageResult(
            nodes.item(i).getAttributes());
        results.put(s.type().name(), s);
      }
      results.put(Type.COMPLEXITY.name(), new JacocoBadgeComplexityResult(
          results.get(Type.COMPLEXITY.name()), results.get(Type.METHOD.name())));
    }
    return results;
  }
}
