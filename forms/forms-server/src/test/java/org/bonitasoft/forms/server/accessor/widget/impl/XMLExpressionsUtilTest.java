package org.bonitasoft.forms.server.accessor.widget.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bonitasoft.forms.client.model.Expression;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLExpressionsUtilTest {

	@Test
	public void should_parseExpression_do_not_modify_dependencies() throws Exception {
		//given
		XMLExpressionsUtil xmlExpressionsUtil = new XMLExpressionsUtil();
		String expressionParentNodeName = "script";	
		String expressionTagName = "input-parameter";
		
		String fileName = "formsWithExpressionDependencies.xml";
		File xmlFile = new File(getClass().getClassLoader().getResource(fileName).toURI());
		if(!xmlFile.exists()){
			throw new IllegalStateException("Recource File missing:" + fileName);
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		Node expressionParentNode = null;
		NodeList nodeList = doc.getElementsByTagName(expressionTagName);
		for (int i = 0;  i < nodeList.getLength(); i++) {
			if(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(expressionParentNodeName)){
				expressionParentNode = nodeList.item(i);
			}
		}		
		assertNotNull("Cannot retrieve the Expression Node with name: "+ expressionParentNodeName , expressionParentNode);
		
		//when
		Expression generatedExpression = xmlExpressionsUtil.parseExpression(expressionParentNodeName, expressionParentNode);
		
		//then 
		assertEquals("script:<pattern-expression>",generatedExpression.getName());
		assertEquals(generatedExpression.getDependencies().get(0).getName(), "myId");
		
	}
}
