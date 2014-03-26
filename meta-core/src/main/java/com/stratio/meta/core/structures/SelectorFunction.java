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

package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

import java.util.ArrayList;

public class SelectorFunction extends SelectorMeta {

    private String name;
    private ArrayList<SelectorMeta> params;

    public SelectorFunction(String name, ArrayList<SelectorMeta> params) {
        this.type = TYPE_FUNCTION;
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SelectorMeta> getParams() {
        return params;
    }

    public void setParams(ArrayList<SelectorMeta> params) {
        this.params = params;
    }        
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(").append(ParserUtils.stringList(params, ", ")).append(")");
        return sb.toString();
    }
    
}
