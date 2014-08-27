/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.engine.validator;

import java.util.List;

import com.stratio.meta2.common.exception.validation.MustExistCatalogException;
import com.stratio.meta2.common.exception.validation.NotMustExistCatalogException;
import com.stratio.meta2.common.exception.validation.ValidationException;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.common.data.CatalogName;
import org.apache.log4j.Logger;


import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import com.stratio.meta2.core.statements.IStatement;

public class Validator {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Validator.class);

  public ValidatedQuery validate(ParsedQuery parsedQuery) throws ValidationException {
    this.validate(parsedQuery);
    return new ValidatedQuery(parsedQuery);
  }


  private void validate(Validation requirement, ParsedQuery statement) throws ValidationException  {
    switch (requirement) {
      case MUST_NOT_EXIST_CATALOG:
        validateNotExistCatalog(statement.getCatalogs(), statement.getIfNotExists());
        break;
      case MUST_EXIST_CATALOG:
        validateExistCatalog(statement.getCatalogs(), statement.getIfExists());
        break;

    }
  }

  private void validateExistCatalog(List<CatalogName> catalogs, boolean hasIfExists)
      throws MustExistCatalogException {
    for(CatalogName catalog:catalogs){
      this.validateExistCatalog(catalog, hasIfExists);
    }

  }

  private void validateExistCatalog(CatalogName catalog, boolean hasIfExists)
      throws MustExistCatalogException {
    if(!MetadataManager.MANAGER.existsCatalog(catalog)){
      if(!hasIfExists){
        throw new MustExistCatalogException(catalog);
      }
    }
  }

  private void validateNotExistCatalog(List<CatalogName> catalogs, boolean hasIfNotExist) throws NotMustExistCatalogException {
    for(CatalogName catalog:catalogs){
      this.validateNotExistCatalog(catalog,hasIfNotExist);
    }
  }


  private void validateNotExistCatalog(CatalogName catalog, boolean onlyIfNotExis) throws NotMustExistCatalogException{
    if(MetadataManager.MANAGER.existsCatalog(catalog)){
      if(!onlyIfNotExis){
        throw new NotMustExistCatalogException(catalog);
      }
    }
  }

}