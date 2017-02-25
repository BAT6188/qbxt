package com.ushine.abstracttest;

import org.apache.log4j.Logger;
import org.junit.Assert;
/**
 * 继承自Assert<br>
 * 不需要每次都静态引入
 * @author dh
 *
 */
public class AbstractAssert extends Assert {

	protected Logger logger=Logger.getLogger(AbstractAssert.class);
}
