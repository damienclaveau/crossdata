/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class GreaterThan extends Function<Cells, Boolean> implements Serializable {
    private static final long serialVersionUID = 2675616112608139116L;

    private Object value;
    private String field;

    /**
     * GreaterThan apply > filter to a field in a Deep Cell.
     * @param field Name of the field to check.
     * @param value Value to compare to.
     */
    public GreaterThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells){
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            return value == null;
        }
        return ((Comparable) currentValue).compareTo(value) > 0;
    }
}
