package org.bonitasoft.web.rest.model.bpm.cases;

import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author Emmanuel Duchastenier
 */
public class CaseDefinitionTest {

    @Test
    public void caseDefinitionShouldDefineSearchIndexLabelAttributes() {
        CaseDefinition caseDefinition = spy(new CaseDefinition());
        caseDefinition.defineAttributes();

        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_1_LABEL, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_2_LABEL, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_3_LABEL, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_4_LABEL, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_5_LABEL, ItemAttribute.TYPE.STRING);
    }

    @Test
    public void caseDefinitionShouldDefineSearchIndexVAlueAttributes() {
        CaseDefinition caseDefinition = spy(new CaseDefinition());
        caseDefinition.defineAttributes();

        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_1_VALUE, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_2_VALUE, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_3_VALUE, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_4_VALUE, ItemAttribute.TYPE.STRING);
        verify(caseDefinition).createAttribute(CaseItem.ATTRIBUTE_SEARCH_INDEX_5_VALUE, ItemAttribute.TYPE.STRING);
    }
}
