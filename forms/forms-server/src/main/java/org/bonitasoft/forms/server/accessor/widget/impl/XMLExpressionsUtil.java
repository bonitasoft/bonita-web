/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 */
package org.bonitasoft.forms.server.accessor.widget.impl;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.server.accessor.impl.util.XPathUtil;
import org.bonitasoft.forms.server.api.impl.util.FormFieldValuesUtil;
import org.bonitasoft.forms.server.constants.XMLForms;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parse an XML node to return one/several expressions
 * 
 * @author Anthony Birembaut
 * 
 */
public class XMLExpressionsUtil extends XPathUtil {

    /**
     * Instance attribute
     */
    protected static XMLExpressionsUtil INSTANCE = null;
   
    public static String NON_APPLICABLE_PREFIX = "n/a";
    
    /**
     * @return the XMLExpressionsUtil instance
     */
    public static synchronized XMLExpressionsUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XMLExpressionsUtil();
        }
        return INSTANCE;
    }

    /**
     * Constructor
     */
    protected XMLExpressionsUtil() {
    }

    
    /**
     * Parse an expression Array node, put expressionParentName as "n/a" as default value.
     * 
     * @param expressionsNode
     *            the expression node
     *            
     * @return a list of list of expressions
     * @throws InvalidFormDefinitionException
     */
    public List<List<Expression>> parseExpressionsArray(final Node expressionsNode) throws InvalidFormDefinitionException {
    	return parseExpressionsArray(NON_APPLICABLE_PREFIX, expressionsNode);
    }
    
    /**
     * Parse an expression Array node
     * 
     * @param expressionParentName
     *            name of the parent widget
     * 
     * @param expressionsNode
     *            the expression node
     * @return a list of list of expressions
     * @throws InvalidFormDefinitionException
     */
    public List<List<Expression>> parseExpressionsArray(final String expressionParentName, final Node expressionsNode) throws InvalidFormDefinitionException {
        final List<List<Expression>> expressions = new ArrayList<List<Expression>>();
        final NodeList rowNodes = getNodeListByXpath(expressionsNode, XMLForms.ROW);
        for (int i = 0; i < rowNodes.getLength(); i++) {
            final Node rowNode = rowNodes.item(i);
            if (rowNode != null) {
                expressions.add(parseExpressionsList(expressionParentName, rowNode));
            }
        }
        return expressions;
    }
    
    /**
     * Parse a expression list node, put expressionParentName as "n/a" as default value.
     * 
     * @param expressionsNode
     *            the expression node
     * @return a list of expressions
     * @throws InvalidFormDefinitionException
     */
    public List<Expression> parseExpressionsList(final Node expressionsNode) throws InvalidFormDefinitionException {
    	return  parseExpressionsList(NON_APPLICABLE_PREFIX, expressionsNode);
    }
    
    /**
     * Parse a expression list node
     * 
     * @param expressionParentName
     *            name of the parent widget
     *            
     * @param expressionsNode
     *            the expression node
     * @return a list of expressions
     * @throws InvalidFormDefinitionException
     */
    public List<Expression> parseExpressionsList(final String expressionParentName, final Node expressionsNode) throws InvalidFormDefinitionException {
        final List<Expression> expressions = new ArrayList<Expression>();
        final NodeList expressionNodes = getNodeListByXpath(expressionsNode, XMLForms.EXPRESSION);
        for (int i = 0; i < expressionNodes.getLength(); i++) {
            final Node expressionNode = expressionNodes.item(i);
            if (expressionNode != null) {
                expressions.add(parseExpressionContent(expressionParentName, expressionNode));
            }
        }
        return expressions;
    }

    /**
     * Parse an expression parent node, put expressionParentName as "n/a" as default value.
     *            
     * @param expressionParentNode
     *            the expression parent node
     * @return an Expression
     * @throws InvalidFormDefinitionException
     */
    public Expression parseExpression(final Node expressionParentNode) throws InvalidFormDefinitionException {
    	return parseExpression(NON_APPLICABLE_PREFIX, expressionParentNode);
    }
    
    /**
     * Parse an expression parent node
     *      
     * @param expressionParentName
     *            name of the parent widget
     *            
     * @param expressionParentNode
     *            the expression parent node
     * @return an Expression
     * @throws InvalidFormDefinitionException
     */
    public Expression parseExpression(final String expressionParentName, final Node expressionParentNode) throws InvalidFormDefinitionException {
        final Node expressionNode = getNodeByXpath(expressionParentNode, XMLForms.EXPRESSION);
        if (expressionNode != null) {
            return parseExpressionContent(expressionParentName, expressionNode);
        } else {
            return null;
        }
    }

    /**
     * Parse an expression node, put expressionParentName as "n/a" as default value.
     *         
     * @param expressionNode
     *            the expression node
     * @return an Expression
     * @throws InvalidFormDefinitionException
     */
    protected Expression parseExpressionContent(final Node expressionNode) throws InvalidFormDefinitionException {
    	return  parseExpressionContent(NON_APPLICABLE_PREFIX, expressionNode);
    }
    
    /**
     * Parse an expression node
     *
     * @param expressionParentName
     *            name of the parent widget
     *         
     * @param expressionNode
     *            the expression node
     * @return an Expression
     * @throws InvalidFormDefinitionException
     */
    protected Expression parseExpressionContent(final String expressionParentName, final Node expressionNode) throws InvalidFormDefinitionException {
        final String name = expressionParentName + FormFieldValuesUtil.EXPRESSION_KEY_SEPARATOR + getStringByXpath(expressionNode, XMLForms.NAME);
        final String returnType = getStringByXpath(expressionNode, XMLForms.EXPRESSION_RETURN_TYPE);
        final String expressionType = getStringByXpath(expressionNode, XMLForms.EXPRESSION_TYPE);
        final String interpreter = getStringByXpath(expressionNode, XMLForms.EXPRESSION_INTERPRETER);
        final String content = getStringByXpath(expressionNode, XMLForms.EXPRESSION_CONTENT);
        final Node expressionDependenciesNode = getNodeByXpath(expressionNode, XMLForms.DEPENDENCIES);
        final List<Expression> dependencies;
        if (expressionDependenciesNode != null) {
            dependencies = parseExpressionsList(expressionParentName, expressionDependenciesNode);
        } else {
            dependencies = new ArrayList<Expression>();
        }
        return new Expression(name, content, expressionType, returnType, interpreter, dependencies);
    }
}
