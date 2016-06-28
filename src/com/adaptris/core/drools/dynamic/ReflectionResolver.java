package com.adaptris.core.drools.dynamic;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.drools.Resolver;
import com.adaptris.core.drools.RuleException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Class for manipulating java beans using Reflection.
 * 
 * @config drools-reflection-resolver
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-reflection-resolver")
public class ReflectionResolver implements Resolver {
  private transient Logger logR = LoggerFactory.getLogger(ReflectionResolver.class);

  @NotBlank
  private String beanClassname;
  @NotNull
  @AutoPopulated
  @Valid
  private List<JavabeanMapper> toBeanMappers;
  @NotNull
  @AutoPopulated
  @Valid
  private List<JavabeanMapper> fromBeanMappers;

  public ReflectionResolver() {
    setToBeanMappers(new ArrayList<JavabeanMapper>());
    setFromBeanMappers(new ArrayList<JavabeanMapper>());
  }

  /**
   *
   * @see Resolver#create(com.adaptris.core.AdaptrisMessage)
   */
  @Override
  public Object[] create(AdaptrisMessage msg) throws RuleException {
    Object result = null;
    try {
      result = Class.forName(getBeanClassname()).newInstance();
      for (JavabeanMapper f : getToBeanMappers()) {
        f.translate(msg, result);
      }
    }
    catch (Exception e) {
      throw new RuleException(e);
    }
    return new Object[]
    {
      result
    };
  }

  /**
   *
   * @see Resolver#resolve(java.lang.Object[],
   *      com.adaptris.core.AdaptrisMessage)
   */
  @Override
  public void resolve(Object[] src, AdaptrisMessage msg) throws RuleException {
    Object result = src[0];
    try {
      for (JavabeanMapper f : getFromBeanMappers()) {
        f.translate(result, msg);
      }
    }
    catch (Exception e) {
      throw new RuleException(e);
    }
  }

  /**
   * @return the factClass
   */
  public String getBeanClassname() {
    return beanClassname;
  }

  /**
   * @param clazz the bean classname, which will be created with the standard no
   *          param constructor.
   */
  public void setBeanClassname(String clazz) {
    beanClassname = clazz;
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  public void init() throws CoreException {
    try {
      Class clazz = Class.forName(beanClassname);
      clazz.newInstance();
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#start()
   */
  @Override
  public void start() throws CoreException {

  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#stop()
   */
  @Override
  public void stop() {

  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#close()
   */
  @Override
  public void close() {

  }

  @Override
  public void prepare() throws CoreException {}

  /**
   * @return the toBeanMappers
   */
  public List<JavabeanMapper> getToBeanMappers() {
    return toBeanMappers;
  }

  /**
   * Set the mappers that will map from the AdaptrisMessage to the JavaBean
   *
   * @param l the fieldMappers to set
   */
  public void setToBeanMappers(List<JavabeanMapper> l) {
    toBeanMappers = l;
  }

  public void addToBeanMapper(JavabeanMapper m) {
    toBeanMappers.add(m);
  }

  /**
   * @return the fromBeanMappers
   */
  public List<JavabeanMapper> getFromBeanMappers() {
    return fromBeanMappers;
  }

  /**
   * Set the mappers that will map from the javabean to the AdaptrisMessage
   *
   * @param l the fromBeanMappers to set
   */
  public void setFromBeanMappers(List<JavabeanMapper> l) {
    fromBeanMappers = l;
  }

  public void addFromBeanMapper(JavabeanMapper m) {
    fromBeanMappers.add(m);
  }

}
