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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a generic Statement supported by the META language.
 */
public abstract class MetaStatement {

    /**
     * The String representation of the query to be executed prior parsing it.
     */
    protected String query;

    /**
     * The execution path for the query.
     */
    protected MetaPath path;

    /**
     * Whether the query is an internal command or it returns a {@link com.stratio.meta.common.data.ResultSet}.
     */
    protected boolean command;

    /**
     * Default class constructor.
     */
    public MetaStatement() {
    }

    /**
     * Class constructor.
     * @param query The string representation of the submitted query.
     * @param path The path to be used to execute the query.
     * @param command Whether the query is a command or a query returning a {@link com.stratio.meta.common.data.ResultSet}.
     */
    public MetaStatement(String query, MetaPath path, boolean command) {
        this.query = query;
        this.path = path;
        this.command = command;
    }

    /**
     * Whether the query is an internal command or not.
     * @return The boolean value.
     */
    public boolean isCommand() {
        return command;
    }

    /**
     * Set whether the query is a command or not.
     * @param command The boolean value.
     */
    public void setAsCommand(boolean command) {
        this.command = command;
    }

    /**
     * Get the query introduced by the user.
     * @return The query.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the query introduced by the user.
     * @param query The string representation of the submitted query.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Get the path required to execute the query.
     * @return The {@link com.stratio.meta.core.utils.MetaPath} with a
     * {@link com.stratio.meta.core.utils.Tree} representation of the different
     * steps involved.
     */
    public MetaPath getPath() {
        return path;
    }

    /**
     * Set the {@link com.stratio.meta.core.utils.MetaPath} required to
     * execute this query.
     * @param path The path.
     */
    public void setPath(MetaPath path) {
        this.path = path;
    }        
    
    @Override
    public abstract String toString();

    /**
     * Validate the semantics of the current statement. This method checks the
     * existing metadata to determine that all referenced entities exists in the
     * {@code targetKeyspace} and the types are compatible with the assignations
     * or comparisons.
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param targetKeyspace The target keyspace where the query will be executed.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    public Result validate(MetadataManager metadata, String targetKeyspace){
        return QueryResult.createFailQueryResult("Statement not supported");
    }


    /**
     * Validate that a valid keyspace and table is present.
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param targetKeyspace The target keyspace where the query will be executed.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    protected Result validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace,
                                            boolean keyspaceInc, String stmtKeyspace, String tableName){
        Result result = QueryResult.createSuccessQueryResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = stmtKeyspace;
        }

        //Check that the keyspace and table exists.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result= QueryResult.createFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result= QueryResult.createFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
                if (tableMetadata == null) {
                    result= QueryResult.createFailQueryResult("Table " + tableName + " does not exists.");
                }
            }

        }
        return result;
    }

    protected String getEffectiveKeyspace(String targetKeyspace, boolean keyspaceInc, String stmtKeyspace){
        String effectiveKs = targetKeyspace;
        if(keyspaceInc){
            effectiveKs = stmtKeyspace;
        }
        return effectiveKs;
    }

    /**
     * Create a suggestion for a viable statement in case a parsing error occurs.
     * @return The suggestion for solving the parsing error.
     */
    public abstract String getSuggestion();

    /**
     * Translate the statement into the CQL equivalent when possible.
     * @return The CQL equivalent.
     */
    public abstract String translateToCQL();

    /**
     * Get the {@link Statement} equivalent of the current query.
     * @return The Statement or null if the driver translation cannot be done.
     */
    public Statement getDriverStatement(){
        return null;
    }

    /**
     * Execute the statement in Stratio Deep.
     * @return A {@link com.stratio.meta.common.data.DeepResultSet} with the result.
     */
    public abstract DeepResultSet executeDeep();

    /**
     * Get a tree that contains the planning for executing the query.
     * The plan will be executed starting from the leaves and finishing at the tree root.
     * @param metadataManager The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param targetKeyspace The target keyspace where the query will be executed.
     * @return A {@link com.stratio.meta.core.utils.Tree} with the execution plan.
     */
    public abstract Tree getPlan(MetadataManager metadataManager, String targetKeyspace);
    
}
