package com.amor.common.model;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by dylan on 2017/10/1.
 */

public abstract class Model{

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
