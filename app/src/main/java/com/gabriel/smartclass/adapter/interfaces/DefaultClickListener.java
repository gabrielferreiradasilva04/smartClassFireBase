package com.gabriel.smartclass.adapter.interfaces;

import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

public interface DefaultClickListener<T extends SimpleAuxEntity> {
    void onClick(T t);
}
