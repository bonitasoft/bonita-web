package org.bonitasoft.reporting.form.components;

import java.util.HashMap;

import org.bonitasoft.reporting.utils.BonitaSystem;

public interface IComponent {

    /**
     * return the name of the component
     * 
     * @return
     */
    public String getName();

    /**
     * return the label of the component
     * this label is translated in the locale of the connected user
     * 
     * @return
     */
    public String getLabel();

    /**
     * return the identifier of the component
     * this identifier is used to differanciate html element
     * 
     * @return
     */
    public String getId();

    /**
     * retunr the type of component :
     * SELECT
     * DATE
     * DATE_RANGE
     * 
     * @return
     */
    public String getType();

    /**
     * get values that must showed in the component
     * 
     * @return
     */
    public HashMap<String, String> getvalues();

    /**
     * get the default value of the componentn
     * can be change with url parameter value
     * 
     * @return
     */
    public HashMap<String, String> getDefaultValues();

    /**
     * set the name of the component
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * set the label of the component
     * 
     * @param label
     */
    public void setLabel(String label);

    /**
     * set the identifier of the component
     * 
     * @param id
     */
    public void setId(String id);

    /**
     * set the type of the component
     * SELECT
     * DATE
     * DATE_RANGE
     * 
     * @param type
     */
    public void setType(String type);

    /**
     * set the values of the component
     * 
     * @param values
     */
    public void setvalues(HashMap<String, String> values);

    /**
     * set the default values of the component
     * 
     * @param defaultValues
     */
    public void setDefaultValues(HashMap<String, String> defaultValues);

    /**
     * get the specific css code fot the component
     * 
     * @return
     */
    public String getCssCode();

    /**
     * get the specific JS Code for the component
     * 
     * @return
     */
    public String getJsCode();

    /**
     * get specific libraries of Css for the component
     * 
     * @return
     */
    public HashMap<String, String> getCssLibraries();

    /**
     * get specific libraries of JavaScript for the component
     * 
     * @return
     */
    public HashMap<String, String> getJsLibraries();

    /**
     * get the Html code for this componennt
     * 
     * @return
     */
    public String getBodyHtml();

    /**
     * to String method
     * 
     * @return
     */
    @Override
    public String toString();

    /**
     * system properties
     */
    public BonitaSystem getBonitaSystem();

    public void setBonitaSystem(BonitaSystem sys);
}
