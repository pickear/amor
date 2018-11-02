package com.amor.core.protocol;

import org.apache.commons.lang3.StringUtils;
import org.msgpack.annotation.Message;

/**
 * @author dylan
 * @time 2017/6/23
 */
@Message
public class AbstractProtocol{

    protected String entityClass;

    public AbstractProtocol() {
    }

    public AbstractProtocol(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getEntityClass() {
        if(StringUtils.isBlank(entityClass)){
            throw new RuntimeException("子类请设置entityClass为子类的类名");
        }
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }
}
